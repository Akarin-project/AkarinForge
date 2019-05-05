/*
 * Decompiled with CFR 0_119.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package io.akarin.forge.api.bukkit.entity;

import java.lang.reflect.Constructor;
import javax.annotation.Nullable;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;

import io.akarin.forge.utils.NMSUtils;

public class CustomEntityClass {
    private final String entityName;
    private final Class<? extends vg> entityClass;

    public CustomEntityClass(String entityName, Class<? extends vg> entityClass) {
        this.entityName = entityName;
        this.entityClass = entityClass;
    }

    public String getEntityName() {
        return this.entityName;
    }

    public Class<? extends vg> getEntityClass() {
        return this.entityClass;
    }

    @Nullable
    public vg newInstance(oo world) {
        vg entity = null;
        try {
            entity = this.entityClass.getConstructor(amu.class).newInstance(world);
        }
        catch (Throwable e2) {
            e2.printStackTrace();
        }
        return entity;
    }

    @Nullable
    public vg newInstance(World world) {
        return this.newInstance(NMSUtils.toNMS(world));
    }

    @Nullable
    public Entity spawn(World world, double x2, double y2, double z2) {
        oo worldserver = NMSUtils.toNMS(world);
        vg entity = this.newInstance(worldserver);
        if (entity == null) {
            return null;
        }
        entity.b(x2, y2, z2);
        worldserver.a(entity);
        return entity.getBukkitEntity();
    }

    public boolean isLivingbase() {
        return vp.class.isAssignableFrom(this.entityClass);
    }

    public boolean isCreature() {
        return vx.class.isAssignableFrom(this.entityClass);
    }

    public boolean isAnimal() {
        return zv.class.isAssignableFrom(this.entityClass);
    }

    public boolean isMonster() {
        return ade.class.isAssignableFrom(this.entityClass);
    }
}

