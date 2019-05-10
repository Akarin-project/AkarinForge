package io.akarin.forge.entity;

import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.EntityType;

public class CraftCustomEntity
extends CraftEntity {
    private String entityName;

    public CraftCustomEntity(CraftServer server, Entity entity) {
        super(server, entity);
        this.entityName = EntityRegistry.entityTypeMap.get(entity.getClass());
        if (this.entityName == null) {
            this.entityName = entity.getName();
        }
    }

    @Override
    public Entity getHandle() {
        return this.entity;
    }

    @Override
    public String toString() {
        return this.entityName;
    }

    @Override
    public EntityType getType() {
        EntityType type = EntityType.fromName(this.entityName);
        if (type != null) {
            return type;
        }
        return EntityType.MOD_CUSTOM;
    }

    @Override
    public String getCustomName() {
        String name = this.getHandle().getCustomNameTag();
        if (name == null || name.length() == 0) {
            return this.entity.getName();
        }
        return name;
    }
}

