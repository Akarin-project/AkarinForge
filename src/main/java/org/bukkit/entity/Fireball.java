/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.entity.Explosive;
import org.bukkit.entity.Projectile;
import org.bukkit.util.Vector;

public interface Fireball
extends Projectile,
Explosive {
    public void setDirection(Vector var1);

    public Vector getDirection();
}

