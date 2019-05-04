/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.CaseFormat
 *  com.google.common.collect.BiMap
 *  com.google.common.collect.ImmutableBiMap
 *  com.google.common.collect.ImmutableBiMap$Builder
 */
package org.bukkit.craftbukkit.v1_12_R1;

import com.google.common.base.CaseFormat;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers;
import org.bukkit.entity.EntityType;

public class CraftStatistic {
    private static final BiMap<String, Statistic> statistics;

    private CraftStatistic() {
    }

    public static Statistic getBukkitStatistic(qo statistic) {
        return CraftStatistic.getBukkitStatisticByName(statistic.a);
    }

    public static Statistic getBukkitStatisticByName(String name) {
        if (name.startsWith("stat.killEntity.")) {
            name = "stat.killEntity";
        }
        if (name.startsWith("stat.entityKilledBy.")) {
            name = "stat.entityKilledBy";
        }
        if (name.startsWith("stat.breakItem.")) {
            name = "stat.breakItem";
        }
        if (name.startsWith("stat.useItem.")) {
            name = "stat.useItem";
        }
        if (name.startsWith("stat.mineBlock.")) {
            name = "stat.mineBlock";
        }
        if (name.startsWith("stat.craftItem.")) {
            name = "stat.craftItem";
        }
        if (name.startsWith("stat.drop.")) {
            name = "stat.drop";
        }
        if (name.startsWith("stat.pickup.")) {
            name = "stat.pickup";
        }
        return (Statistic)((Object)statistics.get((Object)name));
    }

    public static qo getNMSStatistic(Statistic statistic) {
        return qs.a((String)statistics.inverse().get((Object)statistic));
    }

    public static qo getMaterialStatistic(Statistic stat, Material material) {
        try {
            if (stat == Statistic.MINE_BLOCK) {
                return qs.a(CraftMagicNumbers.getBlock(material));
            }
            if (stat == Statistic.CRAFT_ITEM) {
                return qs.a(CraftMagicNumbers.getItem(material));
            }
            if (stat == Statistic.USE_ITEM) {
                return qs.b(CraftMagicNumbers.getItem(material));
            }
            if (stat == Statistic.BREAK_ITEM) {
                return qs.c(CraftMagicNumbers.getItem(material));
            }
            if (stat == Statistic.PICKUP) {
                return qs.d(CraftMagicNumbers.getItem(material));
            }
            if (stat == Statistic.DROP) {
                return qs.e(CraftMagicNumbers.getItem(material));
            }
        }
        catch (ArrayIndexOutOfBoundsException e2) {
            return null;
        }
        return null;
    }

    public static qo getEntityStatistic(Statistic stat, EntityType entity) {
        vi.a monsteregginfo = vi.c.get(new nf(entity.getName()));
        if (monsteregginfo != null) {
            if (stat == Statistic.KILL_ENTITY) {
                return monsteregginfo.d;
            }
            if (stat == Statistic.ENTITY_KILLED_BY) {
                return monsteregginfo.e;
            }
        }
        return null;
    }

    public static EntityType getEntityTypeFromStatistic(qo statistic) {
        String statisticString = statistic.a;
        return EntityType.fromName(statisticString.substring(statisticString.lastIndexOf(".") + 1));
    }

    public static Material getMaterialFromStatistic(qo statistic) {
        String statisticString = statistic.a;
        String val = statisticString.substring(statisticString.lastIndexOf(".") + 1);
        ain item = ain.g.c(new nf(val));
        if (item != null) {
            return Material.getMaterial(ain.a(item));
        }
        aow block = aow.h.c(new nf(val));
        if (block != null) {
            return Material.getBlockMaterial(aow.a(block));
        }
        try {
            return Material.getMaterial(Integer.parseInt(val));
        }
        catch (NumberFormatException e2) {
            return null;
        }
    }

    static {
        ImmutableBiMap.Builder statisticBuilder = ImmutableBiMap.builder();
        for (Statistic statistic : Statistic.values()) {
            if (statistic == Statistic.PLAY_ONE_TICK) {
                statisticBuilder.put((Object)"stat.playOneMinute", (Object)statistic);
                continue;
            }
            statisticBuilder.put((Object)("stat." + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, statistic.name())), (Object)statistic);
        }
        statistics = statisticBuilder.build();
    }
}

