package org.bukkit.craftbukkit.v1_12_R1.entity;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Achievement;
import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.GameMode;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.Statistic;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.advancement.Advancement;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Villager;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.InventoryView.Property;
import org.bukkit.inventory.MainHand;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

import net.minecraft.command.ICommandSender;

public class CraftPlayer implements Player {

	@Override
	public String getName() {

		return null;
	}

	@Override
	public PlayerInventory getInventory() {

		return null;
	}

	@Override
	public Inventory getEnderChest() {

		return null;
	}

	@Override
	public MainHand getMainHand() {

		return null;
	}

	@Override
	public boolean setWindowProperty(Property prop, int value) {

		return false;
	}

	@Override
	public InventoryView getOpenInventory() {

		return null;
	}

	@Override
	public InventoryView openInventory(Inventory inventory) {

		return null;
	}

	@Override
	public InventoryView openWorkbench(Location location, boolean force) {

		return null;
	}

	@Override
	public InventoryView openEnchanting(Location location, boolean force) {

		return null;
	}

	@Override
	public void openInventory(InventoryView inventory) {

		
	}

	@Override
	public InventoryView openMerchant(Villager trader, boolean force) {

		return null;
	}

	@Override
	public InventoryView openMerchant(Merchant merchant, boolean force) {

		return null;
	}

	@Override
	public void closeInventory() {

		
	}

	@Override
	public org.bukkit.inventory.ItemStack getItemInHand() {

		return null;
	}

	@Override
	public void setItemInHand(org.bukkit.inventory.ItemStack item) {

		
	}

	@Override
	public org.bukkit.inventory.ItemStack getItemOnCursor() {

		return null;
	}

	@Override
	public void setItemOnCursor(org.bukkit.inventory.ItemStack item) {

		
	}

	@Override
	public boolean hasCooldown(Material material) {

		return false;
	}

	@Override
	public int getCooldown(Material material) {

		return 0;
	}

	@Override
	public void setCooldown(Material material, int ticks) {

		
	}

	@Override
	public boolean isSleeping() {

		return false;
	}

	@Override
	public int getSleepTicks() {

		return 0;
	}

	@Override
	public GameMode getGameMode() {

		return null;
	}

	@Override
	public void setGameMode(GameMode mode) {

		
	}

	@Override
	public boolean isBlocking() {

		return false;
	}

	@Override
	public boolean isHandRaised() {

		return false;
	}

	@Override
	public int getExpToLevel() {

		return 0;
	}

	@Override
	public org.bukkit.entity.Entity getShoulderEntityLeft() {

		return null;
	}

	@Override
	public void setShoulderEntityLeft(org.bukkit.entity.Entity entity) {

		
	}

	@Override
	public org.bukkit.entity.Entity getShoulderEntityRight() {

		return null;
	}

	@Override
	public void setShoulderEntityRight(org.bukkit.entity.Entity entity) {

		
	}

	@Override
	public double getEyeHeight() {

		return 0;
	}

	@Override
	public double getEyeHeight(boolean ignorePose) {

		return 0;
	}

	@Override
	public Location getEyeLocation() {

		return null;
	}

	@Override
	public List<Block> getLineOfSight(Set<Material> transparent, int maxDistance) {

		return null;
	}

	@Override
	public Block getTargetBlock(Set<Material> transparent, int maxDistance) {

		return null;
	}

	@Override
	public List<Block> getLastTwoTargetBlocks(Set<Material> transparent, int maxDistance) {

		return null;
	}

	@Override
	public int getRemainingAir() {

		return 0;
	}

	@Override
	public void setRemainingAir(int ticks) {

		
	}

	@Override
	public int getMaximumAir() {

		return 0;
	}

	@Override
	public void setMaximumAir(int ticks) {

		
	}

	@Override
	public int getMaximumNoDamageTicks() {

		return 0;
	}

	@Override
	public void setMaximumNoDamageTicks(int ticks) {

		
	}

