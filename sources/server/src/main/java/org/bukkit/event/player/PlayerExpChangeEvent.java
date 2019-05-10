/*
 * Akarin Forge
 */
package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerExpChangeEvent
extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private int exp;

    public PlayerExpChangeEvent(Player player, int expAmount) {
        super(player);
        this.exp = expAmount;
    }

    public int getAmount() {
        return this.exp;
    }

    public void setAmount(int amount) {
        this.exp = amount;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

