/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.DaylightDetector;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBlockEntityState;

public class CraftDaylightDetector
extends CraftBlockEntityState<avo>
implements DaylightDetector {
    public CraftDaylightDetector(Block block) {
        super(block, avo.class);
    }

    public CraftDaylightDetector(Material material, avo te2) {
        super(material, te2);
    }
}

