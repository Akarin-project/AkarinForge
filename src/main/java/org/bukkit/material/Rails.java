/*
 * Akarin Forge
 */
package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.material.MaterialData;

public class Rails
extends MaterialData {
    public Rails() {
        super(Material.RAILS);
    }

    @Deprecated
    public Rails(int type) {
        super(type);
    }

    public Rails(Material type) {
        super(type);
    }

    @Deprecated
    public Rails(int type, byte data) {
        super(type, data);
    }

    @Deprecated
    public Rails(Material type, byte data) {
        super(type, data);
    }

    public boolean isOnSlope() {
        byte d2 = this.getConvertedData();
        return d2 == 2 || d2 == 3 || d2 == 4 || d2 == 5;
    }

    public boolean isCurve() {
        byte d2 = this.getConvertedData();
        return d2 == 6 || d2 == 7 || d2 == 8 || d2 == 9;
    }

    public BlockFace getDirection() {
        byte d2 = this.getConvertedData();
        switch (d2) {
            default: {
                return BlockFace.SOUTH;
            }
            case 1: {
                return BlockFace.EAST;
            }
            case 2: {
                return BlockFace.EAST;
            }
            case 3: {
                return BlockFace.WEST;
            }
            case 4: {
                return BlockFace.NORTH;
            }
            case 5: {
                return BlockFace.SOUTH;
            }
            case 6: {
                return BlockFace.NORTH_WEST;
            }
            case 7: {
                return BlockFace.NORTH_EAST;
            }
            case 8: {
                return BlockFace.SOUTH_EAST;
            }
            case 9: 
        }
        return BlockFace.SOUTH_WEST;
    }

    @Override
    public String toString() {
        return super.toString() + " facing " + (Object)((Object)this.getDirection()) + (this.isCurve() ? " on a curve" : (this.isOnSlope() ? " on a slope" : ""));
    }

    @Deprecated
    protected byte getConvertedData() {
        return this.getData();
    }

    public void setDirection(BlockFace face, boolean isOnSlope) {
        switch (face) {
            case EAST: {
                this.setData((byte)(isOnSlope ? 2 : 1));
                break;
            }
            case WEST: {
                this.setData((byte)(isOnSlope ? 3 : 1));
                break;
            }
            case NORTH: {
                this.setData((byte)(isOnSlope ? 4 : 0));
                break;
            }
            case SOUTH: {
                this.setData((byte)(isOnSlope ? 5 : 0));
                break;
            }
            case NORTH_WEST: {
                this.setData(6);
                break;
            }
            case NORTH_EAST: {
                this.setData(7);
                break;
            }
            case SOUTH_EAST: {
                this.setData(8);
                break;
            }
            case SOUTH_WEST: {
                this.setData(9);
            }
        }
    }

    @Override
    public Rails clone() {
        return (Rails)super.clone();
    }

}

