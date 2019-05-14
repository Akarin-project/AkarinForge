/*
 * Akarin Forge
 */
package org.bukkit.event.inventory;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.InventoryView;

public class InventoryCloseEvent
extends InventoryEvent {
    private static final HandlerList handlers = new HandlerList();

    public InventoryCloseEvent(InventoryView transaction) {
        super(transaction);
    }

    public final HumanEntity getPlayer() {
        return this.transaction.getPlayer();
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

