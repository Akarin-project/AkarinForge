/*
 * Akarin Forge
 */
package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Directional;
import org.bukkit.material.MaterialData;

public class Skull
extends MaterialData
implements Directional {
    public Skull() {
        super(Material.SKULL);
    }

    public Skull(BlockFace direction) {
        this();
        this.setFacingDirection(direction);
    }

    @Deprecated
    public Skull(int type) {
        super(type);
    }

    public Skull(Material type) {
        super(type);
    }

    @Deprecated
    public Skull(int type, byte data) {
        super(type, data);
    }

    @Deprecated
    public Skull(Material type, byte data) {
        super(type, data);
    }

    @Override
    public void setFacingDirection(BlockFace face) {
        int data;
        switch (face) {
            default: {
                data = 1;
                break;
            }
            case NORTH: {
                data = 2;
                break;
            }
            case WEST: {
                data = 4;
                break;
            }
            case SOUTH: {
                data = 3;
                break;
            }
            case EAST: {
                data = 5;
            }
        }
        this.setData((byte)data);
    }

    @Override
    public BlockFace getFacing() {
        byte data = this.getData();
        switch (data) {
            default: {
                return BlockFace.SELF;
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
    public Skull clone() {
        return (Skull)super.clone();
    }

}

