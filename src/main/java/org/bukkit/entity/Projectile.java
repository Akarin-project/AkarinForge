/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.entity.Entity;
import org.bukkit.projectiles.ProjectileSource;

public interface Projectile
extends Entity {
    public ProjectileSource getShooter();

    public void setShooter(ProjectileSource var1);

    public boolean doesBounce();

    public void setBounce(boolean var1);
}

