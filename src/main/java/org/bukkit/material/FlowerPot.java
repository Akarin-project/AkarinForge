/*
 * Akarin Forge
 */
package org.bukkit.material;

import org.bukkit.GrassSpecies;
import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.material.LongGrass;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Tree;

@Deprecated
public class FlowerPot
extends MaterialData {
    public FlowerPot() {
        super(Material.FLOWER_POT);
    }

    @Deprecated
    public FlowerPot(int type) {
        super(type);
    }

    public FlowerPot(Material type) {
        super(type);
    }

    @Deprecated
    public FlowerPot(int type, byte data) {
        super(type, data);
    }

    @Deprecated
    public FlowerPot(Material type, byte data) {
        super(type, data);
    }

    public MaterialData getContents() {
        switch (this.getData()) {
            case 1: {
                return new MaterialData(Material.RED_ROSE);
            }
            case 2: {
                return new MaterialData(Material.YELLOW_FLOWER);
            }
            case 3: {
                return new Tree(TreeSpecies.GENERIC);
            }
            case 4: {
                return new Tree(TreeSpecies.REDWOOD);
            }
            case 5: {
                return new Tree(TreeSpecies.BIRCH);
            }
            case 6: {
                return new Tree(TreeSpecies.JUNGLE);
            }
            case 7: {
                return new MaterialData(Material.RED_MUSHROOM);
            }
            case 8: {
                return new MaterialData(Material.BROWN_MUSHROOM);
            }
            case 9: {
                return new MaterialData(Material.CACTUS);
            }
            case 10: {
                return new MaterialData(Material.DEAD_BUSH);
            }
            case 11: {
                return new LongGrass(GrassSpecies.FERN_LIKE);
            }
        }
        return null;
    }

    public void setContents(MaterialData materialData) {
        GrassSpecies species;
        Material mat = materialData.getItemType();
        if (mat == Material.RED_ROSE) {
            this.setData(1);
        } else if (mat == Material.YELLOW_FLOWER) {
            this.setData(2);
        } else if (mat == Material.RED_MUSHROOM) {
            this.setData(7);
        } else if (mat == Material.BROWN_MUSHROOM) {
            this.setData(8);
        } else if (mat == Material.CACTUS) {
            this.setData(9);
        } else if (mat == Material.DEAD_BUSH) {
            this.setData(10);
        } else if (mat == Material.SAPLING) {
            TreeSpecies species2 = ((Tree)materialData).getSpecies();
            if (species2 == TreeSpecies.GENERIC) {
                this.setData(3);
            } else if (species2 == TreeSpecies.REDWOOD) {
                this.setData(4);
            } else if (species2 == TreeSpecies.BIRCH) {
                this.setData(5);
            } else {
                this.setData(6);
            }
        } else if (mat == Material.LONG_GRASS && (species = ((LongGrass)materialData).getSpecies()) == GrassSpecies.FERN_LIKE) {
            this.setData(11);
        }
    }

    @Override
    public String toString() {
        return super.toString() + " containing " + this.getContents();
    }

    @Override
    public FlowerPot clone() {
        return (FlowerPot)super.clone();
    }
}

