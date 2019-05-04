/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.Rotation;
import org.bukkit.entity.Hanging;
import org.bukkit.inventory.ItemStack;

public interface ItemFrame
extends Hanging {
    public ItemStack getItem();

    public void setItem(ItemStack var1);

    public Rotation getRotation();

    public void setRotation(Rotation var1) throws IllegalArgumentException;
}

