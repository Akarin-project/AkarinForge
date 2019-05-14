/*
 * Akarin Forge
 */
package org.spigotmc.event.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerSpawnLocationEvent
extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private Location spawnLocation;

    public PlayerSpawnLocationEvent(Player who, Location spawnLocation) {
        super(who);
        this.spawnLocation = spawnLocation;
    }

    public Location getSpawnLocation() {
        return this.spawnLocation;
    }

    public void setSpawnLocation(Location location) {
        this.spawnLocation = location;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

