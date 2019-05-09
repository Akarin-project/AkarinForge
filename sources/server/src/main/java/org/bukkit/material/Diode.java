/*
 * Akarin Forge
 */
package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Directional;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Redstone;

public class Diode
extends MaterialData
implements Directional,
Redstone {
    protected static final BlockFace DEFAULT_DIRECTION = BlockFace.NORTH;
    protected static final int DEFAULT_DELAY = 1;
    protected static final boolean DEFAULT_STATE = false;

    public Diode() {
        this(DEFAULT_DIRECTION, 1, true);
    }

    public Diode(BlockFace facingDirection) {
        this(facingDirection, 1, false);
    }

    public Diode(BlockFace facingDirection, int delay) {
        this(facingDirection, delay, false);
    }

    public Diode(BlockFace facingDirection, int delay, boolean state) {
        super(state ? Material.DIODE_BLOCK_ON : Material.DIODE_BLOCK_OFF);
        this.setFacingDirection(facingDirection);
        this.setDelay(delay);
    }

    @Deprecated
    public Diode(int type) {
        super(type);
    }

    public Diode(Material type) {
        super(type);
    }

    @Deprecated
    public Diode(int type, byte data) {
        super(type, data);
    }

    @Deprecated
    public Diode(Material type, byte data) {
        super(type, data);
    }

    public void setDelay(int delay) {
        if (delay > 4) {
            delay = 4;
        }
        if (delay < 1) {
            delay = 1;
        }
        byte newData = (byte)(this.getData() & 3);
        this.setData((byte)(newData | delay - 1 << 2));
    }

    public int getDelay() {
        return (this.getData() >> 2) + 1;
    }

    @Override
    public void setFacingDirection(BlockFace face) {
        byte data;
        int delay = this.getDelay();
        switch (face) {
            case EAST: {
                data = 1;
                break;
            }
            case SOUTH: {
                data = 2;
                break;
            }
            case WEST: {
                data = 3;
                break;
            }
            default: {
                data = 0;
            }
        }
        this.setData(data);
        this.setDelay(delay);
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
        return super.toString() + " facing " + (Object)((Object)this.getFacing()) + " with " + this.getDelay() + " ticks delay";
    }

    @Override
    public Diode clone() {
        return (Diode)super.clone();
    }

    @Override
    public boolean isPowered() {
        return this.getItemType() == Material.DIODE_BLOCK_ON;
    }

}

