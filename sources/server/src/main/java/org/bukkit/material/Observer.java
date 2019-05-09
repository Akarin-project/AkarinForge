/*
 * Akarin Forge
 */
package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Directional;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Redstone;

public class Observer
extends MaterialData
implements Directional,
Redstone {
    public Observer() {
        super(Material.OBSERVER);
    }

    public Observer(BlockFace direction) {
        this();
        this.setFacingDirection(direction);
    }

    @Deprecated
    public Observer(int type) {
        super(type);
    }

    public Observer(Material type) {
        super(type);
    }

    @Deprecated
    public Observer(int type, byte data) {
        super(type, data);
    }

    @Deprecated
    public Observer(Material type, byte data) {
        super(type, data);
    }

    @Override
    public boolean isPowered() {
        return (this.getData() & 8) == 8;
    }

    @Override
    public void setFacingDirection(BlockFace face) {
        byte data = (byte)(this.getData() & 8);
        switch (face) {
            case DOWN: {
                data = (byte)(data | 0);
                break;
            }
            case UP: {
                data = (byte)(data | 1);
                break;
            }
            case SOUTH: {
                data = (byte)(data | 2);
                break;
            }
            case NORTH: {
                data = (byte)(data | 3);
                break;
            }
            case EAST: {
                data = (byte)(data | 4);
                break;
            }
            case WEST: {
                data = (byte)(data | 5);
            }
        }
        this.setData(data);
    }

    @Override
    public BlockFace getFacing() {
        int data = this.getData() & 7;
        switch (data) {
            case 0: {
                return BlockFace.DOWN;
            }
            case 1: {
                return BlockFace.UP;
            }
            case 2: {
                return BlockFace.SOUTH;
            }
            case 3: {
                return BlockFace.NORTH;
            }
            case 4: {
                return BlockFace.EAST;
            }
            case 5: {
                return BlockFace.WEST;
            }
        }
        throw new IllegalArgumentException("Illegal facing direction " + data);
    }

    @Override
    public String toString() {
        return super.toString() + " facing " + (Object)((Object)this.getFacing());
    }

    @Override
    public Observer clone() {
        return (Observer)super.clone();
    }

}

