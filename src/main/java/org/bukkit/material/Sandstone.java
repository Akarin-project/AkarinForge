/*
 * Akarin Forge
 */
package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.SandstoneType;
import org.bukkit.material.MaterialData;

public class Sandstone
extends MaterialData {
    public Sandstone() {
        super(Material.SANDSTONE);
    }

    public Sandstone(SandstoneType type) {
        this();
        this.setType(type);
    }

    @Deprecated
    public Sandstone(int type) {
        super(type);
    }

    public Sandstone(Material type) {
        super(type);
    }

    @Deprecated
    public Sandstone(int type, byte data) {
        super(type, data);
    }

    @Deprecated
    public Sandstone(Material type, byte data) {
        super(type, data);
    }

    public SandstoneType getType() {
        return SandstoneType.getByData(this.getData());
    }

    public void setType(SandstoneType type) {
        this.setData(type.getData());
    }

    @Override
    public String toString() {
        return (Object)((Object)this.getType()) + " " + super.toString();
    }

    @Override
    public Sandstone clone() {
        return (Sandstone)super.clone();
    }
}

