/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.entity.AbstractHorse;

public interface ChestedHorse
extends AbstractHorse {
    public boolean isCarryingChest();

    public void setCarryingChest(boolean var1);
}

