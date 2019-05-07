package org.bukkit.craftbukkit.entity;

import com.destroystokyo.paper.entity.CraftSentientNPC;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.ComplexLivingEntity;

public abstract class CraftComplexLivingEntity extends CraftLivingEntity implements ComplexLivingEntity, CraftSentientNPC { // Paper
    public CraftComplexLivingEntity(CraftServer server, EntityLivingBase entity) {
        super(server, entity);
    }

    @Override
    public EntityLiving getHandle() { // Paper
        return (EntityLiving) entity; // Paper
    }

    @Override
    public String toString() {
        return "CraftComplexLivingEntity";
    }
}
