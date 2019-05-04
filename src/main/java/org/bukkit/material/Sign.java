/*
 * Akarin Forge
 */
package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Attachable;
import org.bukkit.material.MaterialData;

public class Sign
extends MaterialData
implements Attachable {
    public Sign() {
        super(Material.SIGN_POST);
    }

    @Deprecated
    public Sign(int type) {
        super(type);
    }

    public Sign(Material type) {
        super(type);
    }

    @Deprecated
    public Sign(int type, byte data) {
        super(type, data);
    }

    @Deprecated
    public Sign(Material type, byte data) {
        super(type, data);
    }

    public boolean isWallSign() {
        return this.getItemType() == Material.WALL_SIGN;
    }

    @Override
    public BlockFace getAttachedFace() {
        if (this.isWallSign()) {
            byte data = this.getData();
            switch (data) {
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
            return null;
        }
        return BlockFace.DOWN;
    }

    @Override
    public BlockFace getFacing() {
        byte data = this.getData();
        if (!this.isWallSign()) {
            switch (data) {
                case 0: {
                    return BlockFace.SOUTH;
                }
                case 1: {
                    return BlockFace.SOUTH_SOUTH_WEST;
                }
                case 2: {
                    return BlockFace.SOUTH_WEST;
                }
                case 3: {
                    return BlockFace.WEST_SOUTH_WEST;
                }
                case 4: {
                    return BlockFace.WEST;
                }
                case 5: {
                    return BlockFace.WEST_NORTH_WEST;
                }
                case 6: {
                    return BlockFace.NORTH_WEST;
                }
                case 7: {
                    return BlockFace.NORTH_NORTH_WEST;
                }
                case 8: {
                    return BlockFace.NORTH;
                }
                case 9: {
                    return BlockFace.NORTH_NORTH_EAST;
                }
                case 10: {
                    return BlockFace.NORTH_EAST;
                }
                case 11: {
                    return BlockFace.EAST_NORTH_EAST;
                }
                case 12: {
                    return BlockFace.EAST;
                }
                case 13: {
                    return BlockFace.EAST_SOUTH_EAST;
                }
                case 14: {
                    return BlockFace.SOUTH_EAST;
                }
                case 15: {
                    return BlockFace.SOUTH_SOUTH_EAST;
                }
            }
            return null;
        }
        return this.getAttachedFace().getOppositeFace();
    }

    @Override
    public void setFacingDirection(BlockFace face) {
        byte data;
        if (this.isWallSign()) {
            switch (face) {
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
                    break;
                }
            }
        } else {
            switch (face) {
                case SOUTH: {
                    data = 0;
                    break;
                }
                case SOUTH_SOUTH_WEST: {
                    data = 1;
                    break;
                }
                case SOUTH_WEST: {
                    data = 2;
                    break;
                }
                case WEST_SOUTH_WEST: {
                    data = 3;
                    break;
                }
                case WEST: {
                    data = 4;
                    break;
                }
                case WEST_NORTH_WEST: {
                    data = 5;
                    break;
                }
                case NORTH_WEST: {
                    data = 6;
                    break;
                }
                case NORTH_NORTH_WEST: {
                    data = 7;
                    break;
                }
                case NORTH: {
                    data = 8;
                    break;
                }
                case NORTH_NORTH_EAST: {
                    data = 9;
                    break;
                }
                case NORTH_EAST: {
                    data = 10;
                    break;
                }
                case EAST_NORTH_EAST: {
                    data = 11;
                    break;
                }
                case EAST: {
                    data = 12;
                    break;
                }
                case EAST_SOUTH_EAST: {
                    data = 13;
                    break;
                }
                case SOUTH_SOUTH_EAST: {
                    data = 15;
                    break;
                }
                default: {
                    data = 14;
                }
            }
        }
        this.setData(data);
    }

    @Override
    public String toString() {
        return super.toString() + " facing " + (Object)((Object)this.getFacing());
    }

    @Override
    public Sign clone() {
        return (Sign)super.clone();
    }

}

