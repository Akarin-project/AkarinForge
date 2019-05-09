/*
 * Akarin Forge
 */
package org.bukkit.event.entity;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

public class EntityChangeBlockEvent
extends EntityEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Block block;
    private boolean cancel;
    private final Material to;
    private final byte data;

    @Deprecated
    public EntityChangeBlockEvent(Entity what, Block block, Material to2, byte data) {
        super(what);
        this.block = block;
        this.cancel = false;
        this.to = to2;
        this.data = data;
    }

    public Block getBlock() {
        return this.block;
    }

    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public Material getTo() {
        return this.to;
    }

    @Deprecated
    public byte getData() {
        return this.data;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

