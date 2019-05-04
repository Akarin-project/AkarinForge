/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.entity.Animals;
import org.bukkit.material.Colorable;

public interface Sheep
extends Animals,
Colorable {
    public boolean isSheared();

    public void setSheared(boolean var1);
}

