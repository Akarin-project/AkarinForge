/*
 * Akarin Forge
 */
package org.bukkit.inventory;

import org.bukkit.inventory.Inventory;

public interface AnvilInventory
extends Inventory {
    public String getRenameText();

    public int getRepairCost();

    public void setRepairCost(int var1);
}

