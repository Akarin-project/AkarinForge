/*
 * Akarin Forge
 */
package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChannelEvent;

public class PlayerUnregisterChannelEvent
extends PlayerChannelEvent {
    public PlayerUnregisterChannelEvent(Player player, String channel) {
        super(player, channel);
    }
}

