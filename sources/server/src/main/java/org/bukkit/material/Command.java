/*
 * Akarin Forge
 */
package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Redstone;

public class Command
extends MaterialData
implements Redstone {
    public Command() {
        super(Material.COMMAND);
    }

    @Deprecated
    public Command(int type) {
        super(type);
    }

    public Command(Material type) {
        super(type);
    }

    @Deprecated
    public Command(int type, byte data) {
        super(type, data);
    }

    @Deprecated
    public Command(Material type, byte data) {
        super(type, data);
    }

    @Override
    public boolean isPowered() {
        return (this.getData() & 1) != 0;
    }

    public void setPowered(boolean bool) {
        this.setData((byte)(bool ? this.getData() | 1 : this.getData() & -2));
    }

    @Override
    public String toString() {
        return super.toString() + " " + (this.isPowered() ? "" : "NOT ") + "POWERED";
    }

    @Override
    public Command clone() {
        return (Command)super.clone();
    }
}

