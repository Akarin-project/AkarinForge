/*
 * Akarin Forge
 */
package org.bukkit.inventory.meta;

import org.bukkit.block.BlockState;
import org.bukkit.inventory.meta.ItemMeta;

public interface BlockStateMeta
extends ItemMeta {
    public boolean hasBlockState();

    public BlockState getBlockState();

    public void setBlockState(BlockState var1);
}

