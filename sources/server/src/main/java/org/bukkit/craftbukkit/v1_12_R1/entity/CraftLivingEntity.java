package org.bukkit.craftbukkit.v1_12_R1.entity;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;

public class CraftLivingEntity extends CraftEntity implements LivingEntity {
    protected Entity entity;
	
    public EntityLivingBase getHandle() {
        return (EntityLivingBase) entity;
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
	public boolean teleport(Entity destination) {

		return false;
	}

	@Override
	public boolean teleport(Entity destination, TeleportCause cause) {

		return false;
	}

	@Override
	public List<Entity> getNearbyEntities(double x, double y, double z) {

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

		return MinecraftServer.instance().server;
	}

	@Override
	public Entity getPassenger() {

		return null;
	}

	@Override
	public boolean setPassenger(Entity passenger) {

		return false;
	}

	@Override
	public List<Entity> getPassengers() {

		return null;
	}

	@Override
	public boolean addPassenger(Entity passenger) {

		return false;
	}

	@Override
	public boolean removePassenger(Entity passenger) {

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
	public Entity getVehicle() {

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
	public String getName() {

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
	public void damage(double amount, Entity source) {

		
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
	public boolean hasLineOfSight(Entity other) {

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
	public Entity getLeashHolder() throws IllegalStateException {

		return null;
	}

	@Override
	public boolean setLeashHolder(Entity holder) {

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
    
}
