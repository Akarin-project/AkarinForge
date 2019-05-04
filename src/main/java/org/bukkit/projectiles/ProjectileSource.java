/*
 * Akarin Forge
 */
package org.bukkit.projectiles;

import org.bukkit.entity.Projectile;
import org.bukkit.util.Vector;

public interface ProjectileSource {
    public <T extends Projectile> T launchProjectile(Class<? extends T> var1);

    public <T extends Projectile> T launchProjectile(Class<? extends T> var1, Vector var2);
}

