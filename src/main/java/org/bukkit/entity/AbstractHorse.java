/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.entity.Animals;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Vehicle;
import org.bukkit.inventory.AbstractHorseInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public interface AbstractHorse
extends Animals,
Vehicle,
InventoryHolder,
Tameable {
    @Deprecated
    public Horse.Variant getVariant();

    @Deprecated
    public void setVariant(Horse.Variant var1);

    public int getDomestication();

    public void setDomestication(int var1);

    public int getMaxDomestication();

    public void setMaxDomestication(int var1);

    public double getJumpStrength();

    public void setJumpStrength(double var1);

    @Override
    public AbstractHorseInventory getInventory();
}

