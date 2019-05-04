/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import com.google.common.base.Preconditions;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftTameableAnimal;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Parrot;

public class CraftParrot
extends CraftTameableAnimal
implements Parrot {
    public CraftParrot(CraftServer server, aac parrot) {
        super(server, parrot);
    }

    @Override
    public aac getHandle() {
        return (aac)this.entity;
    }

    @Override
    public Parrot.Variant getVariant() {
        return Parrot.Variant.values()[this.getHandle().du()];
    }

    @Override
    public void setVariant(Parrot.Variant variant) {
        Preconditions.checkArgument((boolean)(variant != null), (Object)"variant");
        this.getHandle().m(variant.ordinal());
    }

    @Override
    public String toString() {
        return "CraftParrot";
    }

    @Override
    public EntityType getType() {
        return EntityType.PARROT;
    }
}

