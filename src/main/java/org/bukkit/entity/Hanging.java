/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.material.Attachable;

public interface Hanging
extends Entity,
Attachable {
    public boolean setFacingDirection(BlockFace var1, boolean var2);
}

