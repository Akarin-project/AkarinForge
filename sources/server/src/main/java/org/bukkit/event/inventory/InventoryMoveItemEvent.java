/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.Validate
 */
package org.bukkit.event.inventory;

import org.apache.commons.lang.Validate;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryMoveItemEvent
extends Event
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final Inventory sourceInventory;
    private final Inventory destinationInventory;
    private ItemStack itemStack;
    private final boolean didSourceInitiate;

    public InventoryMoveItemEvent(Inventory sourceInventory, ItemStack itemStack, Inventory destinationInventory, boolean didSourceInitiate) {
        Validate.notNull((Object)itemStack, (String)"ItemStack cannot be null");
        this.sourceInventory = sourceInventory;
        this.itemStack = itemStack;
        this.destinationInventory = destinationInventory;
        this.didSourceInitiate = didSourceInitiate;
    }

    public Inventory getSource() {
        return this.sourceInventory;
    }

    public ItemStack getItem() {
        return this.itemStack.clone();
    }

    public void setItem(ItemStack itemStack) {
        Validate.notNull((Object)itemStack, (String)"ItemStack cannot be null.  Cancel the event if you want nothing to be transferred.");
        this.itemStack = itemStack.clone();
    }

    public Inventory getDestination() {
        return this.destinationInventory;
    }

    public Inventory getInitiator() {
        return this.didSourceInitiate ? this.sourceInventory : this.destinationInventory;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

