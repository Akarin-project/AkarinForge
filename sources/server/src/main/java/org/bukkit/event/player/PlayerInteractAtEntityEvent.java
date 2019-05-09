/*
 * Akarin Forge
 */
package org.bukkit.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;

public class PlayerInteractAtEntityEvent
extends PlayerInteractEntityEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Vector position;

    public PlayerInteractAtEntityEvent(Player who, Entity clickedEntity, Vector position) {
        this(who, clickedEntity, position, EquipmentSlot.HAND);
    }

    public PlayerInteractAtEntityEvent(Player who, Entity clickedEntity, Vector position, EquipmentSlot hand) {
        super(who, clickedEntity, hand);
        this.position = position;
    }

    public Vector getClickedPosition() {
        return this.position.clone();
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

