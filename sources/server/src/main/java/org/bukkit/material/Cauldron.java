/*
 * Akarin Forge
 */
package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;

public class Cauldron
extends MaterialData {
    private static final int CAULDRON_FULL = 3;
    private static final int CAULDRON_EMPTY = 0;

    public Cauldron() {
        super(Material.CAULDRON);
    }

    @Deprecated
    public Cauldron(int type, byte data) {
        super(type, data);
    }

    @Deprecated
    public Cauldron(byte data) {
        super(Material.CAULDRON, data);
    }

    public boolean isFull() {
        return this.getData() >= 3;
    }

    public boolean isEmpty() {
        return this.getData() <= 0;
    }

    @Override
    public String toString() {
        return (this.isEmpty() ? "EMPTY" : (this.isFull() ? "FULL" : new StringBuilder().append(this.getData()).append("/3 FULL").toString())) + " CAULDRON";
    }

    @Override
    public Cauldron clone() {
        return (Cauldron)super.clone();
    }
}

