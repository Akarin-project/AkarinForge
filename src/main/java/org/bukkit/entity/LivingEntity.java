/*
 * Akarin Forge
 */
package org.bukkit.entity;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attributable;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

public interface LivingEntity
extends Attributable,
Entity,
Damageable,
ProjectileSource {
    public double getEyeHeight();

    public double getEyeHeight(boolean var1);

    public Location getEyeLocation();

    public List<Block> getLineOfSight(Set<Material> var1, int var2);

    public Block getTargetBlock(Set<Material> var1, int var2);

    public List<Block> getLastTwoTargetBlocks(Set<Material> var1, int var2);

    public int getRemainingAir();

    public void setRemainingAir(int var1);

    public int getMaximumAir();

    public void setMaximumAir(int var1);

    public int getMaximumNoDamageTicks();

    public void setMaximumNoDamageTicks(int var1);

    public double getLastDamage();

    public void setLastDamage(double var1);

    public int getNoDamageTicks();

    public void setNoDamageTicks(int var1);

    public Player getKiller();

    public boolean addPotionEffect(PotionEffect var1);

    public boolean addPotionEffect(PotionEffect var1, boolean var2);

    public boolean addPotionEffects(Collection<PotionEffect> var1);

    public boolean hasPotionEffect(PotionEffectType var1);

    public PotionEffect getPotionEffect(PotionEffectType var1);

    public void removePotionEffect(PotionEffectType var1);

    public Collection<PotionEffect> getActivePotionEffects();

    public boolean hasLineOfSight(Entity var1);

    public boolean getRemoveWhenFarAway();

    public void setRemoveWhenFarAway(boolean var1);

    public EntityEquipment getEquipment();

    public void setCanPickupItems(boolean var1);

    public boolean getCanPickupItems();

    public boolean isLeashed();

    public Entity getLeashHolder() throws IllegalStateException;

    public boolean setLeashHolder(Entity var1);

    public boolean isGliding();

    public void setGliding(boolean var1);

    public void setAI(boolean var1);

    public boolean hasAI();

    public void setCollidable(boolean var1);

    public boolean isCollidable();
}

