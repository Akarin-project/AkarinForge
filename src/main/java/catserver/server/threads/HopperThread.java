/*
 * Decompiled with CFR 0_119.
 */
package catserver.server.threads;

import catserver.server.utils.HopperTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;

public class HopperThread
extends Thread {
    private final oo world;
    private final LinkedBlockingQueue<HopperTask> queue;

    public HopperThread(oo worldServer, LinkedBlockingQueue<HopperTask> queue) {
        this.world = worldServer;
        this.queue = queue;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void run() {
        long nowTime = System.currentTimeMillis();
        int count = 0;
        while (this.world != null) {
            try {
                int n2 = count;
                count = (short)(count + 1);
                if (n2 % 10 == 0) {
                    nowTime = System.currentTimeMillis();
                    count = 0;
                }
                HopperTask hopperTask = this.queue.take();
                avw hopper = hopperTask.hopper;
                if (nowTime - hopperTask.time > 200 || !this.world.e(hopper.w()) || this.world.G) continue;
                --hopper.f;
                hopper.g = this.world.R();
                if (hopper.J()) continue;
                hopper.d(0);
                hopper.lock.lock();
                try {
                    hopper.o();
                    continue;
                }
                finally {
                    hopper.lock.unlock();
                }
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}

