/*
 * Akarin Forge
 */
package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.material.MaterialData;

public class Wood
extends MaterialData {
    protected static final Material DEFAULT_TYPE = Material.WOOD;
    protected static final TreeSpecies DEFAULT_SPECIES = TreeSpecies.GENERIC;

    public Wood() {
        this(DEFAULT_TYPE, DEFAULT_SPECIES);
    }

    public Wood(TreeSpecies species) {
        this(DEFAULT_TYPE, species);
    }

    @Deprecated
    public Wood(int type) {
        super(type);
    }

    public Wood(Material type) {
        this(type, DEFAULT_SPECIES);
    }

    public Wood(Material type, TreeSpecies species) {
        super(Wood.getSpeciesType(type, species));
        this.setSpecies(species);
    }

    @Deprecated
    public Wood(int type, byte data) {
        super(type, data);
    }

    @Deprecated
    public Wood(Material type, byte data) {
        super(type, data);
    }

    public TreeSpecies getSpecies() {
        switch (this.getItemType()) {
            case WOOD: 
            case WOOD_DOUBLE_STEP: {
                return TreeSpecies.getByData(this.getData());
            }
            case LOG: 
            case LEAVES: {
                return TreeSpecies.getByData((byte)(this.getData() & 3));
            }
            case LOG_2: 
            case LEAVES_2: {
                return TreeSpecies.getByData((byte)(this.getData() & 3 | 4));
            }
            case SAPLING: 
            case WOOD_STEP: {
                return TreeSpecies.getByData((byte)(this.getData() & 7));
            }
        }
        throw new IllegalArgumentException("Invalid block type for tree species");
    }

    private static Material getSpeciesType(Material type, TreeSpecies species) {
        switch (species) {
            case GENERIC: 
            case REDWOOD: 
            case BIRCH: 
            case JUNGLE: {
                switch (type) {
                    case LOG_2: {
                        return Material.LOG;
                    }
                    case LEAVES_2: {
                        return Material.LEAVES;
                    }
                }
                break;
            }
            case ACACIA: 
            case DARK_OAK: {
                switch (type) {
                    case LOG: {
                        return Material.LOG_2;
                    }
                    case LEAVES: {
                        return Material.LEAVES_2;
                    }
                }
            }
        }
        return type;
    }

    public void setSpecies(TreeSpecies species) {
        boolean firstType = false;
        switch (this.getItemType()) {
            case WOOD: 
            case WOOD_DOUBLE_STEP: {
                this.setData(species.getData());
                break;
            }
            case LOG: 
            case LEAVES: {
                firstType = true;
            }
            case LOG_2: 
            case LEAVES_2: {
                switch (species) {
                    case GENERIC: 
                    case REDWOOD: 
                    case BIRCH: 
                    case JUNGLE: {
                        if (firstType) break;
                        throw new IllegalArgumentException("Invalid tree species for block type, use block type 2 instead");
                    }
                    case ACACIA: 
                    case DARK_OAK: {
                        if (!firstType) break;
                        throw new IllegalArgumentException("Invalid tree species for block type 2, use block type instead");
                    }
                }
                this.setData((byte)(this.getData() & 12 | species.getData() & 3));
                break;
            }
            case SAPLING: 
            case WOOD_STEP: {
                this.setData((byte)(this.getData() & 8 | species.getData()));
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid block type for tree species");
            }
        }
    }

    @Override
    public String toString() {
        return (Object)((Object)this.getSpecies()) + " " + super.toString();
    }

    @Override
    public Wood clone() {
        return (Wood)super.clone();
    }

}

