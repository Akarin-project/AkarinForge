/*
 * Akarin Forge
 */
package org.bukkit.material;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.material.MaterialData;

public class Vine
extends MaterialData {
    private static final int VINE_NORTH = 4;
    private static final int VINE_EAST = 8;
    private static final int VINE_WEST = 2;
    private static final int VINE_SOUTH = 1;
    private static final EnumSet<BlockFace> possibleFaces = EnumSet.of(BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST);

    public Vine() {
        super(Material.VINE);
    }

    @Deprecated
    public Vine(int type, byte data) {
        super(type, data);
    }

    @Deprecated
    public Vine(byte data) {
        super(Material.VINE, data);
    }

    public /* varargs */ Vine(BlockFace ... faces) {
        this(EnumSet.copyOf(Arrays.asList(faces)));
    }

    public Vine(EnumSet<BlockFace> faces) {
        this((byte) 0);
        faces.retainAll(possibleFaces);
        byte data = 0;
        if (faces.contains((Object)BlockFace.WEST)) {
            data = (byte)(data | 2);
        }
        if (faces.contains((Object)BlockFace.NORTH)) {
            data = (byte)(data | 4);
        }
        if (faces.contains((Object)BlockFace.SOUTH)) {
            data = (byte)(data | 1);
        }
        if (faces.contains((Object)BlockFace.EAST)) {
            data = (byte)(data | 8);
        }
        this.setData(data);
    }

    public boolean isOnFace(BlockFace face) {
        switch (face) {
            case WEST: {
                return (this.getData() & 2) == 2;
            }
            case NORTH: {
                return (this.getData() & 4) == 4;
            }
            case SOUTH: {
                return (this.getData() & 1) == 1;
            }
            case EAST: {
                return (this.getData() & 8) == 8;
            }
            case NORTH_EAST: {
                return this.isOnFace(BlockFace.EAST) && this.isOnFace(BlockFace.NORTH);
            }
            case NORTH_WEST: {
                return this.isOnFace(BlockFace.WEST) && this.isOnFace(BlockFace.NORTH);
            }
            case SOUTH_EAST: {
                return this.isOnFace(BlockFace.EAST) && this.isOnFace(BlockFace.SOUTH);
            }
            case SOUTH_WEST: {
                return this.isOnFace(BlockFace.WEST) && this.isOnFace(BlockFace.SOUTH);
            }
            case UP: {
                return true;
            }
        }
        return false;
    }

    public void putOnFace(BlockFace face) {
        switch (face) {
            case WEST: {
                this.setData((byte)(this.getData() | 2));
                break;
            }
            case NORTH: {
                this.setData((byte)(this.getData() | 4));
                break;
            }
            case SOUTH: {
                this.setData((byte)(this.getData() | 1));
                break;
            }
            case EAST: {
                this.setData((byte)(this.getData() | 8));
                break;
            }
            case NORTH_WEST: {
                this.putOnFace(BlockFace.WEST);
                this.putOnFace(BlockFace.NORTH);
                break;
            }
            case SOUTH_WEST: {
                this.putOnFace(BlockFace.WEST);
                this.putOnFace(BlockFace.SOUTH);
                break;
            }
            case NORTH_EAST: {
                this.putOnFace(BlockFace.EAST);
                this.putOnFace(BlockFace.NORTH);
                break;
            }
            case SOUTH_EAST: {
                this.putOnFace(BlockFace.EAST);
                this.putOnFace(BlockFace.SOUTH);
                break;
            }
            case UP: {
                break;
            }
            default: {
                throw new IllegalArgumentException("Vines can't go on face " + face.toString());
            }
        }
    }

    public void removeFromFace(BlockFace face) {
        switch (face) {
            case WEST: {
                this.setData((byte)(this.getData() & -3));
                break;
            }
            case NORTH: {
                this.setData((byte)(this.getData() & -5));
                break;
            }
            case SOUTH: {
                this.setData((byte)(this.getData() & -2));
                break;
            }
            case EAST: {
                this.setData((byte)(this.getData() & -9));
                break;
            }
            case NORTH_WEST: {
                this.removeFromFace(BlockFace.WEST);
                this.removeFromFace(BlockFace.NORTH);
                break;
            }
            case SOUTH_WEST: {
                this.removeFromFace(BlockFace.WEST);
                this.removeFromFace(BlockFace.SOUTH);
                break;
            }
            case NORTH_EAST: {
                this.removeFromFace(BlockFace.EAST);
                this.removeFromFace(BlockFace.NORTH);
                break;
            }
            case SOUTH_EAST: {
                this.removeFromFace(BlockFace.EAST);
                this.removeFromFace(BlockFace.SOUTH);
                break;
            }
            case UP: {
                break;
            }
            default: {
                throw new IllegalArgumentException("Vines can't go on face " + face.toString());
            }
        }
    }

    @Override
    public String toString() {
        return "VINE";
    }

    @Override
    public Vine clone() {
        return (Vine)super.clone();
    }

}

