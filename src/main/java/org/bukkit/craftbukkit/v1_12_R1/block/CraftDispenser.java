/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.block;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftLootable;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_12_R1.projectiles.CraftBlockProjectileSource;
import org.bukkit.inventory.Inventory;
import org.bukkit.projectiles.BlockProjectileSource;

public class CraftDispenser
extends CraftLootable<avp>
implements Dispenser {
    public CraftDispenser(Block block) {
        super(block, avp.class);
    }

    public CraftDispenser(Material material, avp te2) {
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
    public BlockProjectileSource getBlockProjectileSource() {
        Block block = this.getBlock();
        if (block.getType() != Material.DISPENSER) {
            return null;
        }
        return new CraftBlockProjectileSource((avp)this.getTileEntityFromWorld());
    }

    @Override
    public boolean dispense() {
        Block block = this.getBlock();
        if (block.getType() == Material.DISPENSER) {
            CraftWorld world = (CraftWorld)this.getWorld();
            apz dispense = (apz)aox.z;
            dispense.c(world.getHandle(), new et(this.getX(), this.getY(), this.getZ()));
            return true;
        }
        return false;
    }
}

