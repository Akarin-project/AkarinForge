/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.DyeColor;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Sittable;
import org.bukkit.entity.Tameable;

public interface Wolf
extends Animals,
Tameable,
Sittable {
    public boolean isAngry();

    public void setAngry(boolean var1);

    public DyeColor getCollarColor();

    public void setCollarColor(DyeColor var1);
}

