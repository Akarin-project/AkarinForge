/*
 * Akarin Forge
 */
package org.bukkit.event.entity;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

public class ProjectileHitEvent
extends EntityEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Entity hitEntity;
    private final Block hitBlock;

    public ProjectileHitEvent(Projectile projectile) {
        this(projectile, null, null);
    }

    public ProjectileHitEvent(Projectile projectile, Entity hitEntity) {
        this(projectile, hitEntity, null);
    }

    public ProjectileHitEvent(Projectile projectile, Block hitBlock) {
        this(projectile, null, hitBlock);
    }

    public ProjectileHitEvent(Projectile projectile, Entity hitEntity, Block hitBlock) {
        super(projectile);
        this.hitEntity = hitEntity;
        this.hitBlock = hitBlock;
    }

    @Override
    public Projectile getEntity() {
        return (Projectile)this.entity;
    }

    public Block getHitBlock() {
        return this.hitBlock;
    }

    public Entity getHitEntity() {
        return this.hitEntity;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

