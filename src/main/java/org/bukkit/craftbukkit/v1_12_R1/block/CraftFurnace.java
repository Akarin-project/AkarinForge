/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftContainer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryFurnace;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.Inventory;

public class CraftFurnace
extends CraftContainer<avu>
implements Furnace {
    public CraftFurnace(Block block) {
        super(block, avu.class);
    }

    public CraftFurnace(Material material, avu te2) {
        super(material, te2);
    }

    @Override
    public FurnaceInventory getSnapshotInventory() {
        return new CraftInventoryFurnace((avu)this.getSnapshot());
    }

    @Override
    public FurnaceInventory getInventory() {
        if (!this.isPlaced()) {
            return this.getSnapshotInventory();
        }
        return new CraftInventoryFurnace((avu)this.getTileEntity());
    }

    @Override
    public short getBurnTime() {
        return (short)((avu)this.getSnapshot()).c(0);
    }

    @Override
    public void setBurnTime(short burnTime) {
        ((avu)this.getSnapshot()).b(0, burnTime);
    }

    @Override
    public short getCookTime() {
        return (short)((avu)this.getSnapshot()).c(2);
    }

    @Override
    public void setCookTime(short cookTime) {
        ((avu)this.getSnapshot()).b(2, cookTime);
    }

    @Override
    public String getCustomName() {
        avu furnace = (avu)this.getSnapshot();
        return furnace.n_() ? furnace.h_() : null;
    }

    @Override
    public void setCustomName(String name) {
        ((avu)this.getSnapshot()).a(name);
    }

    @Override
    public void applyTo(avu furnace) {
        super.applyTo(furnace);
        if (!((avu)this.getSnapshot()).n_()) {
            furnace.a((String)null);
        }
    }
}

