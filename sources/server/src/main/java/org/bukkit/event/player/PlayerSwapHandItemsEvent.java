/*
 * Akarin Forge
 */
package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerSwapHandItemsEvent
extends PlayerEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private ItemStack mainHandItem;
    private ItemStack offHandItem;
    private boolean cancelled;

    public PlayerSwapHandItemsEvent(Player player, ItemStack mainHandItem, ItemStack offHandItem) {
        super(player);
        this.mainHandItem = mainHandItem;
        this.offHandItem = offHandItem;
    }

    public ItemStack getMainHandItem() {
        return this.mainHandItem;
    }

    public void setMainHandItem(ItemStack mainHandItem) {
        this.mainHandItem = mainHandItem;
    }

    public ItemStack getOffHandItem() {
        return this.offHandItem;
    }

    public void setOffHandItem(ItemStack offHandItem) {
        this.offHandItem = offHandItem;
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

