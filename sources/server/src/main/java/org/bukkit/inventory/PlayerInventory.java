/*
 * Akarin Forge
 */
package org.bukkit.inventory;

import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public interface PlayerInventory
extends Inventory {
    public ItemStack[] getArmorContents();

    public ItemStack[] getExtraContents();

    public ItemStack getHelmet();

    public ItemStack getChestplate();

    public ItemStack getLeggings();

    public ItemStack getBoots();

    @Override
    public void setItem(int var1, ItemStack var2);

    public void setArmorContents(ItemStack[] var1);

    public void setExtraContents(ItemStack[] var1);

    public void setHelmet(ItemStack var1);

    public void setChestplate(ItemStack var1);

    public void setLeggings(ItemStack var1);

    public void setBoots(ItemStack var1);

    public ItemStack getItemInMainHand();

    public void setItemInMainHand(ItemStack var1);

    public ItemStack getItemInOffHand();

    public void setItemInOffHand(ItemStack var1);

    @Deprecated
    public ItemStack getItemInHand();

    @Deprecated
    public void setItemInHand(ItemStack var1);

    public int getHeldItemSlot();

    public void setHeldItemSlot(int var1);

    @Deprecated
    public int clear(int var1, int var2);

    @Override
    public HumanEntity getHolder();
}