	@Override
	public double getLastDamage() {

		return 0;
	}

	@Override
	public void setLastDamage(double damage) {

		
	}

	@Override
	public int getNoDamageTicks() {

		return 0;
	}

	@Override
	public void setNoDamageTicks(int ticks) {

		
	}

	@Override
	public Player getKiller() {

		return null;
	}

	@Override
	public boolean addPotionEffect(PotionEffect effect) {

		return false;
	}

	@Override
	public boolean addPotionEffect(PotionEffect effect, boolean force) {

		return false;
	}

	@Override
	public boolean addPotionEffects(Collection<PotionEffect> effects) {

		return false;
	}

	@Override
	public boolean hasPotionEffect(PotionEffectType type) {

		return false;
	}

	@Override
	public PotionEffect getPotionEffect(PotionEffectType type) {

		return null;
	}

	@Override
	public void removePotionEffect(PotionEffectType type) {

		
	}

	@Override
	public Collection<PotionEffect> getActivePotionEffects() {

		return null;
	}

	@Override
	public boolean hasLineOfSight(org.bukkit.entity.Entity other) {

		return false;
	}

	@Override
	public boolean getRemoveWhenFarAway() {

		return false;
	}

	@Override
	public void setRemoveWhenFarAway(boolean remove) {

		
	}

	@Override
	public EntityEquipment getEquipment() {

		return null;
	}

	@Override
	public void setCanPickupItems(boolean pickup) {

		
	}

	@Override
	public boolean getCanPickupItems() {

		return false;
	}

	@Override
	public boolean isLeashed() {

		return false;
	}

	@Override
	public org.bukkit.entity.Entity getLeashHolder() throws IllegalStateException {

		return null;
	}

	@Override
	public boolean setLeashHolder(org.bukkit.entity.Entity holder) {

		return false;
	}

	@Override
	public boolean isGliding() {

		return false;
	}

	@Override
	public void setGliding(boolean gliding) {

		
	}

	@Override
	public void setAI(boolean ai) {

		
	}

	@Override
	public boolean hasAI() {

		return false;
	}

	@Override
	public void setCollidable(boolean collidable) {

		
	}

	@Override
	public boolean isCollidable() {

		return false;
	}

	@Override
	public AttributeInstance getAttribute(Attribute attribute) {

		return null;
	}

	@Override
	public Location getLocation() {

		return null;
	}

	@Override
	public Location getLocation(Location loc) {

		return null;
	}

	@Override
	public void setVelocity(Vector velocity) {

		
	}

	@Override
	public Vector getVelocity() {

		return null;
	}

	@Override
	public double getHeight() {

		return 0;
	}

	@Override
	public double getWidth() {

		return 0;
	}

	@Override
	public boolean isOnGround() {

		return false;
	}

	@Override
	public World getWorld() {

		return null;
	}

	@Override
	public boolean teleport(Location location) {

		return false;
	}

	@Override
	public boolean teleport(Location location, TeleportCause cause) {

		return false;
	}

	@Override
	public boolean teleport(org.bukkit.entity.Entity destination) {

		return false;
	}

	@Override
	public boolean teleport(org.bukkit.entity.Entity destination, TeleportCause cause) {

		return false;
	}

	@Override
	public List<org.bukkit.entity.Entity> getNearbyEntities(double x, double y, double z) {

		return null;
	}

	@Override
	public int getEntityId() {

		return 0;
	}

	@Override
	public int getFireTicks() {

		return 0;
	}

	@Override
	public int getMaxFireTicks() {

		return 0;
	}

	@Override
	public void setFireTicks(int ticks) {

		
	}

	@Override
	public void remove() {

		
	}

	@Override
	public boolean isDead() {

		return false;
	}

	@Override
	public boolean isValid() {

		return false;
	}

	@Override
	public Server getServer() {

		return null;
	}

	@Override
	public org.bukkit.entity.Entity getPassenger() {

		return null;
	}

