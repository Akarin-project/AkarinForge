/*
 * Akarin Forge
 */
package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Attachable;
import org.bukkit.material.Directional;
import org.bukkit.material.MaterialData;

public class CocoaPlant
extends MaterialData
implements Directional,
Attachable {
    public CocoaPlant() {
        super(Material.COCOA);
    }

    @Deprecated
    public CocoaPlant(int type) {
        super(type);
    }

    @Deprecated
    public CocoaPlant(int type, byte data) {
        super(type, data);
    }

    public CocoaPlant(CocoaPlantSize sz2) {
        this();
        this.setSize(sz2);
    }

    public CocoaPlant(CocoaPlantSize sz2, BlockFace dir) {
        this();
        this.setSize(sz2);
        this.setFacingDirection(dir);
    }

    public CocoaPlantSize getSize() {
        switch (this.getData() & 12) {
            case 0: {
                return CocoaPlantSize.SMALL;
            }
            case 4: {
                return CocoaPlantSize.MEDIUM;
            }
        }
        return CocoaPlantSize.LARGE;
    }

    public void setSize(CocoaPlantSize sz2) {
        int dat = this.getData() & 3;
        switch (sz2) {
            case SMALL: {
                break;
            }
            case MEDIUM: {
                dat |= 4;
                break;
            }
            case LARGE: {
                dat |= 8;
            }
        }
        this.setData((byte)dat);
    }

    @Override
    public BlockFace getAttachedFace() {
        return this.getFacing().getOppositeFace();
    }

    @Override
    public void setFacingDirection(BlockFace face) {
        int dat = this.getData() & 12;
        switch (face) {
            default: {
                break;
            }
            case WEST: {
                dat |= 1;
                break;
            }
            case NORTH: {
                dat |= 2;
                break;
            }
            case EAST: {
                dat |= 3;
            }
        }
        this.setData((byte)dat);
    }

    @Override
    public BlockFace getFacing() {
        switch (this.getData() & 3) {
            case 0: {
                return BlockFace.SOUTH;
            }
            case 1: {
                return BlockFace.WEST;
            }
            case 2: {
                return BlockFace.NORTH;
            }
            case 3: {
                return BlockFace.EAST;
            }
        }
        return null;
    }

    @Override
    public CocoaPlant clone() {
        return (CocoaPlant)super.clone();
    }

    @Override
    public String toString() {
        return super.toString() + " facing " + (Object)((Object)this.getFacing()) + " " + (Object)((Object)this.getSize());
    }

    public static enum CocoaPlantSize {
        SMALL,
        MEDIUM,
        LARGE;
        

        private CocoaPlantSize() {
        }
    }

}

