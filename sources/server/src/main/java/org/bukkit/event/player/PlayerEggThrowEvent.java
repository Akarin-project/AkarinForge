/*
 * Akarin Forge
 */
package org.bukkit.event.player;

import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerEggThrowEvent
extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Egg egg;
    private boolean hatching;
    private EntityType hatchType;
    private byte numHatches;

    public PlayerEggThrowEvent(Player player, Egg egg, boolean hatching, byte numHatches, EntityType hatchingType) {
        super(player);
        this.egg = egg;
        this.hatching = hatching;
        this.numHatches = numHatches;
        this.hatchType = hatchingType;
    }

    public Egg getEgg() {
        return this.egg;
    }

    public boolean isHatching() {
        return this.hatching;
    }

    public void setHatching(boolean hatching) {
        this.hatching = hatching;
    }

    public EntityType getHatchingType() {
        return this.hatchType;
    }

    public void setHatchingType(EntityType hatchType) {
        if (!hatchType.isSpawnable()) {
            throw new IllegalArgumentException("Can't spawn that entity type from an egg!");
        }
        this.hatchType = hatchType;
    }

    public byte getNumHatches() {
        return this.numHatches;
    }

    public void setNumHatches(byte numHatches) {
        this.numHatches = numHatches;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

