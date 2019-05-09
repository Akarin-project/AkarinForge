/*
 * Akarin Forge
 */
package org.bukkit.event.player;

import org.bukkit.Location;
import org.bukkit.TravelAgent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerPortalEvent
extends PlayerTeleportEvent {
    private static final HandlerList handlers = new HandlerList();
    protected boolean useTravelAgent = true;
    protected TravelAgent travelAgent;

    public PlayerPortalEvent(Player player, Location from, Location to2, TravelAgent pta) {
        super(player, from, to2);
        this.travelAgent = pta;
    }

    public PlayerPortalEvent(Player player, Location from, Location to2, TravelAgent pta, PlayerTeleportEvent.TeleportCause cause) {
        super(player, from, to2, cause);
        this.travelAgent = pta;
    }

    public void useTravelAgent(boolean useTravelAgent) {
        this.useTravelAgent = useTravelAgent;
    }

    public boolean useTravelAgent() {
        return this.useTravelAgent && this.travelAgent != null;
    }

    public TravelAgent getPortalTravelAgent() {
        return this.travelAgent;
    }

    public void setPortalTravelAgent(TravelAgent travelAgent) {
        this.travelAgent = travelAgent;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

