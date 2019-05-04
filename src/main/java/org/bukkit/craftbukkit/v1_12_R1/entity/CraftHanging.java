/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Hanging;

public class CraftHanging
extends CraftEntity
implements Hanging {
    public CraftHanging(CraftServer server, aca entity) {
        super(server, entity);
    }

    @Override
    public BlockFace getAttachedFace() {
        return this.getFacing().getOppositeFace();
    }

    @Override
    public void setFacingDirection(BlockFace face) {
        this.setFacingDirection(face, false);
    }

    @Override
    public boolean setFacingDirection(BlockFace face, boolean force) {
        aca hanging = this.getHandle();
        fa dir = hanging.b;
        switch (face) {
            default: {
                this.getHandle().a(fa.d);
                break;
            }
            case WEST: {
                this.getHandle().a(fa.e);
                break;
            }
            case NORTH: {
                this.getHandle().a(fa.c);
                break;
            }
            case EAST: {
                this.getHandle().a(fa.f);
            }
        }
        if (!force && !hanging.k()) {
            hanging.a(dir);
            return false;
        }
        return true;
    }

    @Override
    public BlockFace getFacing() {
        fa direction = this.getHandle().b;
        if (direction == null) {
            return BlockFace.SELF;
        }
        switch (direction) {
            default: {
                return BlockFace.SOUTH;
            }
            case e: {
                return BlockFace.WEST;
            }
            case c: {
                return BlockFace.NORTH;
            }
            case f: 
        }
        return BlockFace.EAST;
    }

    @Override
    public aca getHandle() {
        return (aca)this.entity;
    }

    @Override
    public String toString() {
        return "CraftHanging";
    }

    @Override
    public EntityType getType() {
        return EntityType.UNKNOWN;
    }

}

