/*
 * Akarin Forge
 */
package org.bukkit.event.player;

import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerAdvancementDoneEvent
extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Advancement advancement;

    public PlayerAdvancementDoneEvent(Player who, Advancement advancement) {
        super(who);
        this.advancement = advancement;
    }

    public Advancement getAdvancement() {
        return this.advancement;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

