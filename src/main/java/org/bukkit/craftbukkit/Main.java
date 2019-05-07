/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  jline.UnsupportedTerminal
 *  joptsimple.ArgumentAcceptingOptionSpec
 *  joptsimple.OptionException
 *  joptsimple.OptionParser
 *  joptsimple.OptionSet
 *  joptsimple.OptionSpecBuilder
 *  org.fusesource.jansi.AnsiConsole
 */
package org.bukkit.craftbukkit;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jline.UnsupportedTerminal;
import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpecBuilder;
import org.fusesource.jansi.AnsiConsole;

public class Main {
    public static boolean useJline = true;
    public static boolean useConsole = true;

    public static OptionSet main(String[] args) {
        OptionParser parser = new OptionParser(){};
        OptionSet options = null;
        try {
            options = parser.parse(args);
        }
        catch (OptionException ex2) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, ex2.getLocalizedMessage());
        }
        if (options == null || options.has("?")) {
            try {
                parser.printHelpOn((OutputStream)System.out);
            }
            catch (IOException ex3) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex3);
            }
        } else {
            String path = new File(".").getAbsolutePath();
            if (path.contains("!") || path.contains("+")) {
                System.err.println("Cannot run server in a directory with ! or + in the pathname. Please rename the affected folders and try again.");
                return null;
            }
            try {
                String jline_UnsupportedTerminal = new String(new char[]{'j', 'l', 'i', 'n', 'e', '.', 'U', 'n', 's', 'u', 'p', 'p', 'o', 'r', 't', 'e', 'd', 'T', 'e', 'r', 'm', 'i', 'n', 'a', 'l'});
                String jline_terminal = new String(new char[]{'j', 'l', 'i', 'n', 'e', '.', 't', 'e', 'r', 'm', 'i', 'n', 'a', 'l'});
                boolean bl2 = Main.useJline = !jline_UnsupportedTerminal.equals(System.getProperty(jline_terminal));
                if (options.has("nojline")) {
                    System.setProperty("user.language", "en");
                    useJline = false;
                }
                if (useJline) {
                    AnsiConsole.systemInstall();
                } else {
                    System.setProperty("jline.terminal", UnsupportedTerminal.class.getName());
                }
                if (options.has("noconsole")) {
                    useConsole = false;
                }
                System.out.println("Loading libraries, please wait...");
            }
            catch (Throwable t2) {
                t2.printStackTrace();
            }
            return options;
        }
        return null;
    }

    private static /* varargs */ List<String> asList(String ... params) {
        return Arrays.asList(params);
    }

}

