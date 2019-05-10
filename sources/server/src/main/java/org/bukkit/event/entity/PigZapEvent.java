/*
 * Akarin Forge
 */
package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

public class PigZapEvent
extends EntityEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean canceled;
    private final PigZombie pigzombie;
    private final LightningStrike bolt;

    public PigZapEvent(Pig pig, LightningStrike bolt, PigZombie pigzombie) {
        super(pig);
        this.bolt = bolt;
        this.pigzombie = pigzombie;
    }

    @Override
    public boolean isCancelled() {
        return this.canceled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.canceled = cancel;
    }

    @Override
    public Pig getEntity() {
        return (Pig)this.entity;
    }

    public LightningStrike getLightning() {
        return this.bolt;
    }

    public PigZombie getPigZombie() {
        return this.pigzombie;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

