/*
 * Akarin Forge
 */
package org.bukkit.event.inventory;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class InventoryCreativeEvent
extends InventoryClickEvent {
    private ItemStack item;

    public InventoryCreativeEvent(InventoryView what, InventoryType.SlotType type, int slot, ItemStack newItem) {
        super(what, type, slot, ClickType.CREATIVE, InventoryAction.PLACE_ALL);
        this.item = newItem;
    }

    @Override
    public ItemStack getCursor() {
        return this.item;
    }

    @Override
    public void setCursor(ItemStack item) {
        this.item = item;
    }
}

