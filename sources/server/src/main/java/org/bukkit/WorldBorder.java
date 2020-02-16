/*
 * Akarin Forge
 */
package org.bukkit;

import org.bukkit.Location;

public interface WorldBorder {
    public void reset();

    public double getSize();

    public void setSize(double var1);

    public void setSize(double var1, long var3);

    public Location getCenter();

    public void setCenter(double var1, double var3);

    public void setCenter(Location var1);

    public double getDamageBuffer();

    public void setDamageBuffer(double var1);

    public double getDamageAmount();

    public void setDamageAmount(double var1);

    public int getWarningTime();

    public void setWarningTime(int var1);

    public int getWarningDistance();

    public void setWarningDistance(int var1);

    public boolean isInside(Location var1);
}

