/*
 * Akarin Forge
 */
package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Directional;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Redstone;

public class Hopper
extends MaterialData
implements Directional,
Redstone {
    protected static final BlockFace DEFAULT_DIRECTION = BlockFace.DOWN;
    protected static final boolean DEFAULT_ACTIVE = true;

    public Hopper() {
        this(DEFAULT_DIRECTION, true);
    }

    public Hopper(BlockFace facingDirection) {
        this(facingDirection, true);
    }

    public Hopper(BlockFace facingDirection, boolean isActive) {
        super(Material.HOPPER);
        this.setFacingDirection(facingDirection);
        this.setActive(isActive);
    }

    @Deprecated
    public Hopper(int type) {
        super(type);
    }

    public Hopper(Material type) {
        super(type);
    }

    @Deprecated
    public Hopper(int type, byte data) {
        super(type, data);
    }

    @Deprecated
    public Hopper(Material type, byte data) {
        super(type, data);
    }

    public void setActive(boolean isActive) {
        this.setData((byte)(this.getData() & 7 | (isActive ? 0 : 8)));
    }

    public boolean isActive() {
        return (this.getData() & 8) == 0;
    }

    @Override
    public void setFacingDirection(BlockFace face) {
        int data = this.getData() & 8;
        switch (face) {
            case DOWN: {
                data |= 0;
                break;
            }
            case NORTH: {
                data |= 2;
                break;
            }
            case SOUTH: {
                data |= 3;
                break;
            }
            case WEST: {
                data |= 4;
                break;
            }
            case EAST: {
                data |= 5;
            }
        }
        this.setData((byte)data);
    }

    @Override
    public BlockFace getFacing() {
        byte data = (byte)(this.getData() & 7);
        switch (data) {
            default: {
                return BlockFace.DOWN;
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
            case 5: 
        }
        return BlockFace.EAST;
    }

    @Override
    public String toString() {
        return super.toString() + " facing " + (Object)((Object)this.getFacing());
    }

    @Override
    public Hopper clone() {
        return (Hopper)super.clone();
    }

    @Override
    public boolean isPowered() {
        return (this.getData() & 8) != 0;
    }

}

