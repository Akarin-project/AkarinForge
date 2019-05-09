/*
 * Akarin Forge
 */
package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Redstone;

public class RedstoneWire
extends MaterialData
implements Redstone {
    public RedstoneWire() {
        super(Material.REDSTONE_WIRE);
    }

    @Deprecated
    public RedstoneWire(int type) {
        super(type);
    }

    public RedstoneWire(Material type) {
        super(type);
    }

    @Deprecated
    public RedstoneWire(int type, byte data) {
        super(type, data);
    }

    @Deprecated
    public RedstoneWire(Material type, byte data) {
        super(type, data);
    }

    @Override
    public boolean isPowered() {
        return this.getData() > 0;
    }

    @Override
    public String toString() {
        return super.toString() + " " + (this.isPowered() ? "" : "NOT ") + "POWERED";
    }

    @Override
    public RedstoneWire clone() {
        return (RedstoneWire)super.clone();
    }
}

