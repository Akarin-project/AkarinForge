/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.Validate
 */
package org.bukkit.event.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.ProjectileHitEvent;

public class PotionSplashEvent
extends ProjectileHitEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final Map<LivingEntity, Double> affectedEntities;

    public PotionSplashEvent(ThrownPotion potion, Map<LivingEntity, Double> affectedEntities) {
        super(potion);
        this.affectedEntities = affectedEntities;
    }

    @Override
    public ThrownPotion getEntity() {
        return (ThrownPotion)this.entity;
    }

    public ThrownPotion getPotion() {
        return this.getEntity();
    }

    public Collection<LivingEntity> getAffectedEntities() {
        return new ArrayList<LivingEntity>(this.affectedEntities.keySet());
    }

    public double getIntensity(LivingEntity entity) {
        Double intensity = this.affectedEntities.get(entity);
        return intensity != null ? intensity : 0.0;
    }

    public void setIntensity(LivingEntity entity, double intensity) {
        Validate.notNull((Object)entity, (String)"You must specify a valid entity.");
        if (intensity <= 0.0) {
            this.affectedEntities.remove(entity);
        } else {
            this.affectedEntities.put(entity, Math.min(intensity, 1.0));
        }
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

