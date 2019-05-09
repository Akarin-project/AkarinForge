/*
 * Akarin Forge
 */
package org.bukkit.block;

import org.bukkit.Nameable;
import org.bukkit.block.Container;
import org.bukkit.projectiles.BlockProjectileSource;

public interface Dispenser
extends Container,
Nameable {
    public BlockProjectileSource getBlockProjectileSource();

    public boolean dispense();
}

