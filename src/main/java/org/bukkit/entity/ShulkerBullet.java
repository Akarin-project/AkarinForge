/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;

public interface ShulkerBullet
extends Projectile {
    public Entity getTarget();

    public void setTarget(Entity var1);
}

