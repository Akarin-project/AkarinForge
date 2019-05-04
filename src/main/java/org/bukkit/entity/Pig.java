/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.entity.Animals;
import org.bukkit.entity.Vehicle;

public interface Pig
extends Animals,
Vehicle {
    public boolean hasSaddle();

    public void setSaddle(boolean var1);
}

