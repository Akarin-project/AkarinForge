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
package org.bukkit.craftbukkit.v1_12_R1;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpecBuilder;

public class Main {
    public static boolean useJline = true;
    public static boolean useConsole = true;

    public static OptionSet main(String[] args) {
        OptionParser parser = new OptionParser() {
            {
                acceptsAll(asList("?", "help"), "Show the help");

                acceptsAll(asList("c", "config"), "Properties file to use")
                        .withRequiredArg()
                        .ofType(File.class)
                        .defaultsTo(new File("server.properties"))
                        .describedAs("Properties file");

                acceptsAll(asList("P", "plugins"), "Plugin directory to use")
                        .withRequiredArg()
                        .ofType(File.class)
                        .defaultsTo(new File("plugins"))
                        .describedAs("Plugin directory");

                acceptsAll(asList("h", "host", "server-ip"), "Host to listen on")
                        .withRequiredArg()
                        .ofType(String.class)
                        .describedAs("Hostname or IP");

                acceptsAll(asList("W", "world-dir", "universe", "world-container"), "World container")
                        .withRequiredArg()
                        .ofType(File.class)
                        .describedAs("Directory containing worlds");

                acceptsAll(asList("w", "world", "level-name"), "World name")
                        .withRequiredArg()
                        .ofType(String.class)
                        .describedAs("World name");

                acceptsAll(asList("p", "port", "server-port"), "Port to listen on")
                        .withRequiredArg()
                        .ofType(Integer.class)
                        .describedAs("Port");

                acceptsAll(asList("o", "online-mode"), "Whether to use online authentication")
                        .withRequiredArg()
                        .ofType(Boolean.class)
                        .describedAs("Authentication");

                acceptsAll(asList("s", "size", "max-players"), "Maximum amount of players")
                        .withRequiredArg()
                        .ofType(Integer.class)
                        .describedAs("Server size");

                acceptsAll(asList("d", "date-format"), "Format of the date to display in the console (for log entries)")
                        .withRequiredArg()
                        .ofType(SimpleDateFormat.class)
                        .describedAs("Log date format");

                acceptsAll(asList("log-pattern"), "Specfies the log filename pattern")
                        .withRequiredArg()
                        .ofType(String.class)
                        .defaultsTo("server.log")
                        .describedAs("Log filename");

                acceptsAll(asList("log-limit"), "Limits the maximum size of the log file (0 = unlimited)")
                        .withRequiredArg()
                        .ofType(Integer.class)
                        .defaultsTo(0)
                        .describedAs("Max log size");

                acceptsAll(asList("log-count"), "Specified how many log files to cycle through")
                        .withRequiredArg()
                        .ofType(Integer.class)
                        .defaultsTo(1)
                        .describedAs("Log count");

                acceptsAll(asList("log-append"), "Whether to append to the log file")
                        .withRequiredArg()
                        .ofType(Boolean.class)
                        .defaultsTo(true)
                        .describedAs("Log append");

                acceptsAll(asList("log-strip-color"), "Strips color codes from log file");

                acceptsAll(asList("b", "bukkit-settings"), "File for bukkit settings")
                        .withRequiredArg()
                        .ofType(File.class)
                        .defaultsTo(new File("bukkit.yml"))
                        .describedAs("Yml file");

                acceptsAll(asList("C", "commands-settings"), "File for command settings")
                        .withRequiredArg()
                        .ofType(File.class)
                        .defaultsTo(new File("commands.yml"))
                        .describedAs("Yml file");

                acceptsAll(asList("nojline"), "Disables jline and emulates the vanilla console");

                acceptsAll(asList("noconsole"), "Disables the console");

                acceptsAll(asList("v", "version"), "Show the CraftBukkit Version");

                acceptsAll(asList("demo"), "Demo mode");

                // Spigot Start
                acceptsAll(asList("S", "spigot-settings"), "File for spigot settings")
                        .withRequiredArg()
                        .ofType(File.class)
                        .defaultsTo(new File("spigot.yml"))
                        .describedAs("Yml file");
                // Spigot End
                acceptsAll(asList("P", "paper-settings"), "File for paper settings")
                .withRequiredArg()
                .ofType(File.class)
                .defaultsTo(new File("paper.yml"))
                .describedAs("Yml file");
            }
        };
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
                // Paper start - Log Java and OS versioning to help with debugging plugin issues
                java.lang.management.RuntimeMXBean runtimeMX = java.lang.management.ManagementFactory.getRuntimeMXBean();
                java.lang.management.OperatingSystemMXBean osMX = java.lang.management.ManagementFactory.getOperatingSystemMXBean();
                if (runtimeMX != null && osMX != null) {
                    String javaInfo = "Java " + runtimeMX.getSpecVersion() + " (" + runtimeMX.getVmName() + " " + runtimeMX.getVmVersion() + ")";
                    String osInfo = "Host:  " + osMX.getName() + " " + osMX.getVersion() + " (" + osMX.getArch() + ")";

                    System.out.println("System Info: " + javaInfo + " " + osInfo);
                } else {
                    System.out.println("Unable to read system info");
                }
                // Paper end
                
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

