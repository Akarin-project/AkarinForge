/*
 * Akarin Forge
 */
package org.bukkit.util;

import java.util.Map;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.util.Vector;

@SerializableAs(value="BlockVector")
public class BlockVector
extends Vector {
    public BlockVector() {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
    }

    public BlockVector(Vector vec) {
        this.x = vec.getX();
        this.y = vec.getY();
        this.z = vec.getZ();
    }

    public BlockVector(int x2, int y2, int z2) {
        this.x = x2;
        this.y = y2;
        this.z = z2;
    }

    public BlockVector(double x2, double y2, double z2) {
        this.x = x2;
        this.y = y2;
        this.z = z2;
    }

    public BlockVector(float x2, float y2, float z2) {
        this.x = x2;
        this.y = y2;
        this.z = z2;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BlockVector)) {
            return false;
        }
        BlockVector other = (BlockVector)obj;
        return (int)other.getX() == (int)this.x && (int)other.getY() == (int)this.y && (int)other.getZ() == (int)this.z;
    }

    @Override
    public int hashCode() {
        return Integer.valueOf((int)this.x).hashCode() >> 13 ^ Integer.valueOf((int)this.y).hashCode() >> 7 ^ Integer.valueOf((int)this.z).hashCode();
    }

    @Override
    public BlockVector clone() {
        return (BlockVector)super.clone();
    }

    public static BlockVector deserialize(Map<String, Object> args) {
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
        return new BlockVector(x2, y2, z2);
    }
}

