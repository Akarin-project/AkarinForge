/*
 * Akarin Forge
 */
package org.bukkit.inventory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public interface Inventory
extends Iterable<ItemStack> {
    public int getSize();

    public int getMaxStackSize();

    public void setMaxStackSize(int var1);

    public String getName();

    public ItemStack getItem(int var1);

    public void setItem(int var1, ItemStack var2);

    public /* varargs */ HashMap<Integer, ItemStack> addItem(ItemStack ... var1) throws IllegalArgumentException;

    public /* varargs */ HashMap<Integer, ItemStack> removeItem(ItemStack ... var1) throws IllegalArgumentException;

    public ItemStack[] getContents();

    public void setContents(ItemStack[] var1) throws IllegalArgumentException;

    public ItemStack[] getStorageContents();

    public void setStorageContents(ItemStack[] var1) throws IllegalArgumentException;

    @Deprecated
    public boolean contains(int var1);

    public boolean contains(Material var1) throws IllegalArgumentException;

    public boolean contains(ItemStack var1);

    @Deprecated
    public boolean contains(int var1, int var2);

    public boolean contains(Material var1, int var2) throws IllegalArgumentException;

    public boolean contains(ItemStack var1, int var2);

    public boolean containsAtLeast(ItemStack var1, int var2);

    @Deprecated
    public HashMap<Integer, ? extends ItemStack> all(int var1);

    public HashMap<Integer, ? extends ItemStack> all(Material var1) throws IllegalArgumentException;

    public HashMap<Integer, ? extends ItemStack> all(ItemStack var1);

    @Deprecated
    public int first(int var1);

    public int first(Material var1) throws IllegalArgumentException;

    public int first(ItemStack var1);

    public int firstEmpty();

    @Deprecated
    public void remove(int var1);

    public void remove(Material var1) throws IllegalArgumentException;

    public void remove(ItemStack var1);

    public void clear(int var1);

    public void clear();

    public List<HumanEntity> getViewers();

    public String getTitle();

    public InventoryType getType();

    public InventoryHolder getHolder();

    @Override
    public ListIterator<ItemStack> iterator();

    public ListIterator<ItemStack> iterator(int var1);

    public Location getLocation();
}

