/*
 * Akarin Forge
 */
package org.bukkit.event.entity;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

public class EntityInteractEvent
extends EntityEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    protected Block block;
    private boolean cancelled;

    public EntityInteractEvent(Entity entity, Block block) {
        super(entity);
        this.block = block;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public Block getBlock() {
        return this.block;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

