/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.inventory;

import org.bukkit.Location;
import org.bukkit.block.DoubleChest;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryDoubleChest
extends CraftInventory
implements DoubleChestInventory {
    private final CraftInventory left;
    private final CraftInventory right;

    public CraftInventoryDoubleChest(CraftInventory left, CraftInventory right) {
        super(new tu("Large chest", (uh)left.getInventory(), (uh)right.getInventory()));
        this.left = left;
        this.right = right;
    }

    public CraftInventoryDoubleChest(tu largeChest) {
        super(largeChest);
        this.left = largeChest.b instanceof tu ? new CraftInventoryDoubleChest((tu)largeChest.b) : new CraftInventory(largeChest.b);
        this.right = largeChest.c instanceof tu ? new CraftInventoryDoubleChest((tu)largeChest.c) : new CraftInventory(largeChest.c);
    }

    @Override
    public Inventory getLeftSide() {
        return this.left;
    }

    @Override
    public Inventory getRightSide() {
        return this.right;
    }

    @Override
    public void setContents(ItemStack[] items) {
        if (this.getInventory().w_() < items.length) {
            throw new IllegalArgumentException("Invalid inventory size; expected " + this.getInventory().w_() + " or less");
        }
        ItemStack[] leftItems = new ItemStack[this.left.getSize()];
        ItemStack[] rightItems = new ItemStack[this.right.getSize()];
        System.arraycopy(items, 0, leftItems, 0, Math.min(this.left.getSize(), items.length));
        this.left.setContents(leftItems);
        if (items.length >= this.left.getSize()) {
            System.arraycopy(items, this.left.getSize(), rightItems, 0, Math.min(this.right.getSize(), items.length - this.left.getSize()));
            this.right.setContents(rightItems);
        }
    }

    @Override
    public DoubleChest getHolder() {
        return new DoubleChest(this);
    }

    @Override
    public Location getLocation() {
        return this.getLeftSide().getLocation().add(this.getRightSide().getLocation()).multiply(0.5);
    }
}

