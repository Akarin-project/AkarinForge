/*
 * Akarin Forge
 */
package org.bukkit.inventory;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

public interface EntityEquipment {
    public ItemStack getItemInMainHand();

    public void setItemInMainHand(ItemStack var1);

    public ItemStack getItemInOffHand();

    public void setItemInOffHand(ItemStack var1);

    @Deprecated
    public ItemStack getItemInHand();

    @Deprecated
    public void setItemInHand(ItemStack var1);

    public ItemStack getHelmet();

    public void setHelmet(ItemStack var1);

    public ItemStack getChestplate();

    public void setChestplate(ItemStack var1);

    public ItemStack getLeggings();

    public void setLeggings(ItemStack var1);

    public ItemStack getBoots();

    public void setBoots(ItemStack var1);

    public ItemStack[] getArmorContents();

    public void setArmorContents(ItemStack[] var1);

    public void clear();

    @Deprecated
    public float getItemInHandDropChance();

    @Deprecated
    public void setItemInHandDropChance(float var1);

    public float getItemInMainHandDropChance();

    public void setItemInMainHandDropChance(float var1);

    public float getItemInOffHandDropChance();

    public void setItemInOffHandDropChance(float var1);

    public float getHelmetDropChance();

    public void setHelmetDropChance(float var1);

    public float getChestplateDropChance();

    public void setChestplateDropChance(float var1);

    public float getLeggingsDropChance();

    public void setLeggingsDropChance(float var1);

    public float getBootsDropChance();

    public void setBootsDropChance(float var1);

    public Entity getHolder();
}

