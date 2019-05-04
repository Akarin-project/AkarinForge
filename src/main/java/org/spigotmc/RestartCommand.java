/*
 * Akarin Forge
 */
package org.spigotmc;

import java.io.File;
import java.io.PrintStream;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.spigotmc.AsyncCatcher;
import org.spigotmc.SpigotConfig;
import org.spigotmc.WatchdogThread;

public class RestartCommand
extends Command {
    public RestartCommand(String name) {
        super(name);
        this.description = "Restarts the server";
        this.usageMessage = "/restart";
        this.setPermission("bukkit.command.restart");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (this.testPermission(sender)) {
            MinecraftServer.getServerInst().processQueue.add(()new Runnable(){

                @Override
                public void run() {
                    RestartCommand.restart();
                }
            });
        }
        return true;
    }

    public static void restart() {
        RestartCommand.restart(new File(SpigotConfig.restartScript));
    }

    public static void restart(final File script) {
        AsyncCatcher.enabled = false;
        try {
            if (script.isFile()) {
                System.out.println("Attempting to restart with " + SpigotConfig.restartScript);
                WatchdogThread.doStop();
                for (oq p2 : MinecraftServer.getServerInst().am().i) {
                    p2.a.disconnect(SpigotConfig.restartMessage);
                }
                try {
                    Thread.sleep(100);
                }
                catch (InterruptedException interruptedException) {
                    // empty catch block
                }
                MinecraftServer.getServerInst().an().b();
                try {
                    Thread.sleep(100);
                }
                catch (InterruptedException interruptedException) {
                    // empty catch block
                }
                try {
                    MinecraftServer.getServerInst().u();
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
                Thread shutdownHook = new Thread(){

                    @Override
                    public void run() {
                        try {
                            String os2 = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
                            if (os2.contains("win")) {
                                Runtime.getRuntime().exec("cmd /c start " + script.getPath());
                            } else {
                                Runtime.getRuntime().exec(new String[]{"sh", script.getPath()});
                            }
                        }
                        catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                };
                shutdownHook.setDaemon(true);
                Runtime.getRuntime().addShutdownHook(shutdownHook);
            } else {
                System.out.println("Startup script '" + SpigotConfig.restartScript + "' does not exist! Stopping server.");
                try {
                    MinecraftServer.getServerInst().u();
                }
                catch (Throwable shutdownHook) {
                    // empty catch block
                }
            }
            FMLCommonHandler.instance().exitJava(0, false);
        }
        catch (Exception ex2) {
            ex2.printStackTrace();
        }
    }

}

