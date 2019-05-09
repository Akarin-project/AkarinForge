/*
 * Akarin Forge
 */
package org.bukkit.event.block;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockPistonEvent;

public class BlockPistonRetractEvent
extends BlockPistonEvent {
    private static final HandlerList handlers = new HandlerList();
    private List<Block> blocks;

    public BlockPistonRetractEvent(Block block, List<Block> blocks, BlockFace direction) {
        super(block, direction);
        this.blocks = blocks;
    }

    @Deprecated
    public Location getRetractLocation() {
        return this.getBlock().getRelative(this.getDirection(), 2).getLocation();
    }

    public List<Block> getBlocks() {
        return this.blocks;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

