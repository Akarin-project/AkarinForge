/*
 * Akarin Forge
 */
package org.bukkit.entity.minecart;

import org.bukkit.entity.Minecart;
import org.bukkit.inventory.InventoryHolder;

public interface HopperMinecart
extends Minecart,
InventoryHolder {
    public boolean isEnabled();

    public void setEnabled(boolean var1);
}

