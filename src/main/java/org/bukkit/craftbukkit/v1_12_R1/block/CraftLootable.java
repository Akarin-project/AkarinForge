/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.block;

import org.bukkit.Material;
import org.bukkit.Nameable;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftContainer;

public abstract class CraftLootable<T extends awa>
extends CraftContainer<T>
implements Nameable {
    public CraftLootable(Block block, Class<T> tileEntityClass) {
        super(block, tileEntityClass);
    }

    public CraftLootable(Material material, T tileEntity) {
        super(material, tileEntity);
    }

    @Override
    public String getCustomName() {
        awa lootable = (awa)this.getSnapshot();
        return lootable.n_() ? lootable.h_() : null;
    }

    @Override
    public void setCustomName(String name) {
        ((awa)this.getSnapshot()).a(name);
    }

    @Override
    public void applyTo(T lootable) {
        super.applyTo(lootable);
        if (!((awa)this.getSnapshot()).n_()) {
            lootable.a((String)null);
        }
    }
}

