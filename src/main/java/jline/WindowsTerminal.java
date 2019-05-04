/*
 * Copyright (c) 2002-2016, the original author or authors.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package jline;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import jline.internal.Configuration;
import jline.internal.Log;
import org.fusesource.jansi.internal.WindowsSupport;
import org.fusesource.jansi.internal.Kernel32;
import static org.fusesource.jansi.internal.Kernel32.*;

import static jline.WindowsTerminal.ConsoleMode.ENABLE_ECHO_INPUT;
import static jline.WindowsTerminal.ConsoleMode.ENABLE_LINE_INPUT;
import static jline.WindowsTerminal.ConsoleMode.ENABLE_PROCESSED_INPUT;
import static jline.WindowsTerminal.ConsoleMode.ENABLE_WINDOW_INPUT;

/**
 * Terminal implementation for Microsoft Windows. Terminal initialization in
 * {@link #init} is accomplished by extracting the
 * <em>jline_<i>version</i>.dll</em>, saving it to the system temporary
 * directoy (determined by the setting of the <em>java.io.tmpdir</em> System
 * property), loading the library, and then calling the Win32 APIs <a
 * href="http://msdn.microsoft.com/library/default.asp?
 * url=/library/en-us/dllproc/base/setconsolemode.asp">SetConsoleMode</a> and
 * <a href="http://msdn.microsoft.com/library/default.asp?
 * url=/library/en-us/dllproc/base/getconsolemode.asp">GetConsoleMode</a> to
 * disable character echoing.
 * <p/>
 * <p>
 * By default, the {@link #wrapInIfNeeded(java.io.InputStream)} method will attempt
 * to test to see if the specified {@link InputStream} is {@link System#in} or a wrapper
 * around {@link FileDescriptor#in}, and if so, will bypass the character reading to
 * directly invoke the readc() method in the JNI library. This is so the class
 * can read special keys (like arrow keys) which are otherwise inaccessible via
 * the {@link System#in} stream. Using JNI reading can be bypassed by setting
 * the <code>jline.WindowsTerminal.directConsole</code> system property
 * to <code>false</code>.
 * </p>
 *
 * @author <a href="mailto:mwp1@cornell.edu">Marc Prud'hommeaux</a>
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 2.0
 */
