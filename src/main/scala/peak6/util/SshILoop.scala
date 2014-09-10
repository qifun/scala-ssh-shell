/* NSC -- new Scala compiler
 * Copyright 2005-2011 LAMP/EPFL
 * @author Alexander Spoon
 */

package peak6.util

import java.io.PrintWriter
import scala.concurrent.Await
import scala.tools.nsc.Settings
import scala.reflect.internal.util.ScalaClassLoader
import scala.tools.nsc.interpreter.AbstractOrMissingHandler
import scala.tools.nsc.interpreter.InteractiveReader
import java.io.InputStream
import java.io.OutputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import org.apache.commons.codec.Charsets
import scala.tools.nsc.interpreter._
import StdReplTags._
import scala.reflect.classTag
import scala.tools.util._
import scala.concurrent.ExecutionContext.Implicits._
import scala.tools.nsc.interpreter.session._

final class SshILoop(inputStream: InputStream, outputStream: OutputStream, bindValues: NamedParam*)
  extends scala.tools.nsc.interpreter.ILoop(
    None,
    new PrintWriter(new OutputStreamWriter(outputStream, Charsets.UTF_8))) {

  override final def chooseReader(settings: Settings): InteractiveReader = {
    new SshJLineReader(inputStream, outputStream, new JLineCompletion(intp))
  }

  override final def createInterpreter(): Unit = {
    if (addedClasspath != "")
      settings.classpath append addedClasspath
    intp = new ILoopInterpreter {
      override final def initializeSynchronous(): Unit = {
        super.initializeSynchronous()
        bindValues.foreach(intp.quietBind)
        in.asInstanceOf[SshJLineReader].consoleReader.postInit
      }
    }
  }

}

