/*
 * Akarin Forge
 */
package org.bukkit.material;

import org.bukkit.block.BlockFace;

public interface Directional {
    public void setFacingDirection(BlockFace var1);

    public BlockFace getFacing();
}

