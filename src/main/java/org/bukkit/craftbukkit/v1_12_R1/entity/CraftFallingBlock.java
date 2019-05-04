/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;

public class CraftFallingBlock
extends CraftEntity
implements FallingBlock {
    public CraftFallingBlock(CraftServer server, ack entity) {
        super(server, entity);
    }

    @Override
    public ack getHandle() {
        return (ack)this.entity;
    }

    @Override
    public String toString() {
        return "CraftFallingBlock";
    }

    @Override
    public EntityType getType() {
        return EntityType.FALLING_BLOCK;
    }

    @Override
    public Material getMaterial() {
        return Material.getBlockMaterial(this.getBlockId());
    }

    @Override
    public int getBlockId() {
        return CraftMagicNumbers.getId(this.getHandle().l().u());
    }

    @Override
    public byte getBlockData() {
        return (byte)this.getHandle().l().u().e(this.getHandle().l());
    }

    @Override
    public boolean getDropItem() {
        return this.getHandle().b;
    }

    @Override
    public void setDropItem(boolean drop) {
        this.getHandle().b = drop;
    }

    @Override
    public boolean canHurtEntities() {
        return this.getHandle().g;
    }

    @Override
    public void setHurtEntities(boolean hurtEntities) {
        this.getHandle().g = hurtEntities;
    }

    @Override
    public void setTicksLived(int value) {
        super.setTicksLived(value);
        this.getHandle().T = value;
    }
}

