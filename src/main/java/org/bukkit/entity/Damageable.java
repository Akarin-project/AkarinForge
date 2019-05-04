/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.entity.Entity;

public interface Damageable
extends Entity {
    public void damage(double var1);

    public void damage(double var1, Entity var3);

    public double getHealth();

    public void setHealth(double var1);

    @Deprecated
    public double getMaxHealth();

    @Deprecated
    public void setMaxHealth(double var1);

    @Deprecated
    public void resetMaxHealth();
}

