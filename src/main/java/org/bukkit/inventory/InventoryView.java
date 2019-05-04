/*
 * Akarin Forge
 */
package org.bukkit.inventory;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class InventoryView {
    public static final int OUTSIDE = -999;

    public abstract Inventory getTopInventory();

    public abstract Inventory getBottomInventory();

    public abstract HumanEntity getPlayer();

    public abstract InventoryType getType();

    public void setItem(int slot, ItemStack item) {
        if (slot != -999) {
            if (slot < this.getTopInventory().getSize()) {
                this.getTopInventory().setItem(this.convertSlot(slot), item);
            } else {
                this.getBottomInventory().setItem(this.convertSlot(slot), item);
            }
        } else {
            this.getPlayer().getWorld().dropItemNaturally(this.getPlayer().getLocation(), item);
        }
    }

    public ItemStack getItem(int slot) {
        if (slot == -999) {
            return null;
        }
        if (slot < this.getTopInventory().getSize()) {
            return this.getTopInventory().getItem(this.convertSlot(slot));
        }
        return this.getBottomInventory().getItem(this.convertSlot(slot));
    }

    public final void setCursor(ItemStack item) {
        this.getPlayer().setItemOnCursor(item);
    }

    public final ItemStack getCursor() {
        return this.getPlayer().getItemOnCursor();
    }

    public final int convertSlot(int rawSlot) {
        int numInTop = this.getTopInventory().getSize();
        if (rawSlot < numInTop) {
            return rawSlot;
        }
        int slot = rawSlot - numInTop;
        if (this.getType() == InventoryType.CRAFTING || this.getType() == InventoryType.CREATIVE) {
            if (slot < 4) {
                return 39 - slot;
            }
            if (slot > 39) {
                return slot;
            }
            slot -= 4;
        }
        slot = slot >= 27 ? (slot -= 27) : (slot += 9);
        return slot;
    }

    public final void close() {
        this.getPlayer().closeInventory();
    }

    public final int countSlots() {
        return this.getTopInventory().getSize() + this.getBottomInventory().getSize();
    }

    public final boolean setProperty(Property prop, int value) {
        return this.getPlayer().setWindowProperty(prop, value);
    }

    public final String getTitle() {
        return this.getTopInventory().getTitle();
    }

    public static enum Property {
        BREW_TIME(0, InventoryType.BREWING),
        BURN_TIME(0, InventoryType.FURNACE),
        TICKS_FOR_CURRENT_FUEL(1, InventoryType.FURNACE),
        COOK_TIME(2, InventoryType.FURNACE),
        TICKS_FOR_CURRENT_SMELTING(3, InventoryType.FURNACE),
        ENCHANT_BUTTON1(0, InventoryType.ENCHANTING),
        ENCHANT_BUTTON2(1, InventoryType.ENCHANTING),
        ENCHANT_BUTTON3(2, InventoryType.ENCHANTING),
        ENCHANT_XP_SEED(3, InventoryType.ENCHANTING),
        ENCHANT_ID1(4, InventoryType.ENCHANTING),
        ENCHANT_ID2(5, InventoryType.ENCHANTING),
        ENCHANT_ID3(6, InventoryType.ENCHANTING),
        ENCHANT_LEVEL1(7, InventoryType.ENCHANTING),
        ENCHANT_LEVEL2(8, InventoryType.ENCHANTING),
        ENCHANT_LEVEL3(9, InventoryType.ENCHANTING),
        LEVELS(0, InventoryType.BEACON),
        PRIMARY_EFFECT(1, InventoryType.BEACON),
        SECONDARY_EFFECT(2, InventoryType.BEACON),
        REPAIR_COST(0, InventoryType.ANVIL);
        
        int id;
        InventoryType style;

        private Property(int id2, InventoryType appliesTo) {
            this.id = id2;
            this.style = appliesTo;
        }

        public InventoryType getType() {
            return this.style;
        }

        @Deprecated
        public int getId() {
            return this.id;
        }
    }

}

