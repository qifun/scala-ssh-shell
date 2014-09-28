package peak6.util

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter

import scala.tools.nsc.Settings
import scala.tools.nsc.interpreter._

import org.apache.commons.codec.Charsets
import org.apache.sshd.server.CommandFactory
import org.apache.sshd.server.Environment

final class ReplCommandFactory(settingsFactory: () => Settings, boundValues: NamedParam*) extends CommandFactory {

  override final def createCommand(command: String) = new DefaultCommand {

    override final def start(env: Environment) {
      new Thread {
        override final def run() {
          val commandStream = {
            val stream = new ByteArrayOutputStream()
            val bytes = try {
              val reader = new OutputStreamWriter(stream, Charsets.UTF_8)
              try {
                reader.write(command)
                reader.write('\n')
              } finally {
                reader.close()
              }
              stream.flush()
              stream.toByteArray()
            } finally {
              stream.close()
            }
            new ByteArrayInputStream(bytes)
          }
          val repl: ILoop = new SshILoop(commandStream, outputStream, boundValues: _*)
          try {
            repl.process(settingsFactory())
            exitCallback.onExit(0)
          } catch {
            case e: Throwable =>
              exitCallback.onExit(1, e.getMessage)
          } finally {
            repl.closeInterpreter()
          }
        }
      }.start()
    }
  }

}
