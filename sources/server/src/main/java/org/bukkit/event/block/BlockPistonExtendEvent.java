/*
 * Akarin Forge
 */
package org.bukkit.event.block;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockPistonEvent;

public class BlockPistonExtendEvent
extends BlockPistonEvent {
    private static final HandlerList handlers = new HandlerList();
    private final int length;
    private List<Block> blocks;

    @Deprecated
    public BlockPistonExtendEvent(Block block, int length, BlockFace direction) {
        super(block, direction);
        this.length = length;
    }

    public BlockPistonExtendEvent(Block block, List<Block> blocks, BlockFace direction) {
        super(block, direction);
        this.length = blocks.size();
        this.blocks = blocks;
    }

    @Deprecated
    public int getLength() {
        return this.length;
    }

    public List<Block> getBlocks() {
        if (this.blocks == null) {
            ArrayList<Block> tmp = new ArrayList<Block>();
            for (int i2 = 0; i2 < this.getLength(); ++i2) {
                tmp.add(this.block.getRelative(this.getDirection(), i2 + 1));
            }
            this.blocks = Collections.unmodifiableList(tmp);
        }
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

