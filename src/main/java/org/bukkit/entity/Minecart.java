/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.entity.Vehicle;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

public interface Minecart
extends Vehicle {
    public void setDamage(double var1);

    public double getDamage();

    public double getMaxSpeed();

    public void setMaxSpeed(double var1);

    public boolean isSlowWhenEmpty();

    public void setSlowWhenEmpty(boolean var1);

    public Vector getFlyingVelocityMod();

    public void setFlyingVelocityMod(Vector var1);

    public Vector getDerailedVelocityMod();

    public void setDerailedVelocityMod(Vector var1);

    public void setDisplayBlock(MaterialData var1);

    public MaterialData getDisplayBlock();

    public void setDisplayBlockOffset(int var1);

    public int getDisplayBlockOffset();
}

