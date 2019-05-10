/*
 * Akarin Forge
 */
package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public abstract class PlayerEvent
extends Event {
    protected Player player;

    public PlayerEvent(Player who) {
        this.player = who;
    }

    PlayerEvent(Player who, boolean async) {
        super(async);
        this.player = who;
    }

    public final Player getPlayer() {
        return this.player;
    }
}

