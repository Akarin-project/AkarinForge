/*
 * Akarin Forge
 */
package org.bukkit;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.BlockChangeDelegate;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Difficulty;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.TreeType;
import org.bukkit.WorldBorder;
import org.bukkit.WorldType;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.Metadatable;
import org.bukkit.plugin.messaging.PluginMessageRecipient;
import org.bukkit.util.Consumer;
import org.bukkit.util.Vector;

public interface World
extends PluginMessageRecipient,
Metadatable {
    public Block getBlockAt(int var1, int var2, int var3);

    public Block getBlockAt(Location var1);

    @Deprecated
    public int getBlockTypeIdAt(int var1, int var2, int var3);

    @Deprecated
    public int getBlockTypeIdAt(Location var1);

    public int getHighestBlockYAt(int var1, int var2);

    public int getHighestBlockYAt(Location var1);

    public Block getHighestBlockAt(int var1, int var2);

    public Block getHighestBlockAt(Location var1);

    public Chunk getChunkAt(int var1, int var2);

    public Chunk getChunkAt(Location var1);

    public Chunk getChunkAt(Block var1);

    public boolean isChunkLoaded(Chunk var1);

    public Chunk[] getLoadedChunks();

    public void loadChunk(Chunk var1);

    public boolean isChunkLoaded(int var1, int var2);

    public boolean isChunkInUse(int var1, int var2);

    public void loadChunk(int var1, int var2);

    public boolean loadChunk(int var1, int var2, boolean var3);

    public boolean unloadChunk(Chunk var1);

    public boolean unloadChunk(int var1, int var2);

    public boolean unloadChunk(int var1, int var2, boolean var3);

    @Deprecated
    public boolean unloadChunk(int var1, int var2, boolean var3, boolean var4);

    public boolean unloadChunkRequest(int var1, int var2);

    public boolean unloadChunkRequest(int var1, int var2, boolean var3);

    public boolean regenerateChunk(int var1, int var2);

    @Deprecated
    public boolean refreshChunk(int var1, int var2);

    public Item dropItem(Location var1, ItemStack var2);

    public Item dropItemNaturally(Location var1, ItemStack var2);

    public Arrow spawnArrow(Location var1, Vector var2, float var3, float var4);

    public <T extends Arrow> T spawnArrow(Location var1, Vector var2, float var3, float var4, Class<T> var5);

    public boolean generateTree(Location var1, TreeType var2);

    @Deprecated
    public boolean generateTree(Location var1, TreeType var2, BlockChangeDelegate var3);

    public Entity spawnEntity(Location var1, EntityType var2);

    public LightningStrike strikeLightning(Location var1);

    public LightningStrike strikeLightningEffect(Location var1);

    public List<Entity> getEntities();

    public List<LivingEntity> getLivingEntities();

    @Deprecated
    public /* varargs */ <T extends Entity> Collection<T> getEntitiesByClass(Class<T> ... var1);

    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T> var1);

    public /* varargs */ Collection<Entity> getEntitiesByClasses(Class<?> ... var1);

    public List<Player> getPlayers();

    public Collection<Entity> getNearbyEntities(Location var1, double var2, double var4, double var6);

    public String getName();

    public UUID getUID();

    public Location getSpawnLocation();

    public boolean setSpawnLocation(Location var1);

    public boolean setSpawnLocation(int var1, int var2, int var3);

    public long getTime();

    public void setTime(long var1);

    public long getFullTime();

    public void setFullTime(long var1);

    public boolean hasStorm();

    public void setStorm(boolean var1);

    public int getWeatherDuration();

    public void setWeatherDuration(int var1);

    public boolean isThundering();

    public void setThundering(boolean var1);

    public int getThunderDuration();

    public void setThunderDuration(int var1);

    public boolean createExplosion(double var1, double var3, double var5, float var7);

    public boolean createExplosion(double var1, double var3, double var5, float var7, boolean var8);

    public boolean createExplosion(double var1, double var3, double var5, float var7, boolean var8, boolean var9);

    public boolean createExplosion(Location var1, float var2);

    public boolean createExplosion(Location var1, float var2, boolean var3);

    public Environment getEnvironment();

    public long getSeed();

    public boolean getPVP();

    public void setPVP(boolean var1);

    public ChunkGenerator getGenerator();

    public void save();

    public List<BlockPopulator> getPopulators();

    public <T extends Entity> T spawn(Location var1, Class<T> var2) throws IllegalArgumentException;

    public <T extends Entity> T spawn(Location var1, Class<T> var2, Consumer<T> var3) throws IllegalArgumentException;

    public FallingBlock spawnFallingBlock(Location var1, MaterialData var2) throws IllegalArgumentException;

    @Deprecated
    public FallingBlock spawnFallingBlock(Location var1, Material var2, byte var3) throws IllegalArgumentException;

    @Deprecated
    public FallingBlock spawnFallingBlock(Location var1, int var2, byte var3) throws IllegalArgumentException;

    public void playEffect(Location var1, Effect var2, int var3);

    public void playEffect(Location var1, Effect var2, int var3, int var4);

    public <T> void playEffect(Location var1, Effect var2, T var3);

    public <T> void playEffect(Location var1, Effect var2, T var3, int var4);

    public ChunkSnapshot getEmptyChunkSnapshot(int var1, int var2, boolean var3, boolean var4);

    public void setSpawnFlags(boolean var1, boolean var2);

    public boolean getAllowAnimals();

    public boolean getAllowMonsters();

    public Biome getBiome(int var1, int var2);

    public void setBiome(int var1, int var2, Biome var3);

    public double getTemperature(int var1, int var2);

    public double getHumidity(int var1, int var2);

    public int getMaxHeight();

    public int getSeaLevel();

    public boolean getKeepSpawnInMemory();

    public void setKeepSpawnInMemory(boolean var1);

    public boolean isAutoSave();

    public void setAutoSave(boolean var1);

    public void setDifficulty(Difficulty var1);

    public Difficulty getDifficulty();

    public File getWorldFolder();

    public WorldType getWorldType();

    public boolean canGenerateStructures();

    public long getTicksPerAnimalSpawns();

    public void setTicksPerAnimalSpawns(int var1);

    public long getTicksPerMonsterSpawns();

    public void setTicksPerMonsterSpawns(int var1);

    public int getMonsterSpawnLimit();

    public void setMonsterSpawnLimit(int var1);

    public int getAnimalSpawnLimit();

    public void setAnimalSpawnLimit(int var1);

    public int getWaterAnimalSpawnLimit();

    public void setWaterAnimalSpawnLimit(int var1);

    public int getAmbientSpawnLimit();

    public void setAmbientSpawnLimit(int var1);

    public void playSound(Location var1, Sound var2, float var3, float var4);

    public void playSound(Location var1, String var2, float var3, float var4);

    public void playSound(Location var1, Sound var2, SoundCategory var3, float var4, float var5);

    public void playSound(Location var1, String var2, SoundCategory var3, float var4, float var5);

    public String[] getGameRules();

    public String getGameRuleValue(String var1);

    public boolean setGameRuleValue(String var1, String var2);

    public boolean isGameRule(String var1);

    public WorldBorder getWorldBorder();

    public void spawnParticle(Particle var1, Location var2, int var3);

    public void spawnParticle(Particle var1, double var2, double var4, double var6, int var8);

    public <T> void spawnParticle(Particle var1, Location var2, int var3, T var4);

    public <T> void spawnParticle(Particle var1, double var2, double var4, double var6, int var8, T var9);

    public void spawnParticle(Particle var1, Location var2, int var3, double var4, double var6, double var8);

    public void spawnParticle(Particle var1, double var2, double var4, double var6, int var8, double var9, double var11, double var13);

    public <T> void spawnParticle(Particle var1, Location var2, int var3, double var4, double var6, double var8, T var10);

    public <T> void spawnParticle(Particle var1, double var2, double var4, double var6, int var8, double var9, double var11, double var13, T var15);

    public void spawnParticle(Particle var1, Location var2, int var3, double var4, double var6, double var8, double var10);

    public void spawnParticle(Particle var1, double var2, double var4, double var6, int var8, double var9, double var11, double var13, double var15);

    public <T> void spawnParticle(Particle var1, Location var2, int var3, double var4, double var6, double var8, double var10, T var12);

    public <T> void spawnParticle(Particle var1, double var2, double var4, double var6, int var8, double var9, double var11, double var13, double var15, T var17);

    public Spigot spigot();

    public static enum Environment {
        NORMAL(0),
        NETHER(-1),
        THE_END(1);
        
        private final int id;
        private static final Map<Integer, Environment> lookup;

        private Environment(int id2) {
            this.id = id2;
        }

        public static void registerEnvironment(Environment env) {
            lookup.put(env.getId(), env);
        }

        public int getId() {
            return this.id;
        }

        public static Environment getEnvironment(int id) {
        	return lookup.get(id);
        }

        static {
            lookup = new HashMap<Integer, Environment>();
            for (Environment env : Environment.values()) {
                lookup.put(env.getId(), env);
            }
        }
    }

    public static class Spigot {
        @Deprecated
        public void playEffect(Location location, Effect effect) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Deprecated
        public void playEffect(Location location, Effect effect, int id2, int data, float offsetX, float offsetY, float offsetZ, float speed, int particleCount, int radius) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public LightningStrike strikeLightning(Location loc, boolean isSilent) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public LightningStrike strikeLightningEffect(Location loc, boolean isSilent) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

}

