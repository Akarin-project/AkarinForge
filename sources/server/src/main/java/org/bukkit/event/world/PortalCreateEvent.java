/*
 * Akarin Forge
 */
package org.bukkit.event.world;

import java.util.ArrayList;
import java.util.Collection;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.WorldEvent;

public class PortalCreateEvent
extends WorldEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    private final ArrayList<Block> blocks = new ArrayList();
    private CreateReason reason = CreateReason.FIRE;

    public PortalCreateEvent(Collection<Block> blocks, World world, CreateReason reason) {
        super(world);
        this.blocks.addAll(blocks);
        this.reason = reason;
    }

    public ArrayList<Block> getBlocks() {
        return this.blocks;
    }

    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public CreateReason getReason() {
        return this.reason;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public static enum CreateReason {
        FIRE,
        OBC_DESTINATION;
        

        private CreateReason() {
        }
    }

}

