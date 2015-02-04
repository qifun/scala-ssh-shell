package peak6.util

import java.io.InputStream
import java.io.OutputStream

import org.apache.sshd.server.Command
import org.apache.sshd.server.ExitCallback

private[util] trait DefaultCommand extends Command {
  var _inputStream: InputStream = null
  var _outputStream: OutputStream = null
  var _errorStream: OutputStream = null
  var _exitCallback: ExitCallback = null

  protected final def inputStream = _inputStream
  protected final def outputStream = _outputStream
  protected final def errorStream = _errorStream
  protected final def exitCallback = _exitCallback

  override final def destroy() {}

  override final def setInputStream(inputStream: InputStream) {
    this._inputStream = inputStream
  }

  override final def setOutputStream(outputStream: OutputStream) {
    this._outputStream = outputStream
  }

  override final def setErrorStream(errorStream: OutputStream) {
    this._errorStream = errorStream
  }

  override final def setExitCallback(exitCallback: ExitCallback) {
    this._exitCallback = exitCallback
  }

}