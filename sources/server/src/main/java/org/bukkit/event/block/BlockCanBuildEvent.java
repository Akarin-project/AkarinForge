/*
 * Akarin Forge
 */
package org.bukkit.event.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;

public class BlockCanBuildEvent
extends BlockEvent {
    private static final HandlerList handlers = new HandlerList();
    protected boolean buildable;
    @Deprecated
    protected int material;

    @Deprecated
    public BlockCanBuildEvent(Block block, int id2, boolean canBuild) {
        super(block);
        this.buildable = canBuild;
        this.material = id2;
    }

    public boolean isBuildable() {
        return this.buildable;
    }

    public void setBuildable(boolean cancel) {
        this.buildable = cancel;
    }

    public Material getMaterial() {
        return Material.getBlockMaterial(this.material);
    }

    @Deprecated
    public int getMaterialId() {
        return this.material;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

