/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 */
package org.bukkit;

import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.TippedArrow;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.ZombieVillager;

public enum EntityEffect {
    ARROW_PARTICLES(0, TippedArrow.class),
    RABBIT_JUMP(1, Rabbit.class),
    HURT(2, LivingEntity.class),
    DEATH(3, Entity.class),
    WOLF_SMOKE(6, Tameable.class),
    WOLF_HEARTS(7, Wolf.class),
    WOLF_SHAKE(8, Wolf.class),
    SHEEP_EAT(10, Entity.class),
    IRON_GOLEM_ROSE(11, IronGolem.class),
    VILLAGER_HEART(12, Villager.class),
    VILLAGER_ANGRY(13, Villager.class),
    VILLAGER_HAPPY(14, Villager.class),
    WITCH_MAGIC(15, Witch.class),
    ZOMBIE_TRANSFORM(16, ZombieVillager.class),
    FIREWORK_EXPLODE(17, Firework.class),
    LOVE_HEARTS(18, Ageable.class),
    SQUID_ROTATE(19, Squid.class),
    ENTITY_POOF(20, LivingEntity.class),
    GUARDIAN_TARGET(21, Guardian.class),
    SHIELD_BLOCK(29, LivingEntity.class),
    SHIELD_BREAK(30, LivingEntity.class),
    ARMOR_STAND_HIT(32, ArmorStand.class),
    THORNS_HURT(33, LivingEntity.class),
    IRON_GOLEM_SHEATH(34, IronGolem.class),
    TOTEM_RESURRECT(35, LivingEntity.class),
    HURT_DROWN(36, LivingEntity.class),
    HURT_EXPLOSION(37, LivingEntity.class);
    
    private final byte data;
    private final Class<? extends Entity> applicable;
    private static final Map<Byte, EntityEffect> BY_DATA;

    private EntityEffect(int data, Class<? extends Entity> clazz) {
        this.data = (byte)data;
        this.applicable = clazz;
    }

    @Deprecated
    public byte getData() {
        return this.data;
    }

    public Class<? extends Entity> getApplicable() {
        return this.applicable;
    }

    @Deprecated
    public static EntityEffect getByData(byte data) {
        return BY_DATA.get(Byte.valueOf(data));
    }

    static {
        BY_DATA = Maps.newHashMap();
        for (EntityEffect entityEffect : EntityEffect.values()) {
            BY_DATA.put(Byte.valueOf(entityEffect.data), entityEffect);
        }
    }
}

