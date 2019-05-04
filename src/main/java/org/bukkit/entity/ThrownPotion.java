/*
 * Akarin Forge
 */
package org.bukkit.entity;

import java.util.Collection;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public interface ThrownPotion
extends Projectile {
    public Collection<PotionEffect> getEffects();

    public ItemStack getItem();

    public void setItem(ItemStack var1);
}

