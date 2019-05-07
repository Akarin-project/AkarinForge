package org.bukkit.craftbukkit.entity;

import com.destroystokyo.paper.entity.CraftSentientNPC;
import net.minecraft.entity.passive.EntityWaterMob;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.WaterMob;

public class CraftWaterMob extends CraftLivingEntity implements WaterMob, CraftSentientNPC { // Paper

    public CraftWaterMob(CraftServer server, EntityWaterMob entity) {
        super(server, entity);
    }

    @Override
    public EntityWaterMob getHandle() {
        return (EntityWaterMob) entity;
    }

    @Override
    public String toString() {
        return "CraftWaterMob";
    }
}
