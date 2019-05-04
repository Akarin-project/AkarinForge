/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftLootable;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;

public class CraftHopper
extends CraftLootable<avw>
implements Hopper {
    public CraftHopper(Block block) {
        super(block, avw.class);
    }

    public CraftHopper(Material material, avw te2) {
        super(material, te2);
    }

    @Override
    public Inventory getSnapshotInventory() {
        return new CraftInventory((tv)this.getSnapshot());
    }

    @Override
    public Inventory getInventory() {
        if (!this.isPlaced()) {
            return this.getSnapshotInventory();
        }
        return new CraftInventory((tv)this.getTileEntity());
    }
}

