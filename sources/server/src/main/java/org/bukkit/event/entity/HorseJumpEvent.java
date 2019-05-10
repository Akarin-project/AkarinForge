/*
 * Akarin Forge
 */
package org.bukkit.event.entity;

import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

public class HorseJumpEvent
extends EntityEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private float power;

    public HorseJumpEvent(AbstractHorse horse, float power) {
        super(horse);
        this.power = power;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Deprecated
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public AbstractHorse getEntity() {
        return (AbstractHorse)this.entity;
    }

    public float getPower() {
        return this.power;
    }

    @Deprecated
    public void setPower(float power) {
        this.power = power;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

