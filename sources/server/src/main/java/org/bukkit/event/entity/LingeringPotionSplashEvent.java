/*
 * Akarin Forge
 */
package org.bukkit.event.entity;

import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LingeringPotion;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.ProjectileHitEvent;

public class LingeringPotionSplashEvent
extends ProjectileHitEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final AreaEffectCloud entity;

    public LingeringPotionSplashEvent(ThrownPotion potion, AreaEffectCloud entity) {
        super(potion);
        this.entity = entity;
    }

    @Override
    public LingeringPotion getEntity() {
        return (LingeringPotion)super.getEntity();
    }

    public AreaEffectCloud getAreaEffectCloud() {
        return this.entity;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

