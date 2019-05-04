/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftAnimals;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Sheep;

public class CraftSheep
extends CraftAnimals
implements Sheep {
    public CraftSheep(CraftServer server, aag entity) {
        super(server, entity);
    }

    @Override
    public DyeColor getColor() {
        return DyeColor.getByWoolData((byte)this.getHandle().dl().a());
    }

    @Override
    public void setColor(DyeColor color) {
        this.getHandle().b(ahs.b(color.getWoolData()));
    }

    @Override
    public boolean isSheared() {
        return this.getHandle().dm();
    }

    @Override
    public void setSheared(boolean flag) {
        this.getHandle().p(flag);
    }

    @Override
    public aag getHandle() {
        return (aag)this.entity;
    }

    @Override
    public String toString() {
        return "CraftSheep";
    }

    @Override
    public EntityType getType() {
        return EntityType.SHEEP;
    }
}

