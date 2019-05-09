/*
 * Akarin Forge
 */
package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.block.BlockExpEvent;

public class BlockBreakEvent
extends BlockExpEvent
implements Cancellable {
    private final Player player;
    private boolean dropItems;
    private boolean cancel;

    public BlockBreakEvent(Block theBlock, Player player) {
        super(theBlock, 0);
        this.player = player;
        this.dropItems = true;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setDropItems(boolean dropItems) {
        this.dropItems = dropItems;
    }

    public boolean isDropItems() {
        return this.dropItems;
    }

    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}

