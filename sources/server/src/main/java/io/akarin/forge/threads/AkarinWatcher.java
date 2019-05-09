package io.akarin.forge.threads;

import java.util.Timer;
import java.util.TimerTask;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLLog;
import io.akarin.forge.AkarinForge;

public class AkarinWatcher
extends TimerTask {
    private static Timer timer = new Timer();
    private static volatile long lastTime = 0;
    private static long lastWarnTime = 0;

    @Override
    public void run() {
        long curTime = System.currentTimeMillis();
        if (lastTime > 0 && curTime - lastTime > 2000 && curTime - lastWarnTime > 30000) {
            lastWarnTime = curTime;
            FMLLog.log.debug("------------------------------");
            FMLLog.log.debug("[Akarin Forge] Your server has stuck for " + (curTime - lastTime) + "ms!");
            FMLLog.log.debug("This is the current stack trace form main thread:");
            for (StackTraceElement stack : MinecraftServer.getServerInst().primaryThread.getStackTrace()) {
                FMLLog.log.debug("\t\t" + stack);
            }
            FMLLog.log.debug("-------------- Do NOT report this to AkarinForge, this is not a bug or error! ----------------");
        }
    }

    public static void update() {
        lastTime = System.currentTimeMillis();
    }

    public static void startThread() {
        if (AkarinForge.threadLag) {
            timer.schedule((TimerTask)new AkarinWatcher(), 30000, 500);
        }
    }

    public static void stopThread() {
        timer.cancel();
    }
}

