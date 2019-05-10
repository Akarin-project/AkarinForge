/*
 * Akarin Forge
 */
package org.bukkit.event.inventory;

import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class InventoryClickEvent
extends InventoryInteractEvent {
    private static final HandlerList handlers = new HandlerList();
    private final ClickType click;
    private final InventoryAction action;
    private final Inventory clickedInventory;
    private InventoryType.SlotType slot_type;
    private int whichSlot;
    private int rawSlot;
    private ItemStack current = null;
    private int hotbarKey = -1;

    public InventoryClickEvent(InventoryView view, InventoryType.SlotType type, int slot, ClickType click, InventoryAction action) {
        super(view);
        this.slot_type = type;
        this.rawSlot = slot;
        this.clickedInventory = slot < 0 ? null : (view.getTopInventory() != null && slot < view.getTopInventory().getSize() ? view.getTopInventory() : view.getBottomInventory());
        this.whichSlot = view.convertSlot(slot);
        this.click = click;
        this.action = action;
    }

    public InventoryClickEvent(InventoryView view, InventoryType.SlotType type, int slot, ClickType click, InventoryAction action, int key) {
        this(view, type, slot, click, action);
        this.hotbarKey = key;
    }

    public Inventory getClickedInventory() {
        return this.clickedInventory;
    }

    public InventoryType.SlotType getSlotType() {
        return this.slot_type;
    }

    public ItemStack getCursor() {
        return this.getView().getCursor();
    }

    public ItemStack getCurrentItem() {
        if (this.slot_type == InventoryType.SlotType.OUTSIDE) {
            return this.current;
        }
        return this.getView().getItem(this.rawSlot);
    }

    public boolean isRightClick() {
        return this.click.isRightClick();
    }

    public boolean isLeftClick() {
        return this.click.isLeftClick();
    }

    public boolean isShiftClick() {
        return this.click.isShiftClick();
    }

    @Deprecated
    public void setCursor(ItemStack stack) {
        this.getView().setCursor(stack);
    }

    public void setCurrentItem(ItemStack stack) {
        if (this.slot_type == InventoryType.SlotType.OUTSIDE) {
            this.current = stack;
        } else {
            this.getView().setItem(this.rawSlot, stack);
        }
    }

    public int getSlot() {
        return this.whichSlot;
    }

    public int getRawSlot() {
        return this.rawSlot;
    }

    public int getHotbarButton() {
        return this.hotbarKey;
    }

    public InventoryAction getAction() {
        return this.action;
    }

    public ClickType getClick() {
        return this.click;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

