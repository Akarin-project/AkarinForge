/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBlockEntityState;

public abstract class CraftContainer<T extends avx>
extends CraftBlockEntityState<T>
implements Container {
    public CraftContainer(Block block, Class<T> tileEntityClass) {
        super(block, tileEntityClass);
    }

    public CraftContainer(Material material, T tileEntity) {
        super(material, tileEntity);
    }

    @Override
    public boolean isLocked() {
        return ((avx)this.getSnapshot()).A_();
    }

    @Override
    public String getLock() {
        return ((avx)this.getSnapshot()).j().b();
    }

    @Override
    public void setLock(String key) {
        ((avx)this.getSnapshot()).a(key == null ? ug.a : new ug(key));
    }
}