	@Override
	public boolean setPassenger(org.bukkit.entity.Entity passenger) {

		return false;
	}

	@Override
	public List<org.bukkit.entity.Entity> getPassengers() {

		return null;
	}

	@Override
	public boolean addPassenger(org.bukkit.entity.Entity passenger) {

		return false;
	}

	@Override
	public boolean removePassenger(org.bukkit.entity.Entity passenger) {

		return false;
	}

	@Override
	public boolean isEmpty() {

		return false;
	}

	@Override
	public boolean eject() {

		return false;
	}

	@Override
	public float getFallDistance() {

		return 0;
	}

	@Override
	public void setFallDistance(float distance) {

		
	}

	@Override
	public void setLastDamageCause(EntityDamageEvent event) {

		
	}

	@Override
	public EntityDamageEvent getLastDamageCause() {

		return null;
	}

	@Override
	public UUID getUniqueId() {

		return null;
	}

	@Override
	public int getTicksLived() {

		return 0;
	}

	@Override
	public void setTicksLived(int value) {

		
	}

	@Override
	public void playEffect(EntityEffect type) {

		
	}

	@Override
	public EntityType getType() {

		return null;
	}

	@Override
	public boolean isInsideVehicle() {

		return false;
	}

	@Override
	public boolean leaveVehicle() {

		return false;
	}

	@Override
	public org.bukkit.entity.Entity getVehicle() {

		return null;
	}

	@Override
	public void setCustomNameVisible(boolean flag) {

		
	}

	@Override
	public boolean isCustomNameVisible() {

		return false;
	}

	@Override
	public void setGlowing(boolean flag) {

		
	}

	@Override
	public boolean isGlowing() {

		return false;
	}

	@Override
	public void setInvulnerable(boolean flag) {

		
	}

	@Override
	public boolean isInvulnerable() {

		return false;
	}

	@Override
	public boolean isSilent() {

		return false;
	}

	@Override
	public void setSilent(boolean flag) {

		
	}

	@Override
	public boolean hasGravity() {

		return false;
	}

	@Override
	public void setGravity(boolean gravity) {

		
	}

	@Override
	public int getPortalCooldown() {

		return 0;
	}

	@Override
	public void setPortalCooldown(int cooldown) {

		
	}

	@Override
	public Set<String> getScoreboardTags() {

		return null;
	}

	@Override
	public boolean addScoreboardTag(String tag) {

		return false;
	}

	@Override
	public boolean removeScoreboardTag(String tag) {

		return false;
	}

	@Override
	public PistonMoveReaction getPistonMoveReaction() {

		return null;
	}

	@Override
	public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {

		
	}

	@Override
	public List<MetadataValue> getMetadata(String metadataKey) {

		return null;
	}

	@Override
	public boolean hasMetadata(String metadataKey) {

		return false;
	}

	@Override
	public void removeMetadata(String metadataKey, Plugin owningPlugin) {

		
	}

	@Override
	public void sendMessage(String var1) {

		
	}

	@Override
	public void sendMessage(String[] var1) {

		
	}

	@Override
	public Spigot spigot() {

		return null;
	}

	@Override
	public boolean isPermissionSet(String name) {

		return false;
	}

	@Override
	public boolean isPermissionSet(Permission perm) {

		return false;
	}

	@Override
	public boolean hasPermission(String name) {

		return false;
	}

