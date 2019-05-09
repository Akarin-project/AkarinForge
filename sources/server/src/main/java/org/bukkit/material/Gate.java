/*
 * Akarin Forge
 */
package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Directional;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;

public class Gate
extends MaterialData
implements Directional,
Openable {
    private static final byte OPEN_BIT = 4;
    private static final byte DIR_BIT = 3;
    private static final byte GATE_SOUTH = 0;
    private static final byte GATE_WEST = 1;
    private static final byte GATE_NORTH = 2;
    private static final byte GATE_EAST = 3;

    public Gate() {
        super(Material.FENCE_GATE);
    }

    public Gate(int type, byte data) {
        super(type, data);
    }

    public Gate(byte data) {
        super(Material.FENCE_GATE, data);
    }

    @Override
    public void setFacingDirection(BlockFace face) {
        byte data = (byte)(this.getData() & -4);
        switch (face) {
            default: {
                data = (byte)(data | 0);
                break;
            }
            case SOUTH: {
                data = (byte)(data | 1);
                break;
            }
            case WEST: {
                data = (byte)(data | 2);
                break;
            }
            case NORTH: {
                data = (byte)(data | 3);
            }
        }
        this.setData(data);
    }

    @Override
    public BlockFace getFacing() {
        switch (this.getData() & 3) {
            case 0: {
                return BlockFace.EAST;
            }
            case 1: {
                return BlockFace.SOUTH;
            }
            case 2: {
                return BlockFace.WEST;
            }
            case 3: {
                return BlockFace.NORTH;
            }
        }
        return BlockFace.EAST;
    }

    @Override
    public boolean isOpen() {
        return (this.getData() & 4) > 0;
    }

    @Override
    public void setOpen(boolean isOpen) {
        byte data = this.getData();
        data = isOpen ? (byte)(data | 4) : (byte)(data & -5);
        this.setData(data);
    }

    @Override
    public String toString() {
        return (this.isOpen() ? "OPEN " : "CLOSED ") + " facing and opening " + (Object)((Object)this.getFacing());
    }

    @Override
    public Gate clone() {
        return (Gate)super.clone();
    }

}

