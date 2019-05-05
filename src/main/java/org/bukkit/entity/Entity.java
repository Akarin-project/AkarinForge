/*
 * Akarin Forge
 */
package org.bukkit.entity;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Nameable;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.Metadatable;
import org.bukkit.util.Vector;

public interface Entity
extends Metadatable,
CommandSender,
Nameable {
    public Location getLocation();

    public Location getLocation(Location var1);

    public void setVelocity(Vector var1);

    public Vector getVelocity();

    public double getHeight();

    public double getWidth();

    public boolean isOnGround();

    public World getWorld();

    public boolean teleport(Location var1);

    public boolean teleport(Location var1, PlayerTeleportEvent.TeleportCause var2);

    public boolean teleport(Entity var1);

    public boolean teleport(Entity var1, PlayerTeleportEvent.TeleportCause var2);

    public List<Entity> getNearbyEntities(double var1, double var3, double var5);

    public int getEntityId();

    public int getFireTicks();

    public int getMaxFireTicks();

    public void setFireTicks(int var1);

    public void remove();

    public boolean isDead();

    public boolean isValid();

    @Override
    public Server getServer();

    @Deprecated
    public Entity getPassenger();

    @Deprecated
    public boolean setPassenger(Entity var1);

    public List<Entity> getPassengers();

    public boolean addPassenger(Entity var1);

    public boolean removePassenger(Entity var1);

    public boolean isEmpty();

    public boolean eject();

    public float getFallDistance();

    public void setFallDistance(float var1);

    public void setLastDamageCause(EntityDamageEvent var1);

    public EntityDamageEvent getLastDamageCause();

    public UUID getUniqueId();

    public int getTicksLived();

    public void setTicksLived(int var1);

    public void playEffect(EntityEffect var1);

    public EntityType getType();

    public boolean isInsideVehicle();

    public boolean leaveVehicle();

    public Entity getVehicle();

    public void setCustomNameVisible(boolean var1);

    public boolean isCustomNameVisible();

    public void setGlowing(boolean var1);

    public boolean isGlowing();

    public void setInvulnerable(boolean var1);

    public boolean isInvulnerable();

    public boolean isSilent();

    public void setSilent(boolean var1);

    public boolean hasGravity();

    public void setGravity(boolean var1);

    public int getPortalCooldown();

    public void setPortalCooldown(int var1);

    public Set<String> getScoreboardTags();

    public boolean addScoreboardTag(String var1);

    public boolean removeScoreboardTag(String var1);

    public PistonMoveReaction getPistonMoveReaction();

    public Spigot spigot();

    public static class Spigot
    extends CommandSender.Spigot {
        public boolean isInvulnerable() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

}

