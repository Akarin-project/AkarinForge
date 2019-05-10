/*
 * Akarin Forge
 */
package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Redstone;
import org.bukkit.material.SimpleAttachableMaterialData;
import org.bukkit.material.Torch;

public class RedstoneTorch
extends Torch
implements Redstone {
    public RedstoneTorch() {
        super(Material.REDSTONE_TORCH_ON);
    }

    @Deprecated
    public RedstoneTorch(int type) {
        super(type);
    }

    public RedstoneTorch(Material type) {
        super(type);
    }

    @Deprecated
    public RedstoneTorch(int type, byte data) {
        super(type, data);
    }

    @Deprecated
    public RedstoneTorch(Material type, byte data) {
        super(type, data);
    }

    @Override
    public boolean isPowered() {
        return this.getItemType() == Material.REDSTONE_TORCH_ON;
    }

    @Override
    public String toString() {
        return super.toString() + " " + (this.isPowered() ? "" : "NOT ") + "POWERED";
    }

    @Override
    public RedstoneTorch clone() {
        return (RedstoneTorch)super.clone();
    }
}

