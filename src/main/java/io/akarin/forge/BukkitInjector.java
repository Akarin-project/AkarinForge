/*
 * Decompiled with CFR 0_119.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.Level
 */
package io.akarin.forge;

import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.Level;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_12_R1.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.v1_12_R1.potion.CraftPotionEffectType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;

import io.akarin.forge.entity.CraftCustomEntity;

public class BukkitInjector {
    public static boolean initializedBukkit = false;

    public static void injectItemBukkitMaterials() {
        for (Entry<ResourceLocation, Item> entry : ForgeRegistries.ITEMS.getEntries()) {
            nf key = entry.getKey();
            ain item = entry.getValue();
            if (key.b().equals("minecraft")) continue;
            String materialName = key.toString().toUpperCase().replaceAll("(:|\\s)", "_").replaceAll("\\W", "");
            Material material = Material.addMaterial(EnumHelper.addEnum(Material.class, materialName, new Class[]{Integer.TYPE, Integer.TYPE}, new Object[]{ain.a(item), item.j()}));
            if (material != null) {
                FMLLog.log(Level.DEBUG, "Injected new Forge item material %s with ID %d.", material.name(), material.getId());
                continue;
            }
            FMLLog.log(Level.DEBUG, "Inject item failure %s with ID %d.", materialName, ain.a(item));
        }
    }

    public static void injectBlockBukkitMaterials() {
        for (Material material : Material.values()) {
            if (material.getId() >= 256) continue;
            Material.addBlockMaterial(material);
        }
        for (Map.Entry<ResourceLocation, Block> entry : ForgeRegistries.BLOCKS.getEntries()) {
            ResourceLocation key = entry.getKey();
            Block block = entry.getValue();
            if (key.b().equals("minecraft")) continue;
            String materialName = key.toString().toUpperCase().replaceAll("(:|\\s)", "_").replaceAll("\\W", "");
            Material material = Material.addBlockMaterial(EnumHelper.addEnum(Material.class, materialName, new Class[]{Integer.TYPE}, new Object[]{aow.a(block)}));
            if (material != null) {
                FMLLog.log(Level.DEBUG, "Injected new Forge block material %s with ID %d.", material.name(), material.getId());
                continue;
            }
            FMLLog.log(Level.DEBUG, "Inject block failure %s with ID %d.", materialName, aow.a(block));
        }
    }

    public static void injectBiomes() {
        block0 : for (Entry<ResourceLocation, net.minecraft.world.biome.Biome> entry : ForgeRegistries.BIOMES.getEntries()) {
            String biomeName = entry.getKey().a().toUpperCase(Locale.ENGLISH);
            for (Biome biome : Biome.values()) {
                if (biome.toString().equals(biomeName)) continue block0;
            }
            EnumHelper.addEnum(Biome.class, biomeName, new Class[0], new Object[0]);
        }
    }

    public static void injectEntityType() {
        Map NAME_MAP = ReflectionHelper.getPrivateValue(EntityType.class, null, "NAME_MAP");
        Map ID_MAP = ReflectionHelper.getPrivateValue(EntityType.class, null, "ID_MAP");
        for (Map.Entry<String, Class<? extends vg>> entity : EntityRegistry.entityClassMap.entrySet()) {
            String name = entity.getKey();
            String entityType = name.replace("-", "_").toUpperCase();
            int typeId = GameData.getEntityRegistry().getID(EntityRegistry.getEntry(entity.getValue()));
            EntityType bukkitType = EnumHelper.addEnum(EntityType.class, entityType, new Class[]{String.class, Class.class, Integer.TYPE, Boolean.TYPE}, new Object[]{name, CraftCustomEntity.class, typeId, false});
            NAME_MAP.put(name.toLowerCase(), bukkitType);
            ID_MAP.put((short)typeId, bukkitType);
        }
    }

    public static void registerEnchantments() {
        for (alk enchantment : alk.b) {
            Enchantment.registerEnchantment(new CraftEnchantment(enchantment));
        }
        Enchantment.stopAcceptingRegistrations();
    }

    public static void registerPotions() {
        for (uz effect : uz.b) {
            PotionEffectType.registerPotionEffectType(new CraftPotionEffectType(effect));
        }
        PotionEffectType.stopAcceptingRegistrations();
    }
}

