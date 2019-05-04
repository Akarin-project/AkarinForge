/*
 * Akarin Forge
 */
package org.bukkit.block;

import org.bukkit.block.BlockState;
import org.bukkit.material.MaterialData;

public interface FlowerPot
extends BlockState {
    public MaterialData getContents();

    public void setContents(MaterialData var1);
}

