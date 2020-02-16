package org.bukkit.craftbukkit.v1_12_R1.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.ComplexLivingEntity;

public abstract class CraftComplexLivingEntity extends CraftLivingEntity implements ComplexLivingEntity {
    public CraftComplexLivingEntity(CraftServer server, EntityLivingBase entity) {
        super(server, entity);
    }

    @Override
    public EntityLiving getHandle() {
        return (EntityLiving) entity;
    }

    @Override
    public String toString() {
        return "CraftComplexLivingEntity";
    }
}
