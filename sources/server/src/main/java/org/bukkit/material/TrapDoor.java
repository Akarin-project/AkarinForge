/*
 * Akarin Forge
 */
package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;
import org.bukkit.material.SimpleAttachableMaterialData;

public class TrapDoor
extends SimpleAttachableMaterialData
implements Openable {
    public TrapDoor() {
        super(Material.TRAP_DOOR);
    }

    @Deprecated
    public TrapDoor(int type) {
        super(type);
    }

    public TrapDoor(Material type) {
        super(type);
    }

    @Deprecated
    public TrapDoor(int type, byte data) {
        super(type, data);
    }

    @Deprecated
    public TrapDoor(Material type, byte data) {
        super(type, data);
    }

    @Override
    public boolean isOpen() {
        return (this.getData() & 4) == 4;
    }

    @Override
    public void setOpen(boolean isOpen) {
        byte data = this.getData();
        data = isOpen ? (byte)(data | 4) : (byte)(data & -5);
        this.setData(data);
    }

    public boolean isInverted() {
        return (this.getData() & 8) != 0;
    }

    public void setInverted(boolean inv) {
        int dat = this.getData() & 7;
        if (inv) {
            dat |= 8;
        }
        this.setData((byte)dat);
    }

    @Override
    public BlockFace getAttachedFace() {
        byte data = (byte)(this.getData() & 3);
        switch (data) {
            case 0: {
                return BlockFace.SOUTH;
            }
            case 1: {
                return BlockFace.NORTH;
            }
            case 2: {
                return BlockFace.EAST;
            }
            case 3: {
                return BlockFace.WEST;
            }
        }
        return null;
    }

    @Override
    public void setFacingDirection(BlockFace face) {
        byte data = (byte)(this.getData() & 12);
        switch (face) {
            case SOUTH: {
                data = (byte)(data | 1);
                break;
            }
            case WEST: {
                data = (byte)(data | 2);
                break;
            }
            case EAST: {
                data = (byte)(data | 3);
            }
        }
        this.setData(data);
    }

    @Override
    public String toString() {
        return (this.isOpen() ? "OPEN " : "CLOSED ") + super.toString() + " with hinges set " + (Object)((Object)this.getAttachedFace()) + (this.isInverted() ? " inverted" : "");
    }

    @Override
    public TrapDoor clone() {
        return (TrapDoor)super.clone();
    }

}

