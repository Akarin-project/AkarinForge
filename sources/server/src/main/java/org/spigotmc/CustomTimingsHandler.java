/*
 * Akarin Forge
 */
package org.spigotmc;

import java.io.PrintStream;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.defaults.TimingsCommand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class CustomTimingsHandler {
    private static Queue<CustomTimingsHandler> HANDLERS = new ConcurrentLinkedQueue<CustomTimingsHandler>();
    private final String name;
    private final CustomTimingsHandler parent;
    private long count = 0;
    private long start = 0;
    private long timingDepth = 0;
    private long totalTime = 0;
    private long curTickTotal = 0;
    private long violations = 0;

    public CustomTimingsHandler(String name) {
        this(name, null);
    }

    public CustomTimingsHandler(String name, CustomTimingsHandler parent) {
        this.name = name;
        this.parent = parent;
        HANDLERS.add(this);
    }

    public static void printTimings(PrintStream printStream) {
        printStream.println("Minecraft");
        for (CustomTimingsHandler timings : HANDLERS) {
            long time = timings.totalTime;
            long count = timings.count;
            if (count == 0) continue;
            long avg2 = time / count;
            printStream.println("    " + timings.name + " Time: " + time + " Count: " + count + " Avg: " + avg2 + " Violations: " + timings.violations);
        }
        printStream.println("# Version " + Bukkit.getVersion());
        int entities = 0;
        int livingEntities = 0;
        for (World world : Bukkit.getWorlds()) {
            entities += world.getEntities().size();
            livingEntities += world.getLivingEntities().size();
        }
        printStream.println("# Entities " + entities);
        printStream.println("# LivingEntities " + livingEntities);
    }

    public static void reload() {
        if (Bukkit.getPluginManager().useTimings()) {
            for (CustomTimingsHandler timings : HANDLERS) {
                timings.reset();
            }
        }
        TimingsCommand.timingStart = System.nanoTime();
    }

    public static void tick() {
        if (Bukkit.getPluginManager().useTimings()) {
            for (CustomTimingsHandler timings : HANDLERS) {
                if (timings.curTickTotal > 50000000) {
                    timings.violations = (long)((double)timings.violations + Math.ceil(timings.curTickTotal / 50000000));
                }
                timings.curTickTotal = 0;
                timings.timingDepth = 0;
            }
        }
    }

    public void startTiming() {
        if (Bukkit.getPluginManager().useTimings() && ++this.timingDepth == 1) {
            this.start = System.nanoTime();
            if (this.parent != null && ++this.parent.timingDepth == 1) {
                this.parent.start = this.start;
            }
        }
    }

    public void stopTiming() {
        if (Bukkit.getPluginManager().useTimings()) {
            if (--this.timingDepth != 0 || this.start == 0) {
                return;
            }
            long diff = System.nanoTime() - this.start;
            this.totalTime += diff;
            this.curTickTotal += diff;
            ++this.count;
            this.start = 0;
            if (this.parent != null) {
                this.parent.stopTiming();
            }
        }
    }

    public void reset() {
        this.count = 0;
        this.violations = 0;
        this.curTickTotal = 0;
        this.totalTime = 0;
        this.start = 0;
        this.timingDepth = 0;
    }
}

