/*
 * Akarin Forge
 */
package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.material.ExtendedRails;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Rails;
import org.bukkit.material.Redstone;

public class PoweredRail
extends ExtendedRails
implements Redstone {
    public PoweredRail() {
        super(Material.POWERED_RAIL);
    }

    @Deprecated
    public PoweredRail(int type) {
        super(type);
    }

    public PoweredRail(Material type) {
        super(type);
    }

    @Deprecated
    public PoweredRail(int type, byte data) {
        super(type, data);
    }

    @Deprecated
    public PoweredRail(Material type, byte data) {
        super(type, data);
    }

    @Override
    public boolean isPowered() {
        return (this.getData() & 8) == 8;
    }

    public void setPowered(boolean isPowered) {
        this.setData((byte)(isPowered ? this.getData() | 8 : this.getData() & -9));
    }

    @Override
    public PoweredRail clone() {
        return (PoweredRail)super.clone();
    }
}

