package org.bukkit.craftbukkit.v1_12_R1;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Set;
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
import org.bukkit.World;
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
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Consumer;
import org.bukkit.util.Vector;

import net.minecraft.world.WorldServer;

/*
 * Temp class dummy impl
 */
public class CraftWorld implements World {

    private final WorldServer world;
	
    public WorldServer getHandle() {
        return this.world;
    }
	
	public CraftWorld(WorldServer worldServer, ChunkGenerator gen, Environment env) {
        this.world = worldServer;
	}

	@Override
	public void sendPluginMessage(Plugin source, String channel, byte[] message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<String> getListeningPluginChannels() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<MetadataValue> getMetadata(String metadataKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasMetadata(String metadataKey) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeMetadata(String metadataKey, Plugin owningPlugin) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Block getBlockAt(int x, int y, int z) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Block getBlockAt(Location location) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getBlockTypeIdAt(int x, int y, int z) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBlockTypeIdAt(Location location) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHighestBlockYAt(int x, int z) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHighestBlockYAt(Location location) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Block getHighestBlockAt(int x, int z) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Block getHighestBlockAt(Location location) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Chunk getChunkAt(int x, int z) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Chunk getChunkAt(Location location) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Chunk getChunkAt(Block block) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isChunkLoaded(Chunk chunk) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Chunk[] getLoadedChunks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void loadChunk(Chunk chunk) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isChunkLoaded(int x, int z) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChunkInUse(int x, int z) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void loadChunk(int x, int z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean loadChunk(int x, int z, boolean generate) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean unloadChunk(Chunk chunk) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean unloadChunk(int x, int z) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean unloadChunk(int x, int z, boolean save) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean unloadChunk(int x, int z, boolean save, boolean safe) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean unloadChunkRequest(int x, int z) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean unloadChunkRequest(int x, int z, boolean safe) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean regenerateChunk(int x, int z) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean refreshChunk(int x, int z) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Item dropItem(Location location, ItemStack item) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Item dropItemNaturally(Location location, ItemStack item) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Arrow spawnArrow(Location location, Vector direction, float speed, float spread) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Arrow> T spawnArrow(Location location, Vector direction, float speed, float spread,
			Class<T> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean generateTree(Location location, TreeType type) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean generateTree(Location loc, TreeType type, BlockChangeDelegate delegate) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Entity spawnEntity(Location loc, EntityType type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LightningStrike strikeLightning(Location loc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LightningStrike strikeLightningEffect(Location loc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Entity> getEntities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LivingEntity> getLivingEntities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Entity> Collection<T> getEntitiesByClass(Class<T>... classes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Entity> Collection<T> getEntitiesByClass(Class<T> cls) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Entity> getEntitiesByClasses(Class<?>... classes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Player> getPlayers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Entity> getNearbyEntities(Location location, double x, double y, double z) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UUID getUID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Location getSpawnLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean setSpawnLocation(Location location) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setSpawnLocation(int x, int y, int z) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long getTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setTime(long time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getFullTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setFullTime(long time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasStorm() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setStorm(boolean hasStorm) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getWeatherDuration() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setWeatherDuration(int duration) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isThundering() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setThundering(boolean thundering) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getThunderDuration() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setThunderDuration(int duration) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean createExplosion(double x, double y, double z, float power) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createExplosion(double x, double y, double z, float power, boolean setFire) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createExplosion(Location loc, float power) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createExplosion(Location loc, float power, boolean setFire) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Environment getEnvironment() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getSeed() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean getPVP() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setPVP(boolean pvp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ChunkGenerator getGenerator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<BlockPopulator> getPopulators() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Entity> T spawn(Location location, Class<T> clazz) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Entity> T spawn(Location location, Class<T> clazz, Consumer<T> function)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FallingBlock spawnFallingBlock(Location location, MaterialData data) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FallingBlock spawnFallingBlock(Location location, Material material, byte data)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FallingBlock spawnFallingBlock(Location location, int blockId, byte blockData)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void playEffect(Location location, Effect effect, int data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playEffect(Location location, Effect effect, int data, int radius) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void playEffect(Location location, Effect effect, T data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void playEffect(Location location, Effect effect, T data, int radius) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ChunkSnapshot getEmptyChunkSnapshot(int x, int z, boolean includeBiome, boolean includeBiomeTempRain) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSpawnFlags(boolean allowMonsters, boolean allowAnimals) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getAllowAnimals() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getAllowMonsters() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Biome getBiome(int x, int z) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBiome(int x, int z, Biome bio) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getTemperature(int x, int z) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getHumidity(int x, int z) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSeaLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean getKeepSpawnInMemory() {
		return true;
	}

	@Override
	public void setKeepSpawnInMemory(boolean keepLoaded) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isAutoSave() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setAutoSave(boolean value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDifficulty(Difficulty difficulty) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Difficulty getDifficulty() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getWorldFolder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WorldType getWorldType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canGenerateStructures() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long getTicksPerAnimalSpawns() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setTicksPerAnimalSpawns(int ticksPerAnimalSpawns) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getTicksPerMonsterSpawns() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setTicksPerMonsterSpawns(int ticksPerMonsterSpawns) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getMonsterSpawnLimit() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMonsterSpawnLimit(int limit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getAnimalSpawnLimit() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setAnimalSpawnLimit(int limit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getWaterAnimalSpawnLimit() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setWaterAnimalSpawnLimit(int limit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getAmbientSpawnLimit() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setAmbientSpawnLimit(int limit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playSound(Location location, Sound sound, float volume, float pitch) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playSound(Location location, String sound, float volume, float pitch) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playSound(Location location, Sound sound, SoundCategory category, float volume, float pitch) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playSound(Location location, String sound, SoundCategory category, float volume, float pitch) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String[] getGameRules() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getGameRuleValue(String rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean setGameRuleValue(String rule, String value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isGameRule(String rule) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public WorldBorder getWorldBorder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void spawnParticle(Particle particle, Location location, int count) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void spawnParticle(Particle particle, double x, double y, double z, int count) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void spawnParticle(Particle particle, Location location, int count, T data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, T data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY,
			double offsetZ) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX,
			double offsetY, double offsetZ) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY,
			double offsetZ, T data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX,
			double offsetY, double offsetZ, T data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY,
			double offsetZ, double extra) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX,
			double offsetY, double offsetZ, double extra) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY,
			double offsetZ, double extra, T data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX,
			double offsetY, double offsetZ, double extra, T data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Spigot spigot() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getBlockMetadata() {
		// TODO Auto-generated method stub
		return null;
	}
    
}
