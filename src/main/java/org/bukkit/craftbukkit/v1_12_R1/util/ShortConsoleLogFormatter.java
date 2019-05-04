/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  joptsimple.OptionException
 *  joptsimple.OptionSet
 */
package org.bukkit.craftbukkit.v1_12_R1.util;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import joptsimple.OptionException;
import joptsimple.OptionSet;
import net.minecraft.server.MinecraftServer;

public class ShortConsoleLogFormatter
extends Formatter {
    private final SimpleDateFormat date;

    public ShortConsoleLogFormatter(MinecraftServer server) {
        SimpleDateFormat date;
        OptionSet options = server.options;
        date = null;
        if (options.has("date-format")) {
            try {
                Object object = options.valueOf("date-format");
                if (object != null && object instanceof SimpleDateFormat) {
                    date = (SimpleDateFormat)object;
                }
            }
            catch (OptionException ex2) {
                System.err.println("Given date format is not valid. Falling back to default.");
            }
        } else if (options.has("nojline")) {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        if (date == null) {
            date = new SimpleDateFormat("HH:mm:ss");
        }
        this.date = date;
    }

    @Override
    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder();
        Throwable ex2 = record.getThrown();
        builder.append(this.date.format(record.getMillis()));
        builder.append(" [");
        builder.append(record.getLevel().getLocalizedName().toUpperCase());
        builder.append("] ");
        builder.append(this.formatMessage(record));
        builder.append('\n');
        if (ex2 != null) {
            StringWriter writer = new StringWriter();
            ex2.printStackTrace(new PrintWriter(writer));
            builder.append(writer);
        }
        return builder.toString();
    }
}

