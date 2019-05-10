/*
 * Akarin Forge
 */
package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.material.MaterialData;
import org.bukkit.material.SimpleAttachableMaterialData;

public class Ladder
extends SimpleAttachableMaterialData {
    public Ladder() {
        super(Material.LADDER);
    }

    @Deprecated
    public Ladder(int type) {
        super(type);
    }

    public Ladder(Material type) {
        super(type);
    }

    @Deprecated
    public Ladder(int type, byte data) {
        super(type, data);
    }

    @Deprecated
    public Ladder(Material type, byte data) {
        super(type, data);
    }

    @Override
    public BlockFace getAttachedFace() {
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

    @Override
    public void setFacingDirection(BlockFace face) {
        byte data = 0;
        switch (face) {
            case SOUTH: {
                data = 2;
                break;
            }
            case NORTH: {
                data = 3;
                break;
            }
            case EAST: {
                data = 4;
                break;
            }
            case WEST: {
                data = 5;
            }
        }
        this.setData(data);
    }

    @Override
    public Ladder clone() {
        return (Ladder)super.clone();
    }

}

