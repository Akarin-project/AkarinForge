/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.block;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftLootable;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryDoubleChest;
import org.bukkit.inventory.Inventory;

public class CraftChest
extends CraftLootable<avl>
implements Chest {
    public CraftChest(Block block) {
        super(block, avl.class);
    }

    public CraftChest(Material material, avl te2) {
        super(material, te2);
    }

    @Override
    public Inventory getSnapshotInventory() {
        return new CraftInventory((tv)this.getSnapshot());
    }

    @Override
    public Inventory getBlockInventory() {
        if (!this.isPlaced()) {
            return this.getSnapshotInventory();
        }
        return new CraftInventory((tv)this.getTileEntity());
    }

    @Override
    public Inventory getInventory() {
        int id2;
        CraftInventory right;
        CraftInventory left;
        CraftInventory inventory = (CraftInventory)this.getBlockInventory();
        if (!this.isPlaced()) {
            return inventory;
        }
        int x2 = this.getX();
        int y2 = this.getY();
        int z2 = this.getZ();
        CraftWorld world = (CraftWorld)this.getWorld();
        if (world.getBlockTypeIdAt(x2, y2, z2) == Material.CHEST.getId()) {
            id2 = Material.CHEST.getId();
        } else if (world.getBlockTypeIdAt(x2, y2, z2) == Material.TRAPPED_CHEST.getId()) {
            id2 = Material.TRAPPED_CHEST.getId();
        } else {
            throw new IllegalStateException("CraftChest is not a chest but is instead " + world.getBlockAt(x2, y2, z2));
        }
        if (world.getBlockTypeIdAt(x2 - 1, y2, z2) == id2) {
            left = new CraftInventory((avl)world.getHandle().r(new et(x2 - 1, y2, z2)));
            inventory = new CraftInventoryDoubleChest(left, inventory);
        }
        if (world.getBlockTypeIdAt(x2 + 1, y2, z2) == id2) {
            right = new CraftInventory((avl)world.getHandle().r(new et(x2 + 1, y2, z2)));
            inventory = new CraftInventoryDoubleChest(inventory, right);
        }
        if (world.getBlockTypeIdAt(x2, y2, z2 - 1) == id2) {
            left = new CraftInventory((avl)world.getHandle().r(new et(x2, y2, z2 - 1)));
            inventory = new CraftInventoryDoubleChest(left, inventory);
        }
        if (world.getBlockTypeIdAt(x2, y2, z2 + 1) == id2) {
            right = new CraftInventory((avl)world.getHandle().r(new et(x2, y2, z2 + 1)));
            inventory = new CraftInventoryDoubleChest(inventory, right);
        }
        return inventory;
    }
}

