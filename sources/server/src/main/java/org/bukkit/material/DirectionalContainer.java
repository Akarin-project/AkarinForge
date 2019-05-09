/*
 * Akarin Forge
 */
package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Directional;
import org.bukkit.material.MaterialData;

public class DirectionalContainer
extends MaterialData
implements Directional {
    @Deprecated
    public DirectionalContainer(int type) {
        super(type);
    }

    public DirectionalContainer(Material type) {
        super(type);
    }

    @Deprecated
    public DirectionalContainer(int type, byte data) {
        super(type, data);
    }

    @Deprecated
    public DirectionalContainer(Material type, byte data) {
        super(type, data);
    }

    @Override
    public void setFacingDirection(BlockFace face) {
        byte data;
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
            }
        }
        this.setData(data);
    }

    @Override
    public BlockFace getFacing() {
        byte data = this.getData();
        switch (data) {
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
    public String toString() {
        return super.toString() + " facing " + (Object)((Object)this.getFacing());
    }

    @Override
    public DirectionalContainer clone() {
        return (DirectionalContainer)super.clone();
    }

}

