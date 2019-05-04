/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Charsets
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.io.Files
 *  com.google.gson.Gson
 */
package org.bukkit.craftbukkit.v1_12_R1.util;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Achievement;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Statistic;
import org.bukkit.UnsafeValues;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.CraftStatistic;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftNamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.StringUtil;

public final class CraftMagicNumbers
implements UnsafeValues {
    public static final UnsafeValues INSTANCE = new CraftMagicNumbers();

    private CraftMagicNumbers() {
    }

    public static aow getBlock(Block block) {
        return CraftMagicNumbers.getBlock(block.getType());
    }

    @Deprecated
    public static aow getBlock(int id2) {
        return CraftMagicNumbers.getBlock(Material.getBlockMaterial(id2));
    }

    @Deprecated
    public static int getId(aow block) {
        return aow.a(block);
    }

    public static Material getMaterial(aow block) {
        return Material.getBlockMaterial(aow.a(block));
    }

    public static ain getItem(Material material) {
        ain item = ain.c(material.getId());
        return item;
    }

    @Deprecated
    public static ain getItem(int id2) {
        return ain.c(id2);
    }

    @Deprecated
    public static int getId(ain item) {
        return ain.a(item);
    }

    public static Material getMaterial(ain item) {
        Material material = Material.getMaterial(ain.a(item));
        if (material == null) {
            return Material.AIR;
        }
        return material;
    }

    public static aow getBlock(Material material) {
        aow block = aow.c((material = material == null ? Material.AIR : material).getId());
        if (block == null) {
            return aox.a;
        }
        return block;
    }

    @Override
    public Material getMaterialFromInternalName(String name) {
        return CraftMagicNumbers.getMaterial(ain.g.c(new nf(name)));
    }

    @Override
    public List<String> tabCompleteInternalMaterialName(String token, List<String> completions) {
        ArrayList results = Lists.newArrayList();
        for (nf key : ain.g.c()) {
            results.add(key.toString());
        }
        return StringUtil.copyPartialMatches(token, results, completions);
    }

    @Override
    public ItemStack modifyItemStack(ItemStack stack, String arguments) {
        aip nmsStack = CraftItemStack.asNMSCopy(stack);
        try {
            nmsStack.b(gp.a(arguments));
        }
        catch (go ex2) {
            Logger.getLogger(CraftMagicNumbers.class.getName()).log(Level.SEVERE, null, ex2);
        }
        stack.setItemMeta(CraftItemStack.getItemMeta(nmsStack));
        return stack;
    }

    @Override
    public Statistic getStatisticFromInternalName(String name) {
        return CraftStatistic.getBukkitStatisticByName(name);
    }

    @Override
    public Achievement getAchievementFromInternalName(String name) {
        throw new UnsupportedOperationException("Not supported in this Minecraft version.");
    }

    @Override
    public List<String> tabCompleteInternalStatisticOrAchievementName(String token, List<String> completions) {
        ArrayList<String> matches = new ArrayList<String>();
        Iterator<qo> iterator = qs.b.iterator();
        while (iterator.hasNext()) {
            String statistic = iterator.next().a;
            if (!statistic.startsWith(token)) continue;
            matches.add(statistic);
        }
        return matches;
    }

    @Override
    public Advancement loadAdvancement(NamespacedKey key, String advancement) {
        if (Bukkit.getAdvancement(key) != null) {
            throw new IllegalArgumentException("Advancement " + key + " already exists.");
        }
        i.a nms = rc.a(ns.b, advancement, i.a.class);
        if (nms != null) {
            ns.c.a(Maps.newHashMap(Collections.singletonMap(CraftNamespacedKey.toMinecraft(key), nms)));
            Advancement bukkit = Bukkit.getAdvancement(key);
            if (bukkit != null) {
                File file = new File(MinecraftServer.getServerInst().aK().d, key.getNamespace() + File.separator + key.getKey() + ".json");
                file.getParentFile().mkdirs();
                try {
                    Files.write((CharSequence)advancement, (File)file, (Charset)Charsets.UTF_8);
                }
                catch (IOException ex2) {
                    Bukkit.getLogger().log(Level.SEVERE, "Error saving advancement " + key, ex2);
                }
                MinecraftServer.getServerInst().am().w();
                return bukkit;
            }
        }
        return null;
    }

    @Override
    public boolean removeAdvancement(NamespacedKey key) {
        File file = new File(MinecraftServer.getServerInst().aK().d, key.getNamespace() + File.separator + key.getKey() + ".json");
        return file.delete();
    }

    public static class NBT {
        public static final int TAG_END = 0;
        public static final int TAG_BYTE = 1;
        public static final int TAG_SHORT = 2;
        public static final int TAG_INT = 3;
        public static final int TAG_LONG = 4;
        public static final int TAG_FLOAT = 5;
        public static final int TAG_DOUBLE = 6;
        public static final int TAG_BYTE_ARRAY = 7;
        public static final int TAG_STRING = 8;
        public static final int TAG_LIST = 9;
        public static final int TAG_COMPOUND = 10;
        public static final int TAG_INT_ARRAY = 11;
        public static final int TAG_ANY_NUMBER = 99;
    }

}

