package io.akarin.forge.server;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.generator.ChunkGenerator;
import org.spigotmc.SpigotWorldConfig;
import io.akarin.forge.WorldCapture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

public abstract class AkarinWorld {
	protected CraftWorld world;
    public boolean pvpMode;
    public ChunkGenerator generator;
    public boolean captureTreeGeneration = false;
    public List<ItemStack> captureDrops;
    public long ticksPerAnimalSpawns;
    public long ticksPerMonsterSpawns;
    public boolean populating;
    
    public SpigotWorldConfig spigotConfig;
    public static boolean haveWeSilencedAPhysicsCrash;
    public static String blockLocation;

    public CraftWorld getWorld() {
        return this.world;
    }

    public CraftServer getServer() {
        return (CraftServer) Bukkit.getServer();
    }

    public abstract Chunk getChunkIfLoaded(BlockPos blockposition);
    
    public abstract Chunk getChunkIfLoaded(int x, int z);
    
    public abstract void checkSleepStatus();
}
