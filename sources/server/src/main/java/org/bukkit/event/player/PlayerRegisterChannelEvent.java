/*
 * Akarin Forge
 */
package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChannelEvent;

public class PlayerRegisterChannelEvent
extends PlayerChannelEvent {
    public PlayerRegisterChannelEvent(Player player, String channel) {
        super(player, channel);
    }
}

