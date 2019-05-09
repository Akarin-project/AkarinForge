/*
 * Akarin Forge
 */
package org.bukkit.event.inventory;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.InventoryView;

public abstract class InventoryInteractEvent
extends InventoryEvent
implements Cancellable {
    private Event.Result result = Event.Result.DEFAULT;

    public InventoryInteractEvent(InventoryView transaction) {
        super(transaction);
    }

    public HumanEntity getWhoClicked() {
        return this.getView().getPlayer();
    }

    public void setResult(Event.Result newResult) {
        this.result = newResult;
    }

    public Event.Result getResult() {
        return this.result;
    }

    @Override
    public boolean isCancelled() {
        return this.getResult() == Event.Result.DENY;
    }

    @Override
    public void setCancelled(boolean toCancel) {
        this.setResult(toCancel ? Event.Result.DENY : Event.Result.ALLOW);
    }
}

