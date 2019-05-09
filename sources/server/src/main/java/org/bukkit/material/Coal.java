/*
 * Akarin Forge
 */
package org.bukkit.material;

import org.bukkit.CoalType;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

public class Coal
extends MaterialData {
    public Coal() {
        super(Material.COAL);
    }

    public Coal(CoalType type) {
        this();
        this.setType(type);
    }

    @Deprecated
    public Coal(int type) {
        super(type);
    }

    public Coal(Material type) {
        super(type);
    }

    @Deprecated
    public Coal(int type, byte data) {
        super(type, data);
    }

    @Deprecated
    public Coal(Material type, byte data) {
        super(type, data);
    }

    public CoalType getType() {
        return CoalType.getByData(this.getData());
    }

    public void setType(CoalType type) {
        this.setData(type.getData());
    }

    @Override
    public String toString() {
        return (Object)((Object)this.getType()) + " " + super.toString();
    }

    @Override
    public Coal clone() {
        return (Coal)super.clone();
    }
}

