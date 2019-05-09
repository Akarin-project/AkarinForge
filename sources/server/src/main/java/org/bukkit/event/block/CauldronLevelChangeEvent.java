/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 */
package org.bukkit.event.block;

import com.google.common.base.Preconditions;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;

public class CauldronLevelChangeEvent
extends BlockEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final Entity entity;
    private final ChangeReason reason;
    private final int oldLevel;
    private int newLevel;

    public CauldronLevelChangeEvent(Block block, Entity entity, ChangeReason reason, int oldLevel, int newLevel) {
        super(block);
        this.entity = entity;
        this.reason = reason;
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public ChangeReason getReason() {
        return this.reason;
    }

    public int getOldLevel() {
        return this.oldLevel;
    }

    public int getNewLevel() {
        return this.newLevel;
    }

    public void setNewLevel(int newLevel) {
        Preconditions.checkArgument((boolean)(0 <= newLevel && newLevel <= 3), (String)"Cauldron level out of bounds 0 <= %s <= 3", (int)newLevel);
        this.newLevel = newLevel;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public static enum ChangeReason {
        BUCKET_FILL,
        BUCKET_EMPTY,
        BOTTLE_FILL,
        BOTTLE_EMPTY,
        BANNER_WASH,
        ARMOR_WASH,
        EXTINGUISH,
        EVAPORATE,
        UNKNOWN;
        

        private ChangeReason() {
        }
    }

}

