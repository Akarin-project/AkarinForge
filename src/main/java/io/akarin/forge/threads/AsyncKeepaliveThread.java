/*
 * Decompiled with CFR 0_119.
 */
package io.akarin.forge.threads;

import java.util.List;
import net.minecraft.server.MinecraftServer;

public class AsyncKeepaliveThread
extends Thread {
    private static AsyncKeepaliveThread thread;

    @Override
    public void run() {
        do {
            try {
                do {
                    for (oq player : MinecraftServer.getServerInst().am().i) {
                        if (player.a == null) continue;
                        player.a.asyncKeepalive();
                    }
                    Thread.sleep(100);
                } while (true);
            }
            catch (Exception e2) {
                e2.printStackTrace();
                continue;
            }
            break;
        } while (true);
    }

    public static void startThread() {
        thread = new AsyncKeepaliveThread();
        thread.start();
    }

    public static void stopThread() {
        if (thread != null) {
            thread.stop();
        }
    }
}

