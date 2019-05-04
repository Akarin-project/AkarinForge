/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.util;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.BlockChangeDelegate;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.material.MaterialData;

public class StructureGrowDelegate
implements BlockChangeDelegate {
    private final CraftWorld world;
    private final List<BlockState> blocks = new ArrayList<BlockState>();

    public StructureGrowDelegate(amu world) {
        this.world = world.getWorld();
    }

    @Override
    public boolean setRawTypeId(int x2, int y2, int z2, int type) {
        return this.setRawTypeIdAndData(x2, y2, z2, type, 0);
    }

    @Override
    public boolean setRawTypeIdAndData(int x2, int y2, int z2, int type, int data) {
        BlockState state = this.world.getBlockAt(x2, y2, z2).getState();
        state.setTypeId(type);
        state.setData(new MaterialData(type, (byte)data));
        this.blocks.add(state);
        return true;
    }

    @Override
    public boolean setTypeId(int x2, int y2, int z2, int typeId) {
        return this.setRawTypeId(x2, y2, z2, typeId);
    }

    @Override
    public boolean setTypeIdAndData(int x2, int y2, int z2, int typeId, int data) {
        return this.setRawTypeIdAndData(x2, y2, z2, typeId, data);
    }

    @Override
    public int getTypeId(int x2, int y2, int z2) {
        for (BlockState state : this.blocks) {
            if (state.getX() != x2 || state.getY() != y2 || state.getZ() != z2) continue;
            return state.getTypeId();
        }
        return this.world.getBlockTypeIdAt(x2, y2, z2);
    }

    @Override
    public int getHeight() {
        return this.world.getMaxHeight();
    }

    public List<BlockState> getBlocks() {
        return this.blocks;
    }

    @Override
    public boolean isEmpty(int x2, int y2, int z2) {
        for (BlockState state : this.blocks) {
            if (state.getX() != x2 || state.getY() != y2 || state.getZ() != z2) continue;
            return aow.c(state.getTypeId()) == aox.a;
        }
        return this.world.getBlockAt(x2, y2, z2).isEmpty();
    }
}

