/* NSC -- new Scala compiler
 * Copyright 2005-2013 LAMP/EPFL
 * @author Stepan Koltsov
 */

package peak6.util

import scala.tools.nsc.interpreter
import scala.tools.nsc.interpreter.Completion
import scala.tools.nsc.interpreter.ConsoleReaderHelper
import scala.tools.nsc.interpreter.IMain
import scala.tools.nsc.interpreter.InteractiveReader
import scala.tools.nsc.interpreter.JLineCompletion
import scala.tools.nsc.interpreter.JLineDelimiter
import scala.tools.nsc.interpreter.NoCompletion
import scala.tools.nsc.interpreter.VariColumnTabulator
import jline.console.ConsoleReader
import jline.console.completer._
import scala.tools.nsc.interpreter.session._
import Completion._
import java.io.InputStream
import java.io.OutputStream

/**
 *  Reads from the console using JLine.
 */
class JLineIOReader(
  in: InputStream,
  out: OutputStream,
  _completion: => Completion)
extends InteractiveReader {
  val interactive = true
  val consoleReader = new JLineConsoleReader()

  lazy val completion = _completion
  lazy val history: JLineHistory = JLineHistory()

  private def term = consoleReader.getTerminal()
  def reset() = term.reset()

  def scalaToJline(tc: ScalaCompleter): Completer = new Completer {
    def complete(_buf: String, cursor: Int, candidates: java.util.List[CharSequence]): Int = {
      val buf = if (_buf == null) "" else _buf
      val Candidates(newCursor, newCandidates) = tc.complete(buf, cursor)
      newCandidates foreach (candidates add _)
      newCursor
    }
  }

  class JLineConsoleReader
  extends ConsoleReader(in, out, new SshTerminal)
  with ConsoleReaderHelper
  with VariColumnTabulator {
    val isAcross = interpreter.`package`.isAcross

    this setPaginationEnabled interpreter.`package`.isPaged

    // ASAP
    this setExpandEvents false

    // working around protected/trait/java insufficiencies.
    def goBack(num: Int): Unit = back(num)
    if ((history: History) ne NoHistory)
      this setHistory history

    def readOneKey(prompt: String) = {
      this.print(prompt)
      this.flush()
      this.readCharacter()
    }
    def eraseLine() = consoleReader.resetPromptLine("", "", 0)
    def redrawLineAndFlush(): Unit = { flush(); drawLine(); flush() }

    // A hook for running code after the repl is done initializing.
    lazy val postInit: Unit = {
      this setBellEnabled false

      if (completion ne NoCompletion) {
        val argCompletor: ArgumentCompleter =
          new ArgumentCompleter(new JLineDelimiter, scalaToJline(completion.completer()))
        argCompletor setStrict false

        this addCompleter argCompletor
        this setAutoprintThreshold 400 // max completion candidates without warning
      }
    }
  }

  def redrawLine() = consoleReader.redrawLineAndFlush()
  def readOneLine(prompt: String) = consoleReader readLine prompt
  def readOneKey(prompt: String) = consoleReader readOneKey prompt
}

object JLineIOReader {
  def apply(intp: IMain,
            in: java.io.InputStream,
            out: java.io.OutputStream): JLineIOReader =
              apply(new JLineCompletion(intp), in, out)
  def apply(comp: Completion,
            in: java.io.InputStream,
            out: java.io.OutputStream): JLineIOReader =
              new JLineIOReader(in, out, comp)
}
