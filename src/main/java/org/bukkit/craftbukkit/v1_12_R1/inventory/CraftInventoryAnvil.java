/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.inventory;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryAnvil
extends CraftInventory
implements AnvilInventory {
    private final Location location;
    private final tv resultInventory;
    private final afs container;

    public CraftInventoryAnvil(Location location, tv inventory, tv resultInventory, afs container) {
        super(inventory);
        this.location = location;
        this.resultInventory = resultInventory;
        this.container = container;
    }

    public tv getResultInventory() {
        return this.resultInventory;
    }

    public tv getIngredientsInventory() {
        return this.inventory;
    }

    @Override
    public ItemStack getItem(int slot) {
        if (slot < this.getIngredientsInventory().w_()) {
            aip item = this.getIngredientsInventory().a(slot);
            return item.b() ? null : CraftItemStack.asCraftMirror(item);
        }
        aip item = this.getResultInventory().a(slot - this.getIngredientsInventory().w_());
        return item.b() ? null : CraftItemStack.asCraftMirror(item);
    }

    @Override
    public void setItem(int index, ItemStack item) {
        if (index < this.getIngredientsInventory().w_()) {
            this.getIngredientsInventory().a(index, CraftItemStack.asNMSCopy(item));
        } else {
            this.getResultInventory().a(index - this.getIngredientsInventory().w_(), CraftItemStack.asNMSCopy(item));
        }
    }

    @Override
    public int getSize() {
        return this.getResultInventory().w_() + this.getIngredientsInventory().w_();
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public String getRenameText() {
        return this.container.l;
    }

    @Override
    public int getRepairCost() {
        return this.container.a;
    }

    @Override
    public void setRepairCost(int i2) {
        this.container.a = i2;
    }
}

