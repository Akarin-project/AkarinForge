/*
 * Akarin Forge
 */
package org.bukkit.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.EquipmentSlot;

public class PlayerInteractEntityEvent
extends PlayerEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    protected Entity clickedEntity;
    boolean cancelled = false;
    private EquipmentSlot hand;

    public PlayerInteractEntityEvent(Player who, Entity clickedEntity) {
        this(who, clickedEntity, EquipmentSlot.HAND);
    }

    public PlayerInteractEntityEvent(Player who, Entity clickedEntity, EquipmentSlot hand) {
        super(who);
        this.clickedEntity = clickedEntity;
        this.hand = hand;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public Entity getRightClicked() {
        return this.clickedEntity;
    }

    public EquipmentSlot getHand() {
        return this.hand;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

