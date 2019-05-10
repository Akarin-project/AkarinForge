/*
 * Akarin Forge
 */
package org.bukkit.event.weather;

import org.bukkit.World;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.weather.WeatherEvent;

public class ThunderChangeEvent
extends WeatherEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean canceled;
    private final boolean to;

    public ThunderChangeEvent(World world, boolean to2) {
        super(world);
        this.to = to2;
    }

    @Override
    public boolean isCancelled() {
        return this.canceled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.canceled = cancel;
    }

    public boolean toThunderState() {
        return this.to;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

