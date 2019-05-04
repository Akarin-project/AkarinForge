/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.mojang.util.QueueLogAppender
 *  jline.console.ConsoleReader
 *  jline.console.CursorBuffer
 */
package org.bukkit.craftbukkit.v1_12_R1.util;

import com.mojang.util.QueueLogAppender;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import jline.console.ConsoleReader;
import jline.console.CursorBuffer;
import org.bukkit.craftbukkit.v1_12_R1.Main;
import org.bukkit.craftbukkit.v1_12_R1.command.ColouredConsoleSender;

public class TerminalConsoleWriterThread
implements Runnable {
    private static final byte[] RESET_LINE = String.valueOf('\r').getBytes();
    private final ConsoleReader reader;
    private final OutputStream output;

    public TerminalConsoleWriterThread(OutputStream output, ConsoleReader reader) {
        this.output = output;
        this.reader = reader;
    }

    @Override
    public void run() {
        do {
            String message;
            if ((message = QueueLogAppender.getNextLogEvent((String)"TerminalConsole")) == null) {
                continue;
            }
            try {
                if (Main.useJline) {
                    this.output.write(RESET_LINE);
                    this.output.write(ColouredConsoleSender.toAnsiStr(message).getBytes());
                    this.output.flush();
                    try {
                        this.reader.drawLine();
                    }
                    catch (Throwable ex2) {
                        this.reader.getCursorBuffer().clear();
                    }
                    this.reader.flush();
                    continue;
                }
                this.output.write(message.getBytes());
                this.output.flush();
                continue;
            }
            catch (IOException ex3) {
                Logger.getLogger(TerminalConsoleWriterThread.class.getName()).log(Level.SEVERE, null, ex3);
                continue;
            }
            break;
        } while (true);
    }
}

