/*
 * Akarin Forge
 */
package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.material.DirectionalContainer;
import org.bukkit.material.FurnaceAndDispenser;
import org.bukkit.material.MaterialData;

public class Dispenser
extends FurnaceAndDispenser {
    public Dispenser() {
        super(Material.DISPENSER);
    }

    public Dispenser(BlockFace direction) {
        this();
        this.setFacingDirection(direction);
    }

    @Deprecated
    public Dispenser(int type) {
        super(type);
    }

    public Dispenser(Material type) {
        super(type);
    }

    @Deprecated
    public Dispenser(int type, byte data) {
        super(type, data);
    }

    @Deprecated
    public Dispenser(Material type, byte data) {
        super(type, data);
    }

    @Override
    public void setFacingDirection(BlockFace face) {
        byte data;
        switch (face) {
            case DOWN: {
                data = 0;
                break;
            }
            case UP: {
                data = 1;
                break;
            }
            case NORTH: {
                data = 2;
                break;
            }
            case SOUTH: {
                data = 3;
                break;
            }
            case WEST: {
                data = 4;
                break;
            }
            default: {
                data = 5;
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
                return BlockFace.NORTH;
            }
            case 3: {
                return BlockFace.SOUTH;
            }
            case 4: {
                return BlockFace.WEST;
            }
        }
        return BlockFace.EAST;
    }

    @Override
    public Dispenser clone() {
        return (Dispenser)super.clone();
    }

}

