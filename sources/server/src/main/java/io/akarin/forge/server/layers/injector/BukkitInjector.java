package io.akarin.forge.server.layers.injector;

import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
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

import io.akarin.forge.server.layers.entity.CraftModdedEntity;

public class BukkitInjector {
    public static boolean initializedBukkit = false;

    public static void injectItemBukkitMaterials() {
        for (Entry<ResourceLocation, Item> entry : ForgeRegistries.ITEMS.getEntries()) {
            ResourceLocation key = entry.getKey();
            Item item = entry.getValue();
            
            if (key.getResourceDomain().equals("minecraft"))
                continue;
            
            String materialName = key.toString().toUpperCase().replaceAll("(:|\\s)", "_").replaceAll("\\W", "");
            Material material = Material.addMaterial(EnumHelper.addEnum(Material.class, materialName, new Class[]{Integer.TYPE, Integer.TYPE}, new Object[]{Item.getIdFromItem(item), item.getItemStackLimit()}));
            if (material != null) {
                FMLLog.log(Level.DEBUG, "Injected new Forge item material %s with ID %d.", material.name(), material.getId());
                continue;
            }
            FMLLog.log(Level.DEBUG, "Inject item failure %s with ID %d.", materialName, Item.getIdFromItem(item));
        }
    }

    public static void injectBlockBukkitMaterials() {
        for (Material material : Material.values()) {
            //if (material.getId() >= 256) // TODO
            //    continue;
            
            Material.addBlockMaterial(material);
        }
        for (Map.Entry<ResourceLocation, Block> entry : ForgeRegistries.BLOCKS.getEntries()) {
            ResourceLocation key = entry.getKey();
            Block block = entry.getValue();
            
            if (key.getResourceDomain().equals("minecraft"))
                continue;
            
            String materialName = key.toString().toUpperCase().replaceAll("(:|\\s)", "_").replaceAll("\\W", "");
            Material material = Material.addBlockMaterial(EnumHelper.addEnum(Material.class, materialName, new Class[]{Integer.TYPE}, new Object[]{Block.getIdFromBlock(block)}));
            if (material != null) {
                FMLLog.log(Level.DEBUG, "Injected new Forge block material %s with ID %d.", material.name(), material.getId());
                continue;
            }
            FMLLog.log(Level.DEBUG, "Inject block failure %s with ID %d.", materialName, Block.getIdFromBlock(block));
        }
    }

    public static void injectBiomes() {
        block0 : for (Entry<ResourceLocation, net.minecraft.world.biome.Biome> entry : ForgeRegistries.BIOMES.getEntries()) {
            String biomeName = entry.getKey().getResourcePath().toUpperCase(Locale.ENGLISH);
            for (Biome biome : Biome.values()) {
                if (biome.toString().equals(biomeName)) continue block0;
            }
            EnumHelper.addEnum(Biome.class, biomeName, new Class[0], new Object[0]);
        }
    }

    public static void injectEntityType() {
        Map NAME_MAP = ReflectionHelper.getPrivateValue(EntityType.class, null, "NAME_MAP");
        Map ID_MAP = ReflectionHelper.getPrivateValue(EntityType.class, null, "ID_MAP");
        for (Entry<String, Class<? extends Entity>> entity : EntityRegistry.entityClassMap.entrySet()) {
            String name = entity.getKey();
            String entityType = name.replace("-", "_").toUpperCase();
            int typeId = GameData.getEntityRegistry().getID(EntityRegistry.getEntry(entity.getValue()));
            EntityType bukkitType = EnumHelper.addEnum(EntityType.class, entityType, new Class[]{String.class, Class.class, Integer.TYPE, Boolean.TYPE}, new Object[]{name, CraftModdedEntity.class, typeId, false});
            NAME_MAP.put(name.toLowerCase(), bukkitType);
            ID_MAP.put((short)typeId, bukkitType);
        }
    }

    public static void registerEnchantments() {
        for (net.minecraft.enchantment.Enchantment enchantment : net.minecraft.enchantment.Enchantment.REGISTRY) {
            Enchantment.registerEnchantment(new CraftEnchantment(enchantment));
        }
        Enchantment.stopAcceptingRegistrations();
    }

    public static void registerPotions() {
        for (Potion effect : Potion.REGISTRY) {
            PotionEffectType.registerPotionEffectType(new CraftPotionEffectType(effect));
        }
        PotionEffectType.stopAcceptingRegistrations();
    }
}

