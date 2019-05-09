/*
 * Akarin Forge
 */
package org.bukkit.projectiles;

import org.bukkit.block.Block;
import org.bukkit.projectiles.ProjectileSource;

public interface BlockProjectileSource
extends ProjectileSource {
    public Block getBlock();
}

