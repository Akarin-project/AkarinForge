/*
 * Akarin Forge
 */
package org.bukkit.event.inventory;

import org.bukkit.entity.Item;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;

public class InventoryPickupItemEvent
extends Event
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final Inventory inventory;
    private final Item item;

    public InventoryPickupItemEvent(Inventory inventory, Item item) {
        this.inventory = inventory;
        this.item = item;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public Item getItem() {
        return this.item;
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

