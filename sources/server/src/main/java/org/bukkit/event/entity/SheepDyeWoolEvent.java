/*
 * Akarin Forge
 */
package org.bukkit.event.entity;

import org.bukkit.DyeColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Sheep;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

public class SheepDyeWoolEvent
extends EntityEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    private DyeColor color;

    public SheepDyeWoolEvent(Sheep sheep, DyeColor color) {
        super(sheep);
        this.color = color;
    }

    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public Sheep getEntity() {
        return (Sheep)this.entity;
    }

    public DyeColor getColor() {
        return this.color;
    }

    public void setColor(DyeColor color) {
        this.color = color;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

