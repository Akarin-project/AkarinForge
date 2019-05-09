/*
 * Akarin Forge
 */
package org.bukkit.event.entity;

import org.bukkit.Location;
import org.bukkit.TravelAgent;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityTeleportEvent;

public class EntityPortalEvent
extends EntityTeleportEvent {
    private static final HandlerList handlers = new HandlerList();
    protected boolean useTravelAgent = true;
    protected TravelAgent travelAgent;

    public EntityPortalEvent(Entity entity, Location from, Location to2, TravelAgent pta) {
        super(entity, from, to2);
        this.travelAgent = pta;
    }

    public void useTravelAgent(boolean useTravelAgent) {
        this.useTravelAgent = useTravelAgent;
    }

    public boolean useTravelAgent() {
        return this.useTravelAgent;
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

