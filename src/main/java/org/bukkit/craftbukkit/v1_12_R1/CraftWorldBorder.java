/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 */
package org.bukkit.craftbukkit.v1_12_R1;

import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;

public class CraftWorldBorder
implements WorldBorder {
    private final World world;
    private final axn handle;

    public CraftWorldBorder(CraftWorld world) {
        this.world = world;
        this.handle = world.getHandle().al();
    }

    @Override
    public void reset() {
        this.setSize(6.0E7);
        this.setDamageAmount(0.2);
        this.setDamageBuffer(5.0);
        this.setWarningDistance(5);
        this.setWarningTime(15);
        this.setCenter(0.0, 0.0);
    }

    @Override
    public double getSize() {
        return this.handle.l();
    }

    @Override
    public void setSize(double newSize) {
        this.setSize(newSize, 0);
    }

    @Override
    public void setSize(double newSize, long time) {
        newSize = Math.min(6.0E7, Math.max(1.0, newSize));
        if ((time = Math.min(9223372036854775L, Math.max(0, time))) > 0) {
            this.handle.a(this.handle.l(), newSize, time * 1000);
        } else {
            this.handle.a(newSize);
        }
    }

    @Override
    public Location getCenter() {
        double x2 = this.handle.f();
        double z2 = this.handle.g();
        return new Location(this.world, x2, 0.0, z2);
    }

    @Override
    public void setCenter(double x2, double z2) {
        x2 = Math.min(3.0E7, Math.max(-3.0E7, x2));
        z2 = Math.min(3.0E7, Math.max(-3.0E7, z2));
        this.handle.c(x2, z2);
    }

    @Override
    public void setCenter(Location location) {
        this.setCenter(location.getX(), location.getZ());
    }

    @Override
    public double getDamageBuffer() {
        return this.handle.m();
    }

    @Override
    public void setDamageBuffer(double blocks) {
        this.handle.b(blocks);
    }

    @Override
    public double getDamageAmount() {
        return this.handle.n();
    }

    @Override
    public void setDamageAmount(double damage) {
        this.handle.c(damage);
    }

    @Override
    public int getWarningTime() {
        return this.handle.p();
    }

    @Override
    public void setWarningTime(int time) {
        this.handle.b(time);
    }

    @Override
    public int getWarningDistance() {
        return this.handle.q();
    }

    @Override
    public void setWarningDistance(int distance) {
        this.handle.c(distance);
    }

    @Override
    public boolean isInside(Location location) {
        Preconditions.checkArgument((boolean)(location != null), (Object)"location");
        return location.getWorld().equals(this.world) && this.handle.a(new et(location.getX(), location.getY(), location.getZ()));
    }
}

