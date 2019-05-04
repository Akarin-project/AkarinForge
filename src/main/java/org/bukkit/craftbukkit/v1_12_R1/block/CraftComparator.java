/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Comparator;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBlockEntityState;

public class CraftComparator
extends CraftBlockEntityState<avn>
implements Comparator {
    public CraftComparator(Block block) {
        super(block, avn.class);
    }

    public CraftComparator(Material material, avn te2) {
        super(material, te2);
    }
}

