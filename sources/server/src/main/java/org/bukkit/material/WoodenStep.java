/*
 * Akarin Forge
 */
package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Wood;

public class WoodenStep
extends Wood {
    protected static final Material DEFAULT_TYPE = Material.WOOD_STEP;
    protected static final boolean DEFAULT_INVERTED = false;

    public WoodenStep() {
        this(DEFAULT_SPECIES, false);
    }

    public WoodenStep(TreeSpecies species) {
        this(species, false);
    }

    public WoodenStep(TreeSpecies species, boolean inv) {
        super(DEFAULT_TYPE, species);
        this.setInverted(inv);
    }

    @Deprecated
    public WoodenStep(int type) {
        super(type);
    }

    @Deprecated
    public WoodenStep(int type, byte data) {
        super(type, data);
    }

    @Deprecated
    public WoodenStep(Material type, byte data) {
        super(type, data);
    }

    public boolean isInverted() {
        return (this.getData() & 8) != 0;
    }

    public void setInverted(boolean inv) {
        int dat = this.getData() & 7;
        if (inv) {
            dat |= 8;
        }
        this.setData((byte)dat);
    }

    @Override
    public WoodenStep clone() {
        return (WoodenStep)super.clone();
    }

    @Override
    public String toString() {
        return super.toString() + " " + (Object)((Object)this.getSpecies()) + (this.isInverted() ? " inverted" : "");
    }
}

