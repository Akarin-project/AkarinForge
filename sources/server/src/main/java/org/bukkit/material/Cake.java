/*
 * Akarin Forge
 */
package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;

public class Cake
extends MaterialData {
    public Cake() {
        super(Material.CAKE_BLOCK);
    }

    @Deprecated
    public Cake(int type) {
        super(type);
    }

    public Cake(Material type) {
        super(type);
    }

    @Deprecated
    public Cake(int type, byte data) {
        super(type, data);
    }

    @Deprecated
    public Cake(Material type, byte data) {
        super(type, data);
    }

    public int getSlicesEaten() {
        return this.getData();
    }

    public int getSlicesRemaining() {
        return 6 - this.getData();
    }

    public void setSlicesEaten(int n2) {
        if (n2 < 6) {
            this.setData((byte)n2);
        }
    }

    public void setSlicesRemaining(int n2) {
        if (n2 > 6) {
            n2 = 6;
        }
        this.setData((byte)(6 - n2));
    }

    @Override
    public String toString() {
        return super.toString() + " " + this.getSlicesEaten() + "/" + this.getSlicesRemaining() + " slices eaten/remaining";
    }

    @Override
    public Cake clone() {
        return (Cake)super.clone();
    }
}

