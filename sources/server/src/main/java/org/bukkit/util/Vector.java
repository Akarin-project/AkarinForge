/*
 * Akarin Forge
 */
package org.bukkit.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.util.BlockVector;
import org.bukkit.util.NumberConversions;

@SerializableAs(value="Vector")
public class Vector
implements Cloneable,
ConfigurationSerializable {
    private static final long serialVersionUID = -2657651106777219169L;
    private static Random random = new Random();
    private static final double epsilon = 1.0E-6;
    protected double x;
    protected double y;
    protected double z;

    public Vector() {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
    }

    public Vector(int x2, int y2, int z2) {
        this.x = x2;
        this.y = y2;
        this.z = z2;
    }

    public Vector(double x2, double y2, double z2) {
        this.x = x2;
        this.y = y2;
        this.z = z2;
    }

    public Vector(float x2, float y2, float z2) {
        this.x = x2;
        this.y = y2;
        this.z = z2;
    }

    public Vector add(Vector vec) {
        this.x += vec.x;
        this.y += vec.y;
        this.z += vec.z;
        return this;
    }

    public Vector subtract(Vector vec) {
        this.x -= vec.x;
        this.y -= vec.y;
        this.z -= vec.z;
        return this;
    }

    public Vector multiply(Vector vec) {
        this.x *= vec.x;
        this.y *= vec.y;
        this.z *= vec.z;
        return this;
    }

    public Vector divide(Vector vec) {
        this.x /= vec.x;
        this.y /= vec.y;
        this.z /= vec.z;
        return this;
    }

    public Vector copy(Vector vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
        return this;
    }

    public double length() {
        return Math.sqrt(NumberConversions.square(this.x) + NumberConversions.square(this.y) + NumberConversions.square(this.z));
    }

    public double lengthSquared() {
        return NumberConversions.square(this.x) + NumberConversions.square(this.y) + NumberConversions.square(this.z);
    }

    public double distance(Vector o2) {
        return Math.sqrt(NumberConversions.square(this.x - o2.x) + NumberConversions.square(this.y - o2.y) + NumberConversions.square(this.z - o2.z));
    }

    public double distanceSquared(Vector o2) {
        return NumberConversions.square(this.x - o2.x) + NumberConversions.square(this.y - o2.y) + NumberConversions.square(this.z - o2.z);
    }

    public float angle(Vector other) {
        double dot = this.dot(other) / (this.length() * other.length());
        return (float)Math.acos(dot);
    }

    public Vector midpoint(Vector other) {
        this.x = (this.x + other.x) / 2.0;
        this.y = (this.y + other.y) / 2.0;
        this.z = (this.z + other.z) / 2.0;
        return this;
    }

    public Vector getMidpoint(Vector other) {
        double x2 = (this.x + other.x) / 2.0;
        double y2 = (this.y + other.y) / 2.0;
        double z2 = (this.z + other.z) / 2.0;
        return new Vector(x2, y2, z2);
    }

    public Vector multiply(int m2) {
        this.x *= (double)m2;
        this.y *= (double)m2;
        this.z *= (double)m2;
        return this;
    }

    public Vector multiply(double m2) {
        this.x *= m2;
        this.y *= m2;
        this.z *= m2;
        return this;
    }

    public Vector multiply(float m2) {
        this.x *= (double)m2;
        this.y *= (double)m2;
        this.z *= (double)m2;
        return this;
    }

    public double dot(Vector other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    public Vector crossProduct(Vector o2) {
        double newX = this.y * o2.z - o2.y * this.z;
        double newY = this.z * o2.x - o2.z * this.x;
        double newZ = this.x * o2.y - o2.x * this.y;
        this.x = newX;
        this.y = newY;
        this.z = newZ;
        return this;
    }

    public Vector getCrossProduct(Vector o2) {
        double x2 = this.y * o2.z - o2.y * this.z;
        double y2 = this.z * o2.x - o2.z * this.x;
        double z2 = this.x * o2.y - o2.x * this.y;
        return new Vector(x2, y2, z2);
    }

    public Vector normalize() {
        double length = this.length();
        this.x /= length;
        this.y /= length;
        this.z /= length;
        return this;
    }

    public Vector zero() {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
        return this;
    }

    public boolean isInAABB(Vector min, Vector max) {
        return this.x >= min.x && this.x <= max.x && this.y >= min.y && this.y <= max.y && this.z >= min.z && this.z <= max.z;
    }

    public boolean isInSphere(Vector origin, double radius) {
        return NumberConversions.square(origin.x - this.x) + NumberConversions.square(origin.y - this.y) + NumberConversions.square(origin.z - this.z) <= NumberConversions.square(radius);
    }

    public double getX() {
        return this.x;
    }

    public int getBlockX() {
        return NumberConversions.floor(this.x);
    }

    public double getY() {
        return this.y;
    }

    public int getBlockY() {
        return NumberConversions.floor(this.y);
    }

    public double getZ() {
        return this.z;
    }

    public int getBlockZ() {
        return NumberConversions.floor(this.z);
    }

    public Vector setX(int x2) {
        this.x = x2;
        return this;
    }

    public Vector setX(double x2) {
        this.x = x2;
        return this;
    }

    public Vector setX(float x2) {
        this.x = x2;
        return this;
    }

    public Vector setY(int y2) {
        this.y = y2;
        return this;
    }

    public Vector setY(double y2) {
        this.y = y2;
        return this;
    }

    public Vector setY(float y2) {
        this.y = y2;
        return this;
    }

    public Vector setZ(int z2) {
        this.z = z2;
        return this;
    }

    public Vector setZ(double z2) {
        this.z = z2;
        return this;
    }

    public Vector setZ(float z2) {
        this.z = z2;
        return this;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Vector)) {
            return false;
        }
        Vector other = (Vector)obj;
        return Math.abs(this.x - other.x) < 1.0E-6 && Math.abs(this.y - other.y) < 1.0E-6 && Math.abs(this.z - other.z) < 1.0E-6 && this.getClass().equals(obj.getClass());
    }

    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (int)(Double.doubleToLongBits(this.x) ^ Double.doubleToLongBits(this.x) >>> 32);
        hash = 79 * hash + (int)(Double.doubleToLongBits(this.y) ^ Double.doubleToLongBits(this.y) >>> 32);
        hash = 79 * hash + (int)(Double.doubleToLongBits(this.z) ^ Double.doubleToLongBits(this.z) >>> 32);
        return hash;
    }

    public Vector clone() {
        try {
            return (Vector)super.clone();
        }
        catch (CloneNotSupportedException e2) {
            throw new Error(e2);
        }
    }

    public String toString() {
        return "" + this.x + "," + this.y + "," + this.z;
    }

    public Location toLocation(World world) {
        return new Location(world, this.x, this.y, this.z);
    }

    public Location toLocation(World world, float yaw, float pitch) {
        return new Location(world, this.x, this.y, this.z, yaw, pitch);
    }

    public BlockVector toBlockVector() {
        return new BlockVector(this.x, this.y, this.z);
    }

    public void checkFinite() throws IllegalArgumentException {
        NumberConversions.checkFinite(this.x, "x not finite");
        NumberConversions.checkFinite(this.y, "y not finite");
        NumberConversions.checkFinite(this.z, "z not finite");
    }

    public static double getEpsilon() {
        return 1.0E-6;
    }

    public static Vector getMinimum(Vector v1, Vector v2) {
        return new Vector(Math.min(v1.x, v2.x), Math.min(v1.y, v2.y), Math.min(v1.z, v2.z));
    }

    public static Vector getMaximum(Vector v1, Vector v2) {
        return new Vector(Math.max(v1.x, v2.x), Math.max(v1.y, v2.y), Math.max(v1.z, v2.z));
    }

    public static Vector getRandom() {
        return new Vector(random.nextDouble(), random.nextDouble(), random.nextDouble());
    }

    @Override
    public Map<String, Object> serialize() {
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("x", this.getX());
        result.put("y", this.getY());
        result.put("z", this.getZ());
        return result;
    }

    public static Vector deserialize(Map<String, Object> args) {
        double x2 = 0.0;
        double y2 = 0.0;
        double z2 = 0.0;
        if (args.containsKey("x")) {
            x2 = (Double)args.get("x");
        }
        if (args.containsKey("y")) {
            y2 = (Double)args.get("y");
        }
        if (args.containsKey("z")) {
            z2 = (Double)args.get("z");
        }
        return new Vector(x2, y2, z2);
    }
}

