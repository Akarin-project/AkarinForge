/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.block;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBlockState;

public class CraftBlockEntityState<T extends avj>
extends CraftBlockState {
    private final Class<T> tileEntityClass;
    private final T tileEntity;
    private final T snapshot;

    public CraftBlockEntityState(Block block, Class<T> tileEntityClass) {
        super(block);
        this.tileEntityClass = tileEntityClass;
        CraftWorld world = (CraftWorld)this.getWorld();
        this.tileEntity = (avj)tileEntityClass.cast(world.getTileEntityAt(this.getX(), this.getY(), this.getZ()));
        this.snapshot = this.createSnapshot(this.tileEntity);
        this.load(this.snapshot);
    }

    public CraftBlockEntityState(Material material, T tileEntity) {
        super(material);
        this.tileEntityClass = tileEntity.getClass();
        this.tileEntity = tileEntity;
        this.snapshot = this.createSnapshot(tileEntity);
        this.load(this.snapshot);
    }

    private T createSnapshot(T tileEntity) {
        if (tileEntity == null) {
            return null;
        }
        fy nbtTagCompound = tileEntity.b(new fy());
        avj snapshot = avj.a(tileEntity.D(), nbtTagCompound);
        return (T)snapshot;
    }

    private void copyData(T from, T to2) {
        et pos = to2.w();
        fy nbtTagCompound = from.b(new fy());
        to2.a(nbtTagCompound);
        to2.a(pos);
    }

    public T getTileEntity() {
        return this.tileEntity;
    }

    protected T getSnapshot() {
        return this.snapshot;
    }

    protected avj getTileEntityFromWorld() {
        this.requirePlaced();
        return ((CraftWorld)this.getWorld()).getTileEntityAt(this.getX(), this.getY(), this.getZ());
    }

    public fy getSnapshotNBT() {
        this.applyTo(this.snapshot);
        return this.snapshot.b(new fy());
    }

    protected void load(T tileEntity) {
        if (tileEntity != null && tileEntity != this.snapshot) {
            this.copyData(tileEntity, this.snapshot);
        }
    }

    protected void applyTo(T tileEntity) {
        if (tileEntity != null && tileEntity != this.snapshot) {
            this.copyData(this.snapshot, tileEntity);
        }
    }

    protected boolean isApplicable(avj tileEntity) {
        return this.tileEntityClass.isInstance(tileEntity);
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        avj tile;
        boolean result = super.update(force, applyPhysics);
        if (result && this.isPlaced() && this.isApplicable(tile = this.getTileEntityFromWorld())) {
            this.applyTo((avj)this.tileEntityClass.cast(tile));
            tile.y_();
        }
        return result;
    }
}

