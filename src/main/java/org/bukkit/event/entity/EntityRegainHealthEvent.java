/*
 * Akarin Forge
 */
package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

public class EntityRegainHealthEvent
extends EntityEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private double amount;
    private final RegainReason regainReason;

    public EntityRegainHealthEvent(Entity entity, double amount, RegainReason regainReason) {
        super(entity);
        this.amount = amount;
        this.regainReason = regainReason;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public RegainReason getRegainReason() {
        return this.regainReason;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public static enum RegainReason {
        REGEN,
        SATIATED,
        EATING,
        ENDER_CRYSTAL,
        MAGIC,
        MAGIC_REGEN,
        WITHER_SPAWN,
        WITHER,
        CUSTOM;
        

        private RegainReason() {
        }
    }

}

