/*
 * Akarin Forge
 */
package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Redstone;
import org.bukkit.material.SimpleAttachableMaterialData;

public class Lever
extends SimpleAttachableMaterialData
implements Redstone {
    public Lever() {
        super(Material.LEVER);
    }

    @Deprecated
    public Lever(int type) {
        super(type);
    }

    public Lever(Material type) {
        super(type);
    }

    @Deprecated
    public Lever(int type, byte data) {
        super(type, data);
    }

    @Deprecated
    public Lever(Material type, byte data) {
        super(type, data);
    }

    @Override
    public boolean isPowered() {
        return (this.getData() & 8) == 8;
    }

    public void setPowered(boolean isPowered) {
        this.setData((byte)(isPowered ? this.getData() | 8 : this.getData() & -9));
    }

    @Override
    public BlockFace getAttachedFace() {
        byte data = (byte)(this.getData() & 7);
        switch (data) {
            case 1: {
                return BlockFace.WEST;
            }
            case 2: {
                return BlockFace.EAST;
            }
            case 3: {
                return BlockFace.NORTH;
            }
            case 4: {
                return BlockFace.SOUTH;
            }
            case 5: 
            case 6: {
                return BlockFace.DOWN;
            }
            case 0: 
            case 7: {
                return BlockFace.UP;
            }
        }
        return null;
    }

    @Override
    public void setFacingDirection(BlockFace face) {
        byte data = (byte)(this.getData() & 8);
        BlockFace attach = this.getAttachedFace();
        if (attach == BlockFace.DOWN) {
            switch (face) {
                case SOUTH: 
                case NORTH: {
                    data = (byte)(data | 5);
                    break;
                }
                case EAST: 
                case WEST: {
                    data = (byte)(data | 6);
                }
            }
        } else if (attach == BlockFace.UP) {
            switch (face) {
                case SOUTH: 
                case NORTH: {
                    data = (byte)(data | 7);
                    break;
                }
                case EAST: 
                case WEST: {
                    data = (byte)(data | 0);
                }
            }
        } else {
            switch (face) {
                case EAST: {
                    data = (byte)(data | 1);
                    break;
                }
                case WEST: {
                    data = (byte)(data | 2);
                    break;
                }
                case SOUTH: {
                    data = (byte)(data | 3);
                    break;
                }
                case NORTH: {
                    data = (byte)(data | 4);
                }
            }
        }
        this.setData(data);
    }

    @Override
    public String toString() {
        return super.toString() + " facing " + (Object)((Object)this.getFacing()) + " " + (this.isPowered() ? "" : "NOT ") + "POWERED";
    }

    @Override
    public Lever clone() {
        return (Lever)super.clone();
    }

}

