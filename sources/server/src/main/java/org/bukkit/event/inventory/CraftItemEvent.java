/*
 * Akarin Forge
 */
package org.bukkit.event.inventory;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.Recipe;

public class CraftItemEvent
extends InventoryClickEvent {
    private Recipe recipe;

    public CraftItemEvent(Recipe recipe, InventoryView what, InventoryType.SlotType type, int slot, ClickType click, InventoryAction action) {
        super(what, type, slot, click, action);
        this.recipe = recipe;
    }

    public CraftItemEvent(Recipe recipe, InventoryView what, InventoryType.SlotType type, int slot, ClickType click, InventoryAction action, int key) {
        super(what, type, slot, click, action, key);
        this.recipe = recipe;
    }

    public Recipe getRecipe() {
        return this.recipe;
    }

    @Override
    public CraftingInventory getInventory() {
        return (CraftingInventory)super.getInventory();
    }
}

