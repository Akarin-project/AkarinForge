/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftCreature;
import org.bukkit.entity.Golem;

public class CraftGolem
extends CraftCreature
implements Golem {
    public CraftGolem(CraftServer server, zz entity) {
        super(server, entity);
    }

    @Override
    public zz getHandle() {
        return (zz)this.entity;
    }

    @Override
    public String toString() {
        return "CraftGolem";
    }
}

