/*
 * Akarin Forge
 */
package org.bukkit.event.inventory;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.inventory.ItemStack;

public class FurnaceSmeltEvent
extends BlockEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final ItemStack source;
    private ItemStack result;
    private boolean cancelled;

    public FurnaceSmeltEvent(Block furnace, ItemStack source, ItemStack result) {
        super(furnace);
        this.source = source;
        this.result = result;
        this.cancelled = false;
    }

    public ItemStack getSource() {
        return this.source;
    }

    public ItemStack getResult() {
        return this.result;
    }

    public void setResult(ItemStack result) {
        this.result = result;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

