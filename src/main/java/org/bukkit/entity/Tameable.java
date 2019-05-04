/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;

public interface Tameable
extends Entity {
    public boolean isTamed();

    public void setTamed(boolean var1);

    public AnimalTamer getOwner();

    public void setOwner(AnimalTamer var1);
}

