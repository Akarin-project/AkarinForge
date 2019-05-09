/*
 * Akarin Forge
 */
package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;

public class BlockFromToEvent
extends BlockEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    protected Block to;
    protected BlockFace face;
    protected boolean cancel;

    public BlockFromToEvent(Block block, BlockFace face) {
        super(block);
        this.face = face;
        this.cancel = false;
    }

    public BlockFromToEvent(Block block, Block toBlock) {
        super(block);
        this.to = toBlock;
        this.face = BlockFace.SELF;
        this.cancel = false;
    }

    public BlockFace getFace() {
        return this.face;
    }

    public Block getToBlock() {
        if (this.to == null) {
            this.to = this.block.getRelative(this.face);
        }
        return this.to;
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
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

