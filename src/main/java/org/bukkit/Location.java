/*
 * Akarin Forge
 */
package org.bukkit;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

public class Location
implements Cloneable,
ConfigurationSerializable {
    private World world;
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;

    public Location(World world, double x2, double y2, double z2) {
        this(world, x2, y2, z2, 0.0f, 0.0f);
    }

    public Location(World world, double x2, double y2, double z2, float yaw, float pitch) {
        this.world = world;
        this.x = x2;
        this.y = y2;
        this.z = z2;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public World getWorld() {
        return this.world;
    }

    public Chunk getChunk() {
        return this.world.getChunkAt(this);
    }

    public Block getBlock() {
        return this.world.getBlockAt(this);
    }

    public void setX(double x2) {
        this.x = x2;
    }

    public double getX() {
        return this.x;
    }

    public int getBlockX() {
        return Location.locToBlock(this.x);
    }

    public void setY(double y2) {
        this.y = y2;
    }

    public double getY() {
        return this.y;
    }

    public int getBlockY() {
        return Location.locToBlock(this.y);
    }

    public void setZ(double z2) {
        this.z = z2;
    }

    public double getZ() {
        return this.z;
    }

    public int getBlockZ() {
        return Location.locToBlock(this.z);
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getPitch() {
        return this.pitch;
    }

    public Vector getDirection() {
        Vector vector = new Vector();
        double rotX = this.getYaw();
        double rotY = this.getPitch();
        vector.setY(- Math.sin(Math.toRadians(rotY)));
        double xz2 = Math.cos(Math.toRadians(rotY));
        vector.setX((- xz2) * Math.sin(Math.toRadians(rotX)));
        vector.setZ(xz2 * Math.cos(Math.toRadians(rotX)));
        return vector;
    }

    public Location setDirection(Vector vector) {
        double _2PI = 6.283185307179586;
        double x2 = vector.getX();
        double z2 = vector.getZ();
        if (x2 == 0.0 && z2 == 0.0) {
            this.pitch = vector.getY() > 0.0 ? -90.0f : 90.0f;
            return this;
        }
        double theta = Math.atan2(- x2, z2);
        this.yaw = (float)Math.toDegrees((theta + 6.283185307179586) % 6.283185307179586);
        double x22 = NumberConversions.square(x2);
        double z22 = NumberConversions.square(z2);
        double xz2 = Math.sqrt(x22 + z22);
        this.pitch = (float)Math.toDegrees(Math.atan((- vector.getY()) / xz2));
        return this;
    }

    public Location add(Location vec) {
        if (vec == null || vec.getWorld() != this.getWorld()) {
            throw new IllegalArgumentException("Cannot add Locations of differing worlds");
        }
        this.x += vec.x;
        this.y += vec.y;
        this.z += vec.z;
        return this;
    }

    public Location add(Vector vec) {
        this.x += vec.getX();
        this.y += vec.getY();
        this.z += vec.getZ();
        return this;
    }

    public Location add(double x2, double y2, double z2) {
        this.x += x2;
        this.y += y2;
        this.z += z2;
        return this;
    }

    public Location subtract(Location vec) {
        if (vec == null || vec.getWorld() != this.getWorld()) {
            throw new IllegalArgumentException("Cannot add Locations of differing worlds");
        }
        this.x -= vec.x;
        this.y -= vec.y;
        this.z -= vec.z;
        return this;
    }

    public Location subtract(Vector vec) {
        this.x -= vec.getX();
        this.y -= vec.getY();
        this.z -= vec.getZ();
        return this;
    }

    public Location subtract(double x2, double y2, double z2) {
        this.x -= x2;
        this.y -= y2;
        this.z -= z2;
        return this;
    }

    public double length() {
        return Math.sqrt(NumberConversions.square(this.x) + NumberConversions.square(this.y) + NumberConversions.square(this.z));
    }

    public double lengthSquared() {
        return NumberConversions.square(this.x) + NumberConversions.square(this.y) + NumberConversions.square(this.z);
    }

    public double distance(Location o2) {
        return Math.sqrt(this.distanceSquared(o2));
    }

    public double distanceSquared(Location o2) {
        if (o2 == null) {
            throw new IllegalArgumentException("Cannot measure distance to a null location");
        }
        if (o2.getWorld() == null || this.getWorld() == null) {
            throw new IllegalArgumentException("Cannot measure distance to a null world");
        }
        if (o2.getWorld() != this.getWorld()) {
            throw new IllegalArgumentException("Cannot measure distance between " + this.getWorld().getName() + " and " + o2.getWorld().getName());
        }
        return NumberConversions.square(this.x - o2.x) + NumberConversions.square(this.y - o2.y) + NumberConversions.square(this.z - o2.z);
    }

    public Location multiply(double m2) {
        this.x *= m2;
        this.y *= m2;
        this.z *= m2;
        return this;
    }

    public Location zero() {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
        return this;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        Location other = (Location)obj;
        if (!(this.world == other.world || this.world != null && this.world.equals(other.world))) {
            return false;
        }
        if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x)) {
            return false;
        }
        if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y)) {
            return false;
        }
        if (Double.doubleToLongBits(this.z) != Double.doubleToLongBits(other.z)) {
            return false;
        }
        if (Float.floatToIntBits(this.pitch) != Float.floatToIntBits(other.pitch)) {
            return false;
        }
        if (Float.floatToIntBits(this.yaw) != Float.floatToIntBits(other.yaw)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + (this.world != null ? this.world.hashCode() : 0);
        hash = 19 * hash + (int)(Double.doubleToLongBits(this.x) ^ Double.doubleToLongBits(this.x) >>> 32);
        hash = 19 * hash + (int)(Double.doubleToLongBits(this.y) ^ Double.doubleToLongBits(this.y) >>> 32);
        hash = 19 * hash + (int)(Double.doubleToLongBits(this.z) ^ Double.doubleToLongBits(this.z) >>> 32);
        hash = 19 * hash + Float.floatToIntBits(this.pitch);
        hash = 19 * hash + Float.floatToIntBits(this.yaw);
        return hash;
    }

    public String toString() {
        return "Location{world=" + this.world + ",x=" + this.x + ",y=" + this.y + ",z=" + this.z + ",pitch=" + this.pitch + ",yaw=" + this.yaw + '}';
    }

    public Vector toVector() {
        return new Vector(this.x, this.y, this.z);
    }

    public Location clone() {
        try {
            return (Location)super.clone();
        }
        catch (CloneNotSupportedException e2) {
            throw new Error(e2);
        }
    }

    public void checkFinite() throws IllegalArgumentException {
        NumberConversions.checkFinite(this.x, "x not finite");
        NumberConversions.checkFinite(this.y, "y not finite");
        NumberConversions.checkFinite(this.z, "z not finite");
        NumberConversions.checkFinite(this.pitch, "pitch not finite");
        NumberConversions.checkFinite(this.yaw, "yaw not finite");
    }

    public static int locToBlock(double loc) {
        return NumberConversions.floor(loc);
    }

    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("world", this.world.getName());
        data.put("x", this.x);
        data.put("y", this.y);
        data.put("z", this.z);
        data.put("yaw", Float.valueOf(this.yaw));
        data.put("pitch", Float.valueOf(this.pitch));
        return data;
    }

    public static Location deserialize(Map<String, Object> args) {
        World world = Bukkit.getWorld((String)args.get("world"));
        if (world == null) {
            throw new IllegalArgumentException("unknown world");
        }
        return new Location(world, NumberConversions.toDouble(args.get("x")), NumberConversions.toDouble(args.get("y")), NumberConversions.toDouble(args.get("z")), NumberConversions.toFloat(args.get("yaw")), NumberConversions.toFloat(args.get("pitch")));
    }
}

