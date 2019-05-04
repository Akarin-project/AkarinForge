/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftAbstractHorse;
import org.bukkit.entity.ChestedHorse;

public abstract class CraftChestedHorse
extends CraftAbstractHorse
implements ChestedHorse {
    public CraftChestedHorse(CraftServer server, aan entity) {
        super(server, entity);
    }

    @Override
    public aan getHandle() {
        return (aan)super.getHandle();
    }

    @Override
    public boolean isCarryingChest() {
        return this.getHandle().dm();
    }

    @Override
    public void setCarryingChest(boolean chest) {
        if (chest == this.isCarryingChest()) {
            return;
        }
        this.getHandle().q(chest);
        this.getHandle().dC();
    }
}

