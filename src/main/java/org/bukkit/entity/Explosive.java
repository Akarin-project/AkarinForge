/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.entity.Entity;

public interface Explosive
extends Entity {
    public void setYield(float var1);

    public float getYield();

    public void setIsIncendiary(boolean var1);

    public boolean isIncendiary();
}

