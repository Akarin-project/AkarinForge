/*
 * Akarin Forge
 */
package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.MainHand;

public class PlayerChangedMainHandEvent
extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final MainHand mainHand;

    public PlayerChangedMainHandEvent(Player who, MainHand mainHand) {
        super(who);
        this.mainHand = mainHand;
    }

    public MainHand getMainHand() {
        return this.mainHand;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

