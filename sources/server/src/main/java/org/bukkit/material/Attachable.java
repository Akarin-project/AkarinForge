/*
 * Akarin Forge
 */
package org.bukkit.material;

import org.bukkit.block.BlockFace;
import org.bukkit.material.Directional;

public interface Attachable
extends Directional {
    public BlockFace getAttachedFace();
}

