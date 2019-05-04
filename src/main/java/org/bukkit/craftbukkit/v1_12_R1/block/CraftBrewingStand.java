/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftContainer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryBrewer;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.Inventory;

public class CraftBrewingStand
extends CraftContainer<avk>
implements BrewingStand {
    public CraftBrewingStand(Block block) {
        super(block, avk.class);
    }

    public CraftBrewingStand(Material material, avk te2) {
        super(material, te2);
    }

    @Override
    public BrewerInventory getSnapshotInventory() {
        return new CraftInventoryBrewer((tv)this.getSnapshot());
    }

    @Override
    public BrewerInventory getInventory() {
        if (!this.isPlaced()) {
            return this.getSnapshotInventory();
        }
        return new CraftInventoryBrewer((tv)this.getTileEntity());
    }

    @Override
    public int getBrewingTime() {
        return ((avk)this.getSnapshot()).c(0);
    }

    @Override
    public void setBrewingTime(int brewTime) {
        ((avk)this.getSnapshot()).b(0, brewTime);
    }

    @Override
    public int getFuelLevel() {
        return ((avk)this.getSnapshot()).c(1);
    }

    @Override
    public void setFuelLevel(int level) {
        ((avk)this.getSnapshot()).b(1, level);
    }

    @Override
    public String getCustomName() {
        avk brewingStand = (avk)this.getSnapshot();
        return brewingStand.n_() ? brewingStand.h_() : null;
    }

    @Override
    public void setCustomName(String name) {
        ((avk)this.getSnapshot()).a(name);
    }

    @Override
    public void applyTo(avk brewingStand) {
        super.applyTo(brewingStand);
        if (!((avk)this.getSnapshot()).n_()) {
            brewingStand.a((String)null);
        }
    }
}

