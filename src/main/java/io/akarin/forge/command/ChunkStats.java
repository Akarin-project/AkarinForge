package io.akarin.forge.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import io.akarin.forge.AkarinForge;
import io.akarin.forge.utils.ChunkTime;

public class ChunkStats extends Command {
    private static Map<Chunk, Long> chunks = new HashMap<Chunk, Long>();

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
            chunks = new HashMap<>();
            AkarinForge.chunkStats = true;
            sender.sendMessage("Chunk stats started.");
            return true;
        }
        if (args[0].equals("stop")) {
            sender.sendMessage("Checking... Please see console");
            ArrayList<ChunkTime> chunkList = new ArrayList<ChunkTime>();
            for (int i2 = 0; i2 < 5; ++i2) {
                Chunk hight = null;
                long t2 = 0;
                for (Chunk chunk : chunks.keySet()) {
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
                Chunk chunk = chunkTime.chunk;
                FMLLog.log.info("World:{} X:{} Z:{}, has run time: {} ns", (Object)chunk.getWorld().worldInfo.getWorldName(), (Object)(chunk.x << 4), (Object)(chunk.z << 4), (Object)chunkTime.time);
            }
            return true;
        }
        return false;
    }

    public static void addTime(Chunk chunk, long time) {
        if (!AkarinForge.chunkStats) {
            return;
        }
        Long oldTime = chunks.get(chunk);
        if (oldTime == null) {
            oldTime = 0L;
        }
        oldTime = oldTime + time;
        chunks.put(chunk, oldTime);
    }
}

