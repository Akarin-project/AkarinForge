/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.entity.Projectile;

public interface FishHook
extends Projectile {
    @Deprecated
    public double getBiteChance();

    @Deprecated
    public void setBiteChance(double var1) throws IllegalArgumentException;
}

