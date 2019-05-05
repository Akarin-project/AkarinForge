/*
 * Decompiled with CFR 0_119.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.Logger
 */
package io.akarin.forge.threads;

import java.util.Timer;
import java.util.TimerTask;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Logger;

import io.akarin.forge.CatServer;

public class WatchCatThread
extends TimerTask {
    private static Timer timer = new Timer();
    private static long lastTime = 0;
    private static long lastWarnTime = 0;

    @Override
    public void run() {
        long curTime = System.currentTimeMillis();
        if (lastTime > 0 && curTime - lastTime > 2000 && curTime - lastWarnTime > 30000) {
            lastWarnTime = curTime;
            FMLLog.log.debug("------------------------------");
            FMLLog.log.debug("[Cat\u4fa6\u6d4b\u7cfb\u7edf]\u670d\u52a1\u5668\u4e3b\u7ebf\u7a0b\u5df2\u9677\u5165\u505c\u987f" + (curTime - lastTime) + "ms! \u4f60\u7684\u670d\u52a1\u5668\u5361\u987f\u4e86!");
            FMLLog.log.debug("\u5f53\u524d\u4e3b\u7ebf\u7a0b\u5806\u6808\u8ffd\u8e2a:");
            for (StackTraceElement stack : MinecraftServer.getServerInst().primaryThread.getStackTrace()) {
                FMLLog.log.debug("\t\t" + stack);
            }
            FMLLog.log.debug("--------------\u8bf7\u6ce8\u610f,\u8fd9\u4e0d\u662f\u62a5\u9519!\u8bf7\u52ff\u53cd\u9988!\u53ef\u5728catserver.yml\u4e2dcheck.threadLag\u5173\u95ed----------------");
        }
    }

    public static void update() {
        lastTime = System.currentTimeMillis();
    }

    public static void startThread() {
        if (CatServer.threadLag) {
            timer.schedule((TimerTask)new WatchCatThread(), 30000, 500);
        }
    }

    public static void stopThread() {
        timer.cancel();
    }
}

