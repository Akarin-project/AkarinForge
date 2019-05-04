/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftMonster;
import org.bukkit.entity.Illager;

public class CraftIllager
extends CraftMonster
implements Illager {
    public CraftIllager(CraftServer server, aco entity) {
        super(server, entity);
    }

    @Override
    public aco getHandle() {
        return (aco)super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftIllager";
    }
}

