/*
 * Akarin Forge
 */
package org.spigotmc;

import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import org.bukkit.Bukkit;
import org.spigotmc.RestartCommand;

public class WatchdogThread
extends Thread {
    private static WatchdogThread instance;
    private final long timeoutTime;
    private final boolean restart;
    private volatile long lastTick;
    private volatile boolean stopping;

    private WatchdogThread(long timeoutTime, boolean restart) {
        super("Spigot Watchdog Thread");
        this.timeoutTime = timeoutTime;
        this.restart = restart;
    }

    public static void doStart(int timeoutTime, boolean restart) {
        if (instance == null) {
            instance = new WatchdogThread((long)timeoutTime * 1000, restart);
            instance.start();
        }
    }

    public static void tick() {
        WatchdogThread.instance.lastTick = System.currentTimeMillis();
    }

    public static void doStop() {
        if (instance != null) {
            WatchdogThread.instance.stopping = true;
        }
    }

    @Override
    public void run() {
        while (!this.stopping) {
            if (this.lastTick != 0 && System.currentTimeMillis() > this.lastTick + this.timeoutTime) {
                ThreadInfo[] threads;
                Logger log = Bukkit.getServer().getLogger();
                log.log(Level.SEVERE, "The server has stopped responding!");
                log.log(Level.SEVERE, "Please report this to http://www.spigotmc.org/");
                log.log(Level.SEVERE, "Be sure to include ALL relevant console errors and Minecraft crash reports");
                log.log(Level.SEVERE, "Spigot version: " + Bukkit.getServer().getVersion());
                if (World.haveWeSilencedAPhysicsCrash) {
                    log.log(Level.SEVERE, "------------------------------");
                    log.log(Level.SEVERE, "During the run of the server, a physics stackoverflow was supressed");
                    log.log(Level.SEVERE, "near " + World.blockLocation);
                }
                log.log(Level.SEVERE, "------------------------------");
                log.log(Level.SEVERE, "Server thread dump (Look for plugins here before reporting to Spigot!):");
                WatchdogThread.dumpThread(ManagementFactory.getThreadMXBean().getThreadInfo(MinecraftServer.getServerInst().primaryThread.getId(), Integer.MAX_VALUE), log);
                log.log(Level.SEVERE, "------------------------------");
                log.log(Level.SEVERE, "Entire Thread Dump:");
                for (ThreadInfo thread : threads = ManagementFactory.getThreadMXBean().dumpAllThreads(true, true)) {
                    WatchdogThread.dumpThread(thread, log);
                }
                log.log(Level.SEVERE, "------------------------------");
                if (!this.restart) break;
                MinecraftServer.getServerInst().primaryThread.suspend();
                RestartCommand.restart();
                break;
            }
            try {
                WatchdogThread.sleep(10000);
            }
            catch (InterruptedException ex2) {
                this.interrupt();
            }
        }
    }

    private static void dumpThread(ThreadInfo thread, Logger log) {
        log.log(Level.SEVERE, "------------------------------");
        log.log(Level.SEVERE, "Current Thread: " + thread.getThreadName());
        log.log(Level.SEVERE, "\tPID: " + thread.getThreadId() + " | Suspended: " + thread.isSuspended() + " | Native: " + thread.isInNative() + " | State: " + (Object)((Object)thread.getThreadState()));
        if (thread.getLockedMonitors().length != 0) {
            log.log(Level.SEVERE, "\tThread is waiting on monitor(s):");
            for (MonitorInfo monitor : thread.getLockedMonitors()) {
                log.log(Level.SEVERE, "\t\tLocked on:" + monitor.getLockedStackFrame());
            }
        }
        log.log(Level.SEVERE, "\tStack:");
        for (StackTraceElement stack : thread.getStackTrace()) {
            log.log(Level.SEVERE, "\t\t" + stack);
        }
    }
}

