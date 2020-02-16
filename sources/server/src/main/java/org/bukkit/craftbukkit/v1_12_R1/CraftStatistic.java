package org.bukkit.craftbukkit.v1_12_R1;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityList.EntityEggInfo;
import net.minecraft.stats.StatList;

import org.bukkit.Statistic;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import com.google.common.base.CaseFormat;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class CraftStatistic {
    private static final BiMap<String, org.bukkit.Statistic> statistics;

    static {
        ImmutableBiMap.Builder<String, org.bukkit.Statistic> statisticBuilder = ImmutableBiMap.<String, org.bukkit.Statistic>builder();
        for (Statistic statistic : Statistic.values()) {
            if (statistic == Statistic.PLAY_ONE_TICK) {
                statisticBuilder.put("stat.playOneMinute", statistic);
            } else {
                statisticBuilder.put("stat." + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, statistic.name()), statistic);
            }
        }

        statistics = statisticBuilder.build();
    }

    private CraftStatistic() {}

    public static org.bukkit.Statistic getBukkitStatistic(net.minecraft.stats.StatBase statistic) {
        return getBukkitStatisticByName(statistic.statId);
    }

    public static org.bukkit.Statistic getBukkitStatisticByName(String name) {
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
        return statistics.get(name);
    }

    public static net.minecraft.stats.StatBase getNMSStatistic(org.bukkit.Statistic statistic) {
        return StatList.getOneShotStat(statistics.inverse().get(statistic));
    }

    public static net.minecraft.stats.StatBase getMaterialStatistic(org.bukkit.Statistic stat, Material material) {
        try {
            if (stat == Statistic.MINE_BLOCK) {
                return StatList.getBlockStats(CraftMagicNumbers.getBlock(material)); // PAIL: getMineBlockStatistic
            }
            if (stat == Statistic.CRAFT_ITEM) {
                return StatList.getCraftStats(CraftMagicNumbers.getItem(material)); // PAIL: getCraftItemStatistic
            }
            if (stat == Statistic.USE_ITEM) {
                return StatList.getObjectUseStats(CraftMagicNumbers.getItem(material)); // PAIL: getUseItemStatistic
            }
            if (stat == Statistic.BREAK_ITEM) {
                return StatList.getObjectBreakStats(CraftMagicNumbers.getItem(material)); // PAIL: getBreakItemStatistic
            }
            if (stat == Statistic.PICKUP) {
                return StatList.getObjectsPickedUpStats(CraftMagicNumbers.getItem(material)); // PAIL: getPickupStatistic
            }
            if (stat == Statistic.DROP) {
                return StatList.getDroppedObjectStats(CraftMagicNumbers.getItem(material)); // PAIL: getDropItemStatistic
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
        return null;
    }

    public static net.minecraft.stats.StatBase getEntityStatistic(org.bukkit.Statistic stat, EntityType entity) {
        EntityEggInfo monsteregginfo = (EntityEggInfo) EntityList.ENTITY_EGGS.get(new ResourceLocation(entity.getName()));

        if (monsteregginfo != null) {
            if (stat == org.bukkit.Statistic.KILL_ENTITY) {
                return monsteregginfo.killEntityStat;
            }
            if (stat == org.bukkit.Statistic.ENTITY_KILLED_BY) {
                return monsteregginfo.entityKilledByStat;
            }
        }
        return null;
    }

    public static EntityType getEntityTypeFromStatistic(net.minecraft.stats.StatBase statistic) {
        String statisticString = statistic.statId;
        return EntityType.fromName(statisticString.substring(statisticString.lastIndexOf(".") + 1));
    }

    public static Material getMaterialFromStatistic(net.minecraft.stats.StatBase statistic) {
        String statisticString = statistic.statId;
        String val = statisticString.substring(statisticString.lastIndexOf(".") + 1);
        Item item = (Item) Item.REGISTRY.getObject(new ResourceLocation(val));
        if (item != null) {
            return Material.getMaterial(Item.getIdFromItem(item));
        }
        Block block = (Block) Block.REGISTRY.getObject(new ResourceLocation(val));
        if (block != null) {
            return Material.getMaterial(Block.getIdFromBlock(block));
        }
        try {
            return Material.getMaterial(Integer.parseInt(val));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
