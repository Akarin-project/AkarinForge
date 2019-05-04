/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.Validate
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.apache.commons.lang3.Validate;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftTameableAnimal;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ocelot;

public class CraftOcelot
extends CraftTameableAnimal
implements Ocelot {
    public CraftOcelot(CraftServer server, aab ocelot) {
        super(server, ocelot);
    }

    @Override
    public aab getHandle() {
        return (aab)this.entity;
    }

    @Override
    public Ocelot.Type getCatType() {
        return Ocelot.Type.getType(this.getHandle().dt());
    }

    @Override
    public void setCatType(Ocelot.Type type) {
        Validate.notNull((Object)((Object)type), (String)"Cat type cannot be null", (Object[])new Object[0]);
        this.getHandle().g(type.getId());
    }

    @Override
    public String toString() {
        return "CraftOcelot";
    }

    @Override
    public EntityType getType() {
        return EntityType.OCELOT;
    }
}

