/*
 * Akarin Forge
 */
package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerItemBreakEvent
extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final ItemStack brokenItem;

    public PlayerItemBreakEvent(Player player, ItemStack brokenItem) {
        super(player);
        this.brokenItem = brokenItem;
    }

    public ItemStack getBrokenItem() {
        return this.brokenItem;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

