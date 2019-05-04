/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import com.google.common.base.Preconditions;
import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftGolem;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Shulker;

public class CraftShulker
extends CraftGolem
implements Shulker {
    public CraftShulker(CraftServer server, adi entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftShulker";
    }

    @Override
    public EntityType getType() {
        return EntityType.SHULKER;
    }

    @Override
    public adi getHandle() {
        return (adi)this.entity;
    }

    @Override
    public DyeColor getColor() {
        return DyeColor.getByWoolData(this.getHandle().V().a(adi.bx).byteValue());
    }

    @Override
    public void setColor(DyeColor color) {
        Preconditions.checkArgument((boolean)(color != null), (Object)"color");
        this.getHandle().V().b(adi.bx, Byte.valueOf(color.getWoolData()));
    }
}

