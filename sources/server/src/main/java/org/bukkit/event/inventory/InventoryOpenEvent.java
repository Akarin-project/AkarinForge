/*
 * Akarin Forge
 */
package org.bukkit.event.inventory;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.InventoryView;

public class InventoryOpenEvent
extends InventoryEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;

    public InventoryOpenEvent(InventoryView transaction) {
        super(transaction);
    }

    public final HumanEntity getPlayer() {
        return this.transaction.getPlayer();
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

