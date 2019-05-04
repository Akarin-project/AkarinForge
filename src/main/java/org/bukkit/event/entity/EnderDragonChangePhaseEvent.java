/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.Validate
 */
package org.bukkit.event.entity;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

public class EnderDragonChangePhaseEvent
extends EntityEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel;
    private final EnderDragon.Phase currentPhase;
    private EnderDragon.Phase newPhase;

    public EnderDragonChangePhaseEvent(EnderDragon enderDragon, EnderDragon.Phase currentPhase, EnderDragon.Phase newPhase) {
        super(enderDragon);
        this.currentPhase = currentPhase;
        this.setNewPhase(newPhase);
    }

    @Override
    public EnderDragon getEntity() {
        return (EnderDragon)this.entity;
    }

    public EnderDragon.Phase getCurrentPhase() {
        return this.currentPhase;
    }

    public EnderDragon.Phase getNewPhase() {
        return this.newPhase;
    }

    public void setNewPhase(EnderDragon.Phase newPhase) {
        Validate.notNull((Object)((Object)newPhase), (String)"New dragon phase cannot be null");
        this.newPhase = newPhase;
    }

    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

