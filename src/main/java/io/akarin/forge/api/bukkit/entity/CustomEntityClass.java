package io.akarin.forge.api.bukkit.entity;

import javax.annotation.Nullable;
import org.bukkit.World;
import org.bukkit.craftbukkit.entity.CraftEntity;

import io.akarin.forge.utils.NMSUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.world.WorldServer;

public class CustomEntityClass {
    private final String entityName;
    private final Class<? extends Entity> entityClass;

    public CustomEntityClass(String entityName, Class<? extends Entity> entityClass) {
        this.entityName = entityName;
        this.entityClass = entityClass;
    }

    public String getEntityName() {
        return this.entityName;
    }

    public Class<? extends Entity> getEntityClass() {
        return this.entityClass;
    }

    @Nullable
    public Entity newInstance(WorldServer world) {
        Entity entity = null;
        try {
            entity = this.entityClass.getConstructor(World.class).newInstance(world);
        }
        catch (Throwable e2) {
            e2.printStackTrace();
        }
        return entity;
    }

    @Nullable
    public Entity newInstance(World world) {
        return this.newInstance(NMSUtils.toNMS(world));
    }

    @Nullable
    public CraftEntity spawn(World world, double x2, double y2, double z2) {
        WorldServer worldserver = NMSUtils.toNMS(world);
        Entity entity = this.newInstance(worldserver);
        if (entity == null) {
            return null;
        }
        entity.setPosition(x2, y2, z2);
        worldserver.spawnEntity(entity);
        return entity.getBukkitEntity();
    }

    public boolean isLivingbase() {
        return EntityLivingBase.class.isAssignableFrom(this.entityClass);
    }

    public boolean isCreature() {
        return EntityCreature.class.isAssignableFrom(this.entityClass);
    }

    public boolean isAnimal() {
        return EntityAnimal.class.isAssignableFrom(this.entityClass);
    }

    public boolean isMonster() {
        return EntityMob.class.isAssignableFrom(this.entityClass);
    }
}

