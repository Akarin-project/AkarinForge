/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.Validate
 */
package org.bukkit.event.player;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerRespawnEvent
extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private Location respawnLocation;
    private final boolean isBedSpawn;

    public PlayerRespawnEvent(Player respawnPlayer, Location respawnLocation, boolean isBedSpawn) {
        super(respawnPlayer);
        this.respawnLocation = respawnLocation;
        this.isBedSpawn = isBedSpawn;
    }

    public Location getRespawnLocation() {
        return this.respawnLocation;
    }

    public void setRespawnLocation(Location respawnLocation) {
        Validate.notNull((Object)respawnLocation, (String)"Respawn location can not be null");
        Validate.notNull((Object)respawnLocation.getWorld(), (String)"Respawn world can not be null");
        this.respawnLocation = respawnLocation;
    }

    public boolean isBedSpawn() {
        return this.isBedSpawn;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

