/*
 * Akarin Forge
 */
package org.bukkit.inventory;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public interface ItemFactory {
    public ItemMeta getItemMeta(Material var1);

    public boolean isApplicable(ItemMeta var1, ItemStack var2) throws IllegalArgumentException;

    public boolean isApplicable(ItemMeta var1, Material var2) throws IllegalArgumentException;

    public boolean equals(ItemMeta var1, ItemMeta var2) throws IllegalArgumentException;

    public ItemMeta asMetaFor(ItemMeta var1, ItemStack var2) throws IllegalArgumentException;

    public ItemMeta asMetaFor(ItemMeta var1, Material var2) throws IllegalArgumentException;

    public Color getDefaultLeatherColor();
}

