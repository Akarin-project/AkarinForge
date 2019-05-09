/*
 * Akarin Forge
 */
package org.bukkit.block;

import org.bukkit.DyeColor;
import org.bukkit.Nameable;
import org.bukkit.block.Container;

public interface ShulkerBox
extends Container,
Nameable {
    public DyeColor getColor();
}

