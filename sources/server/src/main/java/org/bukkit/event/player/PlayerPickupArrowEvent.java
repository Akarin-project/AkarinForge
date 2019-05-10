/*
 * Akarin Forge
 */
package org.bukkit.event.player;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PlayerPickupArrowEvent
extends PlayerPickupItemEvent {
    private final Arrow arrow;

    public PlayerPickupArrowEvent(Player player, Item item, Arrow arrow) {
        super(player, item, 0);
        this.arrow = arrow;
    }

    public Arrow getArrow() {
        return this.arrow;
    }
}

