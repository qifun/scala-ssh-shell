package peak6.util

import scala.tools.nsc.Settings
import scala.tools.nsc.interpreter._

import org.apache.sshd.common.Factory
import org.apache.sshd.server.Command
import org.apache.sshd.server.Environment

final class ReplShellFactory(settingsFactory: () => Settings, boundValues: NamedParam*) extends Factory[Command] {

  override final def create = new DefaultCommand {
    override final def start(env: Environment) {
      new Thread {
        override final def run() {
          val repl: ILoop = new SshILoop(inputStream, outputStream, boundValues: _*)
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
