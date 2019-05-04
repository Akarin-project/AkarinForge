/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.block;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.ShulkerBox;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftLootable;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers;
import org.bukkit.inventory.Inventory;

public class CraftShulkerBox
extends CraftLootable<awb>
implements ShulkerBox {
    public CraftShulkerBox(Block block) {
        super(block, awb.class);
    }

    public CraftShulkerBox(Material material, awb te2) {
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
    public DyeColor getColor() {
        aow block = CraftMagicNumbers.getBlock(this.getType());
        return DyeColor.getByWoolData((byte)((atr)block).b.a());
    }
}

