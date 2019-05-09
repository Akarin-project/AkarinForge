/*
 * Akarin Forge
 */
package org.bukkit.block;

import java.util.Collection;
import org.bukkit.Nameable;
import org.bukkit.block.Container;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.BeaconInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public interface Beacon
extends Container,
Nameable {
    @Override
    public BeaconInventory getInventory();

    @Override
    public BeaconInventory getSnapshotInventory();

    public Collection<LivingEntity> getEntitiesInRange();

    public int getTier();

    public PotionEffect getPrimaryEffect();

    public void setPrimaryEffect(PotionEffectType var1);

    public PotionEffect getSecondaryEffect();

    public void setSecondaryEffect(PotionEffectType var1);
}

