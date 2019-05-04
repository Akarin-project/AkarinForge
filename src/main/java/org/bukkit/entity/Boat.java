/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.TreeSpecies;
import org.bukkit.entity.Vehicle;

public interface Boat
extends Vehicle {
    public TreeSpecies getWoodType();

    public void setWoodType(TreeSpecies var1);

    @Deprecated
    public double getMaxSpeed();

    @Deprecated
    public void setMaxSpeed(double var1);

    @Deprecated
    public double getOccupiedDeceleration();

    @Deprecated
    public void setOccupiedDeceleration(double var1);

    @Deprecated
    public double getUnoccupiedDeceleration();

    @Deprecated
    public void setUnoccupiedDeceleration(double var1);

    @Deprecated
    public boolean getWorkOnLand();

    @Deprecated
    public void setWorkOnLand(boolean var1);
}

