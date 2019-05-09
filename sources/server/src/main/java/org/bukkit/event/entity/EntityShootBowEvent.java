/*
 * Akarin Forge
 */
package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.inventory.ItemStack;

public class EntityShootBowEvent
extends EntityEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final ItemStack bow;
    private Entity projectile;
    private final float force;
    private boolean cancelled;

    public EntityShootBowEvent(LivingEntity shooter, ItemStack bow, Projectile projectile, float force) {
        super(shooter);
        this.bow = bow;
        this.projectile = projectile;
        this.force = force;
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity)this.entity;
    }

    public ItemStack getBow() {
        return this.bow;
    }

    public Entity getProjectile() {
        return this.projectile;
    }

    public void setProjectile(Entity projectile) {
        this.projectile = projectile;
    }

    public float getForce() {
        return this.force;
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

