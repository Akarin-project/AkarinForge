/*
 * Akarin Forge
 */
package org.bukkit.block;

import org.bukkit.Nameable;
import org.bukkit.block.Container;
import org.bukkit.inventory.Inventory;

public interface Chest
extends Container,
Nameable {
    public Inventory getBlockInventory();
}

