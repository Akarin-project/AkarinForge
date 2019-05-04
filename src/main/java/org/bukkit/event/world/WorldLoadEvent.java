/*
 * Akarin Forge
 */
package org.bukkit.event.world;

import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.WorldEvent;

public class WorldLoadEvent
extends WorldEvent {
    private static final HandlerList handlers = new HandlerList();

    public WorldLoadEvent(World world) {
        super(world);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

