/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.util;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

public class BlockStateListPopulator {
    private final World world;
    private final List<BlockState> list;

    public BlockStateListPopulator(World world) {
        this(world, new ArrayList<BlockState>());
    }

    public BlockStateListPopulator(World world, List<BlockState> list) {
        this.world = world;
        this.list = list;
    }

    public void setTypeAndData(int x2, int y2, int z2, aow block, int data, int light) {
        BlockState state = this.world.getBlockAt(x2, y2, z2).getState();
        state.setTypeId(aow.a(block));
        state.setRawData((byte)data);
        this.list.add(state);
    }

    public void setTypeId(int x2, int y2, int z2, int type) {
        BlockState state = this.world.getBlockAt(x2, y2, z2).getState();
        state.setTypeId(type);
        this.list.add(state);
    }

    public void setTypeUpdate(int x2, int y2, int z2, aow block) {
        this.setType(x2, y2, z2, block);
    }

    public void setTypeUpdate(et position, awt data) {
        this.setTypeAndData(position.p(), position.q(), position.r(), data.u(), data.u().e(data), 0);
    }

    public void setType(int x2, int y2, int z2, aow block) {
        BlockState state = this.world.getBlockAt(x2, y2, z2).getState();
        state.setTypeId(aow.a(block));
        this.list.add(state);
    }

    public void updateList() {
        for (BlockState state : this.list) {
            state.update(true);
        }
    }

    public List<BlockState> getList() {
        return this.list;
    }

    public World getWorld() {
        return this.world;
    }
}

