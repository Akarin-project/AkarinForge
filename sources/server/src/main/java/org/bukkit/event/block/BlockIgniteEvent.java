/*
 * Akarin Forge
 */
package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;

public class BlockIgniteEvent
extends BlockEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final IgniteCause cause;
    private final Entity ignitingEntity;
    private final Block ignitingBlock;
    private boolean cancel;

    public BlockIgniteEvent(Block theBlock, IgniteCause cause, Entity ignitingEntity) {
        this(theBlock, cause, ignitingEntity, null);
    }

    public BlockIgniteEvent(Block theBlock, IgniteCause cause, Block ignitingBlock) {
        this(theBlock, cause, null, ignitingBlock);
    }

    public BlockIgniteEvent(Block theBlock, IgniteCause cause, Entity ignitingEntity, Block ignitingBlock) {
        super(theBlock);
        this.cause = cause;
        this.ignitingEntity = ignitingEntity;
        this.ignitingBlock = ignitingBlock;
        this.cancel = false;
    }

    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public IgniteCause getCause() {
        return this.cause;
    }

    public Player getPlayer() {
        if (this.ignitingEntity instanceof Player) {
            return (Player)this.ignitingEntity;
        }
        return null;
    }

    public Entity getIgnitingEntity() {
        return this.ignitingEntity;
    }

    public Block getIgnitingBlock() {
        return this.ignitingBlock;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public static enum IgniteCause {
        LAVA,
        FLINT_AND_STEEL,
        SPREAD,
        LIGHTNING,
        FIREBALL,
        ENDER_CRYSTAL,
        EXPLOSION;
        

        private IgniteCause() {
        }
    }

}