public class WindowsTerminal
    extends TerminalSupport
{
    public static final String DIRECT_CONSOLE = WindowsTerminal.class.getName() + ".directConsole";

    public static final String ANSI = WindowsTerminal.class.getName() + ".ansi";

    private boolean directConsole;

    private int originalMode;

    public WindowsTerminal() throws Exception {
        super(true);
    }

    @Override
    public void init() throws Exception {
        super.init();

        setAnsiSupported(Configuration.getBoolean(ANSI, true));

        //
        // FIXME: Need a way to disable direct console and sysin detection muck
        //

        setDirectConsole(Configuration.getBoolean(DIRECT_CONSOLE, true));

        this.originalMode = getConsoleMode();
        setConsoleMode(originalMode & ~ENABLE_ECHO_INPUT.code);
        setEchoEnabled(false);
    }

    /**
     * Restore the original terminal configuration, which can be used when
     * shutting down the console reader. The ConsoleReader cannot be
     * used after calling this method.
     */
    @Override
    public void restore() throws Exception {
        // restore the old console mode
        setConsoleMode(originalMode);
        super.restore();
    }

    @Override
    public int getWidth() {
        int w = getWindowsTerminalWidth();
        return w < 1 ? DEFAULT_WIDTH : w;
    }

    @Override
    public int getHeight() {
        int h = getWindowsTerminalHeight();
        return h < 1 ? DEFAULT_HEIGHT : h;
    }

    @Override
    public void setEchoEnabled(final boolean enabled) {
        // Must set these four modes at the same time to make it work fine.
        if (enabled) {
            setConsoleMode(getConsoleMode() |
                ENABLE_ECHO_INPUT.code |
                ENABLE_LINE_INPUT.code |
                ENABLE_WINDOW_INPUT.code);
        }
        else {
            setConsoleMode(getConsoleMode() &
                ~(ENABLE_LINE_INPUT.code |
                    ENABLE_ECHO_INPUT.code |
                    ENABLE_WINDOW_INPUT.code));
        }
        super.setEchoEnabled(enabled);
    }

    public void disableInterruptCharacter() {
        setConsoleMode(getConsoleMode() &
            ~(ENABLE_PROCESSED_INPUT.code));
    }

    public void enableInterruptCharacter() {
        setConsoleMode(getConsoleMode() |
            ENABLE_PROCESSED_INPUT.code);
    }

    /**
     * Whether or not to allow the use of the JNI console interaction.
     */
    public void setDirectConsole(final boolean flag) {
        this.directConsole = flag;
        Log.debug("Direct console: ", flag);
    }

    /**
     * Whether or not to allow the use of the JNI console interaction.
     */
    public Boolean getDirectConsole() {
        return directConsole;
    }


    @Override
    public InputStream wrapInIfNeeded(InputStream in) throws IOException {
        if (directConsole && isSystemIn(in)) {
            return new InputStream() {
                private byte[] buf = null;
                int bufIdx = 0;

                @Override
                public int read() throws IOException {
                    while (buf == null || bufIdx == buf.length) {
                        buf = readConsoleInput();
                        bufIdx = 0;
                    }
                    int c = buf[bufIdx] & 0xFF;
                    bufIdx++;
                    return c;
                }
            };
        } else {
            return super.wrapInIfNeeded(in);
        }
    }

    protected boolean isSystemIn(final InputStream in) throws IOException {
        if (in == null) {
            return false;
        }
        else if (in == System.in) {
            return true;
        }
        else if (in instanceof FileInputStream && ((FileInputStream) in).getFD() == FileDescriptor.in) {
            return true;
        }

        return false;
    }

    @Override
    public String getOutputEncoding() {
        int codepage = getConsoleOutputCodepage();
        //http://docs.oracle.com/javase/6/docs/technotes/guides/intl/encoding.doc.html
        String charsetMS = "ms" + codepage;
        if (java.nio.charset.Charset.isSupported(charsetMS)) {
            return charsetMS;
        }
        String charsetCP = "cp" + codepage;
        if (java.nio.charset.Charset.isSupported(charsetCP)) {
            return charsetCP;
        }
        Log.debug("can't figure out the Java Charset of this code page (" + codepage + ")...");
        return super.getOutputEncoding();
    }

    //
    // Native Bits
    //
    private static int getConsoleMode() {
        return WindowsSupport.getConsoleMode();
    }

    private static void setConsoleMode(int mode) {
        WindowsSupport.setConsoleMode(mode);
    }

    private byte[] readConsoleInput() {
        // XXX does how many events to read in one call matter?
        INPUT_RECORD[] events = null;
        try {
            events = WindowsSupport.readConsoleInput(1);
        } catch (IOException e) {
            Log.debug("read Windows console input error: ", e);
        }
        if (events == null) {
            return new byte[0];
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < events.length; i++ ) {
            KEY_EVENT_RECORD keyEvent = events[i].keyEvent;
            //Log.trace(keyEvent.keyDown? "KEY_DOWN" : "KEY_UP", "key code:", keyEvent.keyCode, "char:", (long)keyEvent.uchar); 
            if (keyEvent.keyDown) {
                if (keyEvent.uchar > 0) {
                    // support some C1 control sequences: ALT + [@-_] (and [a-z]?) => ESC <ascii>
                    // http://en.wikipedia.org/wiki/C0_and_C1_control_codes#C1_set
                    final int altState = KEY_EVENT_RECORD.LEFT_ALT_PRESSED | KEY_EVENT_RECORD.RIGHT_ALT_PRESSED;
                    // Pressing "Alt Gr" is translated to Alt-Ctrl, hence it has to be checked that Ctrl is _not_ pressed,
                    // otherwise inserting of "Alt Gr" codes on non-US keyboards would yield errors
                    final int ctrlState = KEY_EVENT_RECORD.LEFT_CTRL_PRESSED | KEY_EVENT_RECORD.RIGHT_CTRL_PRESSED;
                    if (((keyEvent.uchar >= '@' && keyEvent.uchar <= '_') || (keyEvent.uchar >= 'a' && keyEvent.uchar <= 'z'))
                        && ((keyEvent.controlKeyState & altState) != 0) && ((keyEvent.controlKeyState & ctrlState) == 0)) {
                        sb.append('\u001B'); // ESC
                    }

                    sb.append(keyEvent.uchar);
                    continue;
                }
                // virtual keycodes: http://msdn.microsoft.com/en-us/library/windows/desktop/dd375731(v=vs.85).aspx
                // just add support for basic editing keys (no control state, no numpad keys)
                String escapeSequence = null;
                switch (keyEvent.keyCode) {
                case 0x21: // VK_PRIOR PageUp
                    escapeSequence = "\u001B[5~";
                    break;
                case 0x22: // VK_NEXT PageDown
                    escapeSequence = "\u001B[6~";
                    break;
                case 0x23: // VK_END
                    escapeSequence = "\u001B[4~";
                    break;
                case 0x24: // VK_HOME
                    escapeSequence = "\u001B[1~";
                    break;
                case 0x25: // VK_LEFT
                    escapeSequence = "\u001B[D";
                    break;
                case 0x26: // VK_UP
                    escapeSequence = "\u001B[A";
                    break;
                case 0x27: // VK_RIGHT
                    escapeSequence = "\u001B[C";
                    break;
                case 0x28: // VK_DOWN
                    escapeSequence = "\u001B[B";
                    break;
                case 0x2D: // VK_INSERT
                    escapeSequence = "\u001B[2~";
                    break;
                case 0x2E: // VK_DELETE
                    escapeSequence = "\u001B[3~";
                    break;
                default:
                    break;
                }
                if (escapeSequence != null) {
                    for (int k = 0; k < keyEvent.repeatCount; k++) {
                        sb.append(escapeSequence);
                    }
                }
            } else {
                // key up event
                // support ALT+NumPad input method
                if (keyEvent.keyCode == 0x12/*VK_MENU ALT key*/ && keyEvent.uchar > 0) {
                    sb.append(keyEvent.uchar);
                }
            }
        }
        return sb.toString().getBytes();
    }

    private static int getConsoleOutputCodepage() {
        return Kernel32.GetConsoleOutputCP();
    }

    private static int getWindowsTerminalWidth() {
        return WindowsSupport.getWindowsTerminalWidth();
    }

    private static int getWindowsTerminalHeight() {
        return WindowsSupport.getWindowsTerminalHeight();
    }

    /**
     * Console mode
     * <p/>
     * Constants copied <tt>wincon.h</tt>.
     */
    public static enum ConsoleMode
    {
        /**
         * The ReadFile or ReadConsole function returns only when a carriage return
         * character is read. If this mode is disable, the functions return when one
         * or more characters are available.
         */
        ENABLE_LINE_INPUT(2),

        /**
         * Characters read by the ReadFile or ReadConsole function are written to
         * the active screen buffer as they are read. This mode can be used only if
         * the ENABLE_LINE_INPUT mode is also enabled.
         */
        ENABLE_ECHO_INPUT(4),

        /**
         * CTRL+C is processed by the system and is not placed in the input buffer.
         * If the input buffer is being read by ReadFile or ReadConsole, other
         * control keys are processed by the system and are not returned in the
         * ReadFile or ReadConsole buffer. If the ENABLE_LINE_INPUT mode is also
         * enabled, backspace, carriage return, and linefeed characters are handled
         * by the system.
         */
        ENABLE_PROCESSED_INPUT(1),

        /**
         * User interactions that change the size of the console screen buffer are
         * reported in the console's input buffee. Information about these events
         * can be read from the input buffer by applications using
         * theReadConsoleInput function, but not by those using ReadFile
         * orReadConsole.
         */
        ENABLE_WINDOW_INPUT(8),

        /**
         * If the mouse pointer is within the borders of the console window and the
         * window has the keyboard focus, mouse events generated by mouse movement
         * and button presses are placed in the input buffer. These events are
         * discarded by ReadFile or ReadConsole, even when this mode is enabled.
         */
        ENABLE_MOUSE_INPUT(16),

        /**
         * When enabled, text entered in a console window will be inserted at the
         * current cursor location and all text following that location will not be
         * overwritten. When disabled, all following text will be overwritten. An OR
         * operation must be performed with this flag and the ENABLE_EXTENDED_FLAGS
         * flag to enable this functionality.
         */
        ENABLE_PROCESSED_OUTPUT(1),

        /**
         * This flag enables the user to use the mouse to select and edit text. To
         * enable this option, use the OR to combine this flag with
         * ENABLE_EXTENDED_FLAGS.
         */
        ENABLE_WRAP_AT_EOL_OUTPUT(2),;

        public final int code;

        ConsoleMode(final int code) {
            this.code = code;
        }
    }

}
