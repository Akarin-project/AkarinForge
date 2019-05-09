/*
 * Akarin Forge
 */
package org.bukkit.event.player;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerItemConsumeEvent
extends PlayerEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean isCancelled = false;
    private ItemStack item;

    public PlayerItemConsumeEvent(Player player, ItemStack item) {
        super(player);
        this.item = item;
    }

    public ItemStack getItem() {
        return this.item.clone();
    }

    public void setItem(ItemStack item) {
        this.item = item == null ? new ItemStack(Material.AIR) : item;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

