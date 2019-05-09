/*
 * Akarin Forge
 */
package org.bukkit.event.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;

public class BlockPhysicsEvent
extends BlockEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final int changed;
    private boolean cancel = false;

    @Deprecated
    public BlockPhysicsEvent(Block block, int changed) {
        super(block);
        this.changed = changed;
    }

    @Deprecated
    public int getChangedTypeId() {
        return this.changed;
    }

    public Material getChangedType() {
        return Material.getBlockMaterial(this.changed);
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

