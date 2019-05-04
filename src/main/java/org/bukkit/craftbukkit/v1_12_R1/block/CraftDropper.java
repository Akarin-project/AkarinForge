/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.block;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Dropper;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftLootable;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;

public class CraftDropper
extends CraftLootable<avq>
implements Dropper {
    public CraftDropper(Block block) {
        super(block, avq.class);
    }

    public CraftDropper(Material material, avq te2) {
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

    @Override
    public void drop() {
        Block block = this.getBlock();
        if (block.getType() == Material.DROPPER) {
            CraftWorld world = (CraftWorld)this.getWorld();
            aqd drop = (aqd)aox.ct;
            drop.c(world.getHandle(), new et(this.getX(), this.getY(), this.getZ()));
        }
    }
}

