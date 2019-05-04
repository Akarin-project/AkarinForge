/*
 * Akarin Forge
 */
package org.bukkit.inventory.meta;

public interface Repairable {
    public boolean hasRepairCost();

    public int getRepairCost();

    public void setRepairCost(int var1);

    public Repairable clone();
}

