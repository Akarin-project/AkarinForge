/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 */
package org.bukkit.event.player;

import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerMoveEvent
extends PlayerEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    private Location from;
    private Location to;

    public PlayerMoveEvent(Player player, Location from, Location to2) {
        super(player);
        this.from = from;
        this.to = to2;
    }

    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public Location getFrom() {
        return this.from;
    }

    public void setFrom(Location from) {
        this.validateLocation(from);
        this.from = from;
    }

    public Location getTo() {
        return this.to;
    }

    public void setTo(Location to2) {
        this.validateLocation(to2);
        this.to = to2;
    }

    private void validateLocation(Location loc) {
        Preconditions.checkArgument((boolean)(loc != null), (Object)"Cannot use null location!");
        Preconditions.checkArgument((boolean)(loc.getWorld() != null), (Object)"Cannot use null location with null world!");
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

