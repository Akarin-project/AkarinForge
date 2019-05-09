/*
 * Akarin Forge
 */
package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Wood;

public class Sapling
extends Wood {
    public Sapling() {
        this(DEFAULT_SPECIES);
    }

    public Sapling(TreeSpecies species) {
        this(species, false);
    }

    public Sapling(TreeSpecies species, boolean isInstantGrowable) {
        this(Material.SAPLING, species, isInstantGrowable);
    }

    public Sapling(Material type) {
        this(type, DEFAULT_SPECIES, false);
    }

    public Sapling(Material type, TreeSpecies species) {
        this(type, species, false);
    }

    public Sapling(Material type, TreeSpecies species, boolean isInstantGrowable) {
        super(type, species);
        this.setIsInstantGrowable(isInstantGrowable);
    }

    @Deprecated
    public Sapling(int type, byte data) {
        super(type, data);
    }

    @Deprecated
    public Sapling(Material type, byte data) {
        super(type, data);
    }

    public boolean isInstantGrowable() {
        return (this.getData() & 8) == 8;
    }

    public void setIsInstantGrowable(boolean isInstantGrowable) {
        this.setData(isInstantGrowable ? (byte)(this.getData() & 7 | 8) : (byte)(this.getData() & 7));
    }

    @Override
    public String toString() {
        return (Object)((Object)this.getSpecies()) + " " + (this.isInstantGrowable() ? " IS_INSTANT_GROWABLE " : "") + " " + super.toString();
    }

    @Override
    public Sapling clone() {
        return (Sapling)super.clone();
    }
}

