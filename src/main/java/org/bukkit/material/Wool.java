/*
 * Akarin Forge
 */
package org.bukkit.material;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.material.Colorable;
import org.bukkit.material.MaterialData;

public class Wool
extends MaterialData
implements Colorable {
    public Wool() {
        super(Material.WOOL);
    }

    public Wool(DyeColor color) {
        this();
        this.setColor(color);
    }

    @Deprecated
    public Wool(int type) {
        super(type);
    }

    public Wool(Material type) {
        super(type);
    }

    @Deprecated
    public Wool(int type, byte data) {
        super(type, data);
    }

    @Deprecated
    public Wool(Material type, byte data) {
        super(type, data);
    }

    @Override
    public DyeColor getColor() {
        return DyeColor.getByWoolData(this.getData());
    }

    @Override
    public void setColor(DyeColor color) {
        this.setData(color.getWoolData());
    }

    @Override
    public String toString() {
        return (Object)((Object)this.getColor()) + " " + super.toString();
    }

    @Override
    public Wool clone() {
        return (Wool)super.clone();
    }
}

