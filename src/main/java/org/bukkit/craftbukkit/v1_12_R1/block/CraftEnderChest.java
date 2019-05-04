/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.EnderChest;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBlockEntityState;

public class CraftEnderChest
extends CraftBlockEntityState<avs>
implements EnderChest {
    public CraftEnderChest(Block block) {
        super(block, avs.class);
    }

    public CraftEnderChest(Material material, avs te2) {
        super(material, te2);
    }
}

