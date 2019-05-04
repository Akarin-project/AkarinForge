/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftTameableAnimal;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Wolf;

public class CraftWolf
extends CraftTameableAnimal
implements Wolf {
    public CraftWolf(CraftServer server, aam wolf) {
        super(server, wolf);
    }

    @Override
    public boolean isAngry() {
        return this.getHandle().dv();
    }

    @Override
    public void setAngry(boolean angry) {
        this.getHandle().s(angry);
    }

    @Override
    public aam getHandle() {
        return (aam)this.entity;
    }

    @Override
    public EntityType getType() {
        return EntityType.WOLF;
    }

    @Override
    public DyeColor getCollarColor() {
        return DyeColor.getByWoolData((byte)this.getHandle().dw().a());
    }

    @Override
    public void setCollarColor(DyeColor color) {
        this.getHandle().a(ahs.b(color.getWoolData()));
    }
}