	@Override
	public boolean hasPermission(Permission perm) {

		return false;
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {

		return null;
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin) {

		return null;
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {

		return null;
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, int ticks) {

		return null;
	}

	@Override
	public void removeAttachment(PermissionAttachment attachment) {

		
	}

	@Override
	public void recalculatePermissions() {

		
	}

	@Override
	public Set<PermissionAttachmentInfo> getEffectivePermissions() {

		return null;
	}

	@Override
	public boolean isOp() {

		return false;
	}

	@Override
	public void setOp(boolean value) {

		
	}

	@Override
	public String getCustomName() {

		return null;
	}

	@Override
	public void setCustomName(String name) {

		
	}

	@Override
	public void damage(double amount) {

		
	}

	@Override
	public void damage(double amount, org.bukkit.entity.Entity source) {

		
	}

	@Override
	public double getHealth() {

		return 0;
	}

	@Override
	public void setHealth(double health) {

		
	}

	@Override
	public double getMaxHealth() {

		return 0;
	}

	@Override
	public void setMaxHealth(double health) {

		
	}

	@Override
	public void resetMaxHealth() {

		
	}

	@Override
	public <T extends Projectile> T launchProjectile(Class<? extends T> projectile) {

		return null;
	}

	@Override
	public <T extends Projectile> T launchProjectile(Class<? extends T> projectile, Vector velocity) {

		return null;
	}

	@Override
	public boolean isConversing() {

		return false;
	}

	@Override
	public void acceptConversationInput(String input) {

		
	}

	@Override
	public boolean beginConversation(Conversation conversation) {

		return false;
	}

	@Override
	public void abandonConversation(Conversation conversation) {

		
	}

	@Override
	public void abandonConversation(Conversation conversation, ConversationAbandonedEvent details) {

		
	}

	@Override
	public boolean isOnline() {

		return false;
	}

	@Override
	public boolean isBanned() {

		return false;
	}

	@Override
	public boolean isWhitelisted() {

		return false;
	}

	@Override
	public void setWhitelisted(boolean value) {

		
	}

	@Override
	public Player getPlayer() {

		return null;
	}

	@Override
	public long getFirstPlayed() {

		return 0;
	}

	@Override
	public long getLastPlayed() {

		return 0;
	}

	@Override
	public boolean hasPlayedBefore() {

		return false;
	}

	@Override
	public Map<String, Object> serialize() {

		return null;
	}

	@Override
	public void sendPluginMessage(Plugin source, String channel, byte[] message) {

		
	}

	@Override
	public Set<String> getListeningPluginChannels() {

		return null;
	}

	@Override
	public String getDisplayName() {

		return null;
	}

	@Override
	public void setDisplayName(String name) {

		
	}

	@Override
	public String getPlayerListName() {

		return null;
	}

	@Override
	public void setPlayerListName(String name) {

		
	}

	@Override
	public void setCompassTarget(Location loc) {

		
	}

	@Override
	public Location getCompassTarget() {

		return null;
	}

	@Override
	public InetSocketAddress getAddress() {

		return null;
	}

	@Override
	public void sendRawMessage(String message) {

		
	}

	@Override
	public void kickPlayer(String message) {

		
	}

	@Override
	public void chat(String msg) {

		
	}

	@Override
	public boolean performCommand(String command) {

		return false;
	}

	@Override
	public boolean isSneaking() {

		return false;
	}

	@Override
	public void setSneaking(boolean sneak) {

		
	}

	@Override
	public boolean isSprinting() {

		return false;
	}

	@Override
	public void setSprinting(boolean sprinting) {

		
	}

	@Override
	public void saveData() {

		
	}

	@Override
	public void loadData() {

		
	}

	@Override
	public void setSleepingIgnored(boolean isSleeping) {

		
	}

	@Override
	public boolean isSleepingIgnored() {

		return false;
	}

	@Override
	public void playNote(Location loc, byte instrument, byte note) {

		
	}

	@Override
	public void playNote(Location loc, Instrument instrument, Note note) {

		
	}

	@Override
	public void playSound(Location location, Sound sound, float volume, float pitch) {

		
	}

	@Override
	public void playSound(Location location, String sound, float volume, float pitch) {

		
	}

	@Override
	public void playSound(Location location, Sound sound, SoundCategory category, float volume, float pitch) {

		
	}

	@Override
	public void playSound(Location location, String sound, SoundCategory category, float volume, float pitch) {

		
	}

	@Override
	public void stopSound(Sound sound) {

		
	}

	@Override
	public void stopSound(String sound) {

		
	}

	@Override
	public void stopSound(Sound sound, SoundCategory category) {

		
	}

	@Override
	public void stopSound(String sound, SoundCategory category) {

		
	}

	@Override
	public void playEffect(Location loc, Effect effect, int data) {

		
	}

	@Override
	public <T> void playEffect(Location loc, Effect effect, T data) {

		
	}

	@Override
	public void sendBlockChange(Location loc, Material material, byte data) {

		
	}

	@Override
	public boolean sendChunkChange(Location loc, int sx, int sy, int sz, byte[] data) {

		return false;
	}

	@Override
	public void sendBlockChange(Location loc, int material, byte data) {

		
	}

	@Override
	public void sendSignChange(Location loc, String[] lines) throws IllegalArgumentException {

		
	}

	@Override
	public void sendMap(MapView map) {

		
	}

	@Override
	public void updateInventory() {

		
	}

	@Override
	public void awardAchievement(Achievement achievement) {

		
	}

	@Override
	public void removeAchievement(Achievement achievement) {

		
	}

	@Override
	public boolean hasAchievement(Achievement achievement) {

		return false;
	}

	@Override
	public void incrementStatistic(Statistic statistic) throws IllegalArgumentException {

		
	}

	@Override
	public void decrementStatistic(Statistic statistic) throws IllegalArgumentException {

		
	}

	@Override
	public void incrementStatistic(Statistic statistic, int amount) throws IllegalArgumentException {

		
	}

	@Override
	public void decrementStatistic(Statistic statistic, int amount) throws IllegalArgumentException {

		
	}

	@Override
	public void setStatistic(Statistic statistic, int newValue) throws IllegalArgumentException {

		
	}

	@Override
	public int getStatistic(Statistic statistic) throws IllegalArgumentException {

		return 0;
	}

	@Override
	public void incrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {

		
	}

	@Override
	public void decrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {

		
	}

	@Override
	public int getStatistic(Statistic statistic, Material material) throws IllegalArgumentException {

		return 0;
	}

	@Override
	public void incrementStatistic(Statistic statistic, Material material, int amount) throws IllegalArgumentException {

		
	}

	@Override
	public void decrementStatistic(Statistic statistic, Material material, int amount) throws IllegalArgumentException {

		
	}

	@Override
	public void setStatistic(Statistic statistic, Material material, int newValue) throws IllegalArgumentException {

		
	}

	@Override
	public void incrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {

		
	}

	@Override
	public void decrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {

		
	}

	@Override
	public int getStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {

		return 0;
	}

	@Override
	public void incrementStatistic(Statistic statistic, EntityType entityType, int amount)
			throws IllegalArgumentException {

		
	}

	@Override
	public void decrementStatistic(Statistic statistic, EntityType entityType, int amount) {

		
	}

	@Override
	public void setStatistic(Statistic statistic, EntityType entityType, int newValue) {

		
	}

	@Override
	public void setPlayerTime(long time, boolean relative) {

		
	}

	@Override
	public long getPlayerTime() {

		return 0;
	}

	@Override
	public long getPlayerTimeOffset() {

		return 0;
	}

	@Override
	public boolean isPlayerTimeRelative() {

		return false;
	}

	@Override
	public void resetPlayerTime() {

		
	}

	@Override
	public void setPlayerWeather(WeatherType type) {

		
	}

	@Override
	public WeatherType getPlayerWeather() {

		return null;
	}

	@Override
	public void resetPlayerWeather() {

		
	}

	@Override
	public void giveExp(int amount) {

		
	}

	@Override
	public void giveExpLevels(int amount) {

		
	}

	@Override
	public float getExp() {

		return 0;
	}

	@Override
	public void setExp(float exp) {

		
	}

	@Override
	public int getLevel() {

		return 0;
	}

	@Override
	public void setLevel(int level) {

		
	}

	@Override
	public int getTotalExperience() {

		return 0;
	}

	@Override
	public void setTotalExperience(int exp) {

		
	}

	@Override
	public float getExhaustion() {

		return 0;
	}

	@Override
	public void setExhaustion(float value) {

		
	}

	@Override
	public float getSaturation() {

		return 0;
	}

	@Override
	public void setSaturation(float value) {

		
	}

	@Override
	public int getFoodLevel() {

		return 0;
	}

	@Override
	public void setFoodLevel(int value) {

		
	}

	@Override
	public Location getBedSpawnLocation() {

		return null;
	}

	@Override
	public void setBedSpawnLocation(Location location) {

		
	}

	@Override
	public void setBedSpawnLocation(Location location, boolean force) {

		
	}

	@Override
	public boolean getAllowFlight() {

		return false;
	}

	@Override
	public void setAllowFlight(boolean flight) {

		
	}

	@Override
	public void hidePlayer(Player player) {

		
	}

	@Override
	public void hidePlayer(Plugin plugin, Player player) {

		
	}

	@Override
	public void showPlayer(Player player) {

		
	}

	@Override
	public void showPlayer(Plugin plugin, Player player) {

		
	}

	@Override
	public boolean canSee(Player player) {

		return false;
	}

	@Override
	public boolean isFlying() {

		return false;
	}

	@Override
	public void setFlying(boolean value) {

		
	}

	@Override
	public void setFlySpeed(float value) throws IllegalArgumentException {

		
	}

	@Override
	public void setWalkSpeed(float value) throws IllegalArgumentException {

		
	}

	@Override
	public float getFlySpeed() {

		return 0;
	}

	@Override
	public float getWalkSpeed() {

		return 0;
	}

	@Override
	public void setTexturePack(String url) {

		
	}

	@Override
	public void setResourcePack(String url) {

		
	}

	@Override
	public void setResourcePack(String url, byte[] hash) {

		
	}

	@Override
	public Scoreboard getScoreboard() {

		return null;
	}

	@Override
	public void setScoreboard(Scoreboard scoreboard) throws IllegalArgumentException, IllegalStateException {

		
	}

	@Override
	public boolean isHealthScaled() {

		return false;
	}

	@Override
	public void setHealthScaled(boolean scale) {

		
	}

	@Override
	public void setHealthScale(double scale) throws IllegalArgumentException {

		
	}

	@Override
	public double getHealthScale() {

		return 0;
	}

	@Override
	public org.bukkit.entity.Entity getSpectatorTarget() {

		return null;
	}

	@Override
	public void setSpectatorTarget(org.bukkit.entity.Entity entity) {

		
	}

	@Override
	public void sendTitle(String title, String subtitle) {

		
	}

	@Override
	public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {

		
	}

	@Override
	public void resetTitle() {

		
	}

	@Override
	public void spawnParticle(Particle particle, Location location, int count) {

		
	}

	@Override
	public void spawnParticle(Particle particle, double x, double y, double z, int count) {

		
	}

	@Override
	public <T> void spawnParticle(Particle particle, Location location, int count, T data) {

		
	}

	@Override
	public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, T data) {

		
	}

	@Override
	public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY,
			double offsetZ) {

		
	}

	@Override
	public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX,
			double offsetY, double offsetZ) {

		
	}

	@Override
	public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY,
			double offsetZ, T data) {

		
	}

	@Override
	public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX,
			double offsetY, double offsetZ, T data) {

		
	}

	@Override
	public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY,
			double offsetZ, double extra) {

		
	}

	@Override
	public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX,
			double offsetY, double offsetZ, double extra) {

		
	}

	@Override
	public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY,
			double offsetZ, double extra, T data) {

		
	}

	@Override
	public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX,
			double offsetY, double offsetZ, double extra, T data) {

		
	}

	@Override
	public org.bukkit.advancement.AdvancementProgress getAdvancementProgress(Advancement advancement) {

		return null;
	}

	@Override
	public String getLocale() {

		return null;
	}

	public ICommandSender getHandle() {

		return null;
	}
    
}
