/*
 * Akarin Forge
 */
package org.bukkit.util;

public class EulerAngle {
    public static final EulerAngle ZERO = new EulerAngle(0.0, 0.0, 0.0);
    private final double x;
    private final double y;
    private final double z;

    public EulerAngle(double x2, double y2, double z2) {
        this.x = x2;
        this.y = y2;
        this.z = z2;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public EulerAngle setX(double x2) {
        return new EulerAngle(x2, this.y, this.z);
    }

    public EulerAngle setY(double y2) {
        return new EulerAngle(this.x, y2, this.z);
    }

    public EulerAngle setZ(double z2) {
        return new EulerAngle(this.x, this.y, z2);
    }

    public EulerAngle add(double x2, double y2, double z2) {
        return new EulerAngle(this.x + x2, this.y + y2, this.z + z2);
    }

    public EulerAngle subtract(double x2, double y2, double z2) {
        return this.add(- x2, - y2, - z2);
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        EulerAngle that = (EulerAngle)o2;
        return Double.compare(that.x, this.x) == 0 && Double.compare(that.y, this.y) == 0 && Double.compare(that.z, this.z) == 0;
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits(this.x);
        int result = (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.y);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.z);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        return result;
    }
}

