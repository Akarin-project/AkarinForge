/*
 * Akarin Forge
 */
package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Directional;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Redstone;

public class Comparator
extends MaterialData
implements Directional,
Redstone {
    protected static final BlockFace DEFAULT_DIRECTION = BlockFace.NORTH;
    protected static final boolean DEFAULT_SUBTRACTION_MODE = false;
    protected static final boolean DEFAULT_STATE = false;

    public Comparator() {
        this(DEFAULT_DIRECTION, false, false);
    }

    public Comparator(BlockFace facingDirection) {
        this(facingDirection, false, false);
    }

    public Comparator(BlockFace facingDirection, boolean isSubtraction) {
        this(facingDirection, isSubtraction, false);
    }

    public Comparator(BlockFace facingDirection, boolean isSubtraction, boolean state) {
        super(state ? Material.REDSTONE_COMPARATOR_ON : Material.REDSTONE_COMPARATOR_OFF);
        this.setFacingDirection(facingDirection);
        this.setSubtractionMode(isSubtraction);
    }

    @Deprecated
    public Comparator(int type) {
        super(type);
    }

    public Comparator(Material type) {
        super(type);
    }

    @Deprecated
    public Comparator(int type, byte data) {
        super(type, data);
    }

    @Deprecated
    public Comparator(Material type, byte data) {
        super(type, data);
    }

    public void setSubtractionMode(boolean isSubtraction) {
        this.setData((byte)(this.getData() & 11 | (isSubtraction ? 4 : 0)));
    }

    public boolean isSubtractionMode() {
        return (this.getData() & 4) != 0;
    }

    @Override
    public void setFacingDirection(BlockFace face) {
        int data = this.getData() & 12;
        switch (face) {
            case EAST: {
                data |= 1;
                break;
            }
            case SOUTH: {
                data |= 2;
                break;
            }
            case WEST: {
                data |= 3;
                break;
            }
            default: {
                data |= 0;
            }
        }
        this.setData((byte)data);
    }

    @Override
    public BlockFace getFacing() {
        byte data = (byte)(this.getData() & 3);
        switch (data) {
            default: {
                return BlockFace.NORTH;
            }
            case 1: {
                return BlockFace.EAST;
            }
            case 2: {
                return BlockFace.SOUTH;
            }
            case 3: 
        }
        return BlockFace.WEST;
    }

    @Override
    public String toString() {
        return super.toString() + " facing " + (Object)((Object)this.getFacing()) + " in " + (this.isSubtractionMode() ? "subtraction" : "comparator") + " mode";
    }

    @Override
    public Comparator clone() {
        return (Comparator)super.clone();
    }

    @Override
    public boolean isPowered() {
        return this.getItemType() == Material.REDSTONE_COMPARATOR_ON;
    }

    public boolean isBeingPowered() {
        return (this.getData() & 8) != 0;
    }

}

