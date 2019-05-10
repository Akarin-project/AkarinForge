/*
 * Akarin Forge
 */
package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Attachable;
import org.bukkit.material.MaterialData;

public class PistonExtensionMaterial
extends MaterialData
implements Attachable {
    @Deprecated
    public PistonExtensionMaterial(int type) {
        super(type);
    }

    public PistonExtensionMaterial(Material type) {
        super(type);
    }

    @Deprecated
    public PistonExtensionMaterial(int type, byte data) {
        super(type, data);
    }

    @Deprecated
    public PistonExtensionMaterial(Material type, byte data) {
        super(type, data);
    }

    @Override
    public void setFacingDirection(BlockFace face) {
        byte data = (byte)(this.getData() & 8);
        switch (face) {
            case UP: {
                data = (byte)(data | 1);
                break;
            }
            case NORTH: {
                data = (byte)(data | 2);
                break;
            }
            case SOUTH: {
                data = (byte)(data | 3);
                break;
            }
            case WEST: {
                data = (byte)(data | 4);
                break;
            }
            case EAST: {
                data = (byte)(data | 5);
            }
        }
        this.setData(data);
    }

    @Override
    public BlockFace getFacing() {
        byte dir = (byte)(this.getData() & 7);
        switch (dir) {
            case 0: {
                return BlockFace.DOWN;
            }
            case 1: {
                return BlockFace.UP;
            }
            case 2: {
                return BlockFace.NORTH;
            }
            case 3: {
                return BlockFace.SOUTH;
            }
            case 4: {
                return BlockFace.WEST;
            }
            case 5: {
                return BlockFace.EAST;
            }
        }
        return BlockFace.SELF;
    }

    public boolean isSticky() {
        return (this.getData() & 8) == 8;
    }

    public void setSticky(boolean sticky) {
        this.setData((byte)(sticky ? this.getData() | 8 : this.getData() & -9));
    }

    @Override
    public BlockFace getAttachedFace() {
        return this.getFacing().getOppositeFace();
    }

    @Override
    public PistonExtensionMaterial clone() {
        return (PistonExtensionMaterial)super.clone();
    }

}

