package peak6.util

import jline.TerminalSupport

/**
 * SshTerminal is like UnixTerminal, but it does not execute stty.
 *
 * @author <a href="mailto:pop.atry@gmail.com">杨博</a>
 */
final class SshTerminal extends TerminalSupport(true) {
  override protected final def init() = {
    super.init();
    setAnsiSupported(true);
    setEchoEnabled(false);
  }
}