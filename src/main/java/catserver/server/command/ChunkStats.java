/*
 * Decompiled with CFR 0_119.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.Logger
 */
package catserver.server.command;

import catserver.server.CatServer;
import catserver.server.utils.ChunkTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ChunkStats
extends Command {
    private static Map<axw, Long> chunks = new HashMap<axw, Long>();

    public ChunkStats(String name) {
        super(name);
        this.description = "Chunk Stats Command";
        this.usageMessage = "/chunkstats start/stop";
        this.setPermission("catserver.command.chunkstats");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        if (args.length != 1) {
            return false;
        }
        if (args[0].equals("start")) {
            chunks = new HashMap<axw, Long>();
            CatServer.chunkStats = true;
            sender.sendMessage("Chunk stats started.");
            return true;
        }
        if (args[0].equals("stop")) {
            sender.sendMessage("Checking... Please see console");
            ArrayList<ChunkTime> chunkList = new ArrayList<ChunkTime>();
            for (int i2 = 0; i2 < 5; ++i2) {
                axw hight = null;
                long t2 = 0;
                for (axw chunk : chunks.keySet()) {
                    if (hight == null) {
                        hight = chunk;
                        t2 = chunks.get(chunk);
                        continue;
                    }
                    long tt2 = chunks.get(chunk);
                    if (tt2 <= t2) continue;
                    hight = chunk;
                    t2 = tt2;
                }
                chunkList.add(new ChunkTime(hight, t2));
                chunks.remove(hight);
            }
            FMLLog.log.info("Chunks Time:");
            for (ChunkTime chunkTime : chunkList) {
                axw chunk = chunkTime.chunk;
                FMLLog.log.info("World:{} X:{} Z:{}, has run time: {} ns", (Object)chunk.q().x.j(), (Object)(chunk.b << 4), (Object)(chunk.c << 4), (Object)chunkTime.time);
            }
            return true;
        }
        return false;
    }

    public static void addTime(axw chunk, long time) {
        if (!CatServer.chunkStats) {
            return;
        }
        Long oldTime = chunks.get(chunk);
        if (oldTime == null) {
            oldTime = 0;
        }
        oldTime = oldTime + time;
        chunks.put(chunk, oldTime);
    }
}

