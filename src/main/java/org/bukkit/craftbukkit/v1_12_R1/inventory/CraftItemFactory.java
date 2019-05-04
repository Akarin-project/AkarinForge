/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 *  com.google.common.collect.ImmutableSet$Builder
 *  org.apache.commons.lang3.Validate
 */
package org.bukkit.craftbukkit.v1_12_R1.inventory;

import com.google.common.collect.ImmutableSet;
import java.util.Collection;
import org.apache.commons.lang3.Validate;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaBanner;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaBlockState;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaBook;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaBookSigned;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaCharge;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaEnchantedBook;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaFirework;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaItem;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaKnowledgeBook;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaLeatherArmor;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaMap;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaPotion;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaSkull;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaSpawnEgg;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class CraftItemFactory
implements ItemFactory {
    static final Color DEFAULT_LEATHER_COLOR = Color.fromRGB(10511680);
    static final Collection<String> KNOWN_NBT_ATTRIBUTE_NAMES;
    private static final CraftItemFactory instance;

    private CraftItemFactory() {
    }

    @Override
    public boolean isApplicable(ItemMeta meta, ItemStack itemstack) {
        if (itemstack == null) {
            return false;
        }
        return this.isApplicable(meta, itemstack.getType());
    }

    @Override
    public boolean isApplicable(ItemMeta meta, Material type) {
        if (type == null || meta == null) {
            return false;
        }
        if (!(meta instanceof CraftMetaItem)) {
            throw new IllegalArgumentException("Meta of " + meta.getClass().toString() + " not created by " + CraftItemFactory.class.getName());
        }
        return ((CraftMetaItem)meta).applicableTo(type);
    }

    @Override
    public ItemMeta getItemMeta(Material material) {
        Validate.notNull((Object)((Object)material), (String)"Material cannot be null", (Object[])new Object[0]);
        return this.getItemMeta(material, null);
    }

    private ItemMeta getItemMeta(Material material, CraftMetaItem meta) {
        switch (material) {
            case AIR: {
                return null;
            }
            case WRITTEN_BOOK: {
                return meta instanceof CraftMetaBookSigned ? meta : new CraftMetaBookSigned(meta);
            }
            case BOOK_AND_QUILL: {
                return meta != null && meta.getClass().equals(CraftMetaBook.class) ? meta : new CraftMetaBook(meta);
            }
            case SKULL_ITEM: {
                return meta instanceof CraftMetaSkull ? meta : new CraftMetaSkull(meta);
            }
            case LEATHER_HELMET: 
            case LEATHER_CHESTPLATE: 
            case LEATHER_LEGGINGS: 
            case LEATHER_BOOTS: {
                return meta instanceof CraftMetaLeatherArmor ? meta : new CraftMetaLeatherArmor(meta);
            }
            case POTION: 
            case SPLASH_POTION: 
            case LINGERING_POTION: 
            case TIPPED_ARROW: {
                return meta instanceof CraftMetaPotion ? meta : new CraftMetaPotion(meta);
            }
            case MAP: {
                return meta instanceof CraftMetaMap ? meta : new CraftMetaMap(meta);
            }
            case FIREWORK: {
                return meta instanceof CraftMetaFirework ? meta : new CraftMetaFirework(meta);
            }
            case FIREWORK_CHARGE: {
                return meta instanceof CraftMetaCharge ? meta : new CraftMetaCharge(meta);
            }
            case ENCHANTED_BOOK: {
                return meta instanceof CraftMetaEnchantedBook ? meta : new CraftMetaEnchantedBook(meta);
            }
            case BANNER: {
                return meta instanceof CraftMetaBanner ? meta : new CraftMetaBanner(meta);
            }
            case MONSTER_EGG: {
                return meta instanceof CraftMetaSpawnEgg ? meta : new CraftMetaSpawnEgg(meta);
            }
            case KNOWLEDGE_BOOK: {
                return meta instanceof CraftMetaKnowledgeBook ? meta : new CraftMetaKnowledgeBook(meta);
            }
            case FURNACE: 
            case CHEST: 
            case TRAPPED_CHEST: 
            case JUKEBOX: 
            case DISPENSER: 
            case DROPPER: 
            case SIGN: 
            case MOB_SPAWNER: 
            case NOTE_BLOCK: 
            case BREWING_STAND_ITEM: 
            case ENCHANTMENT_TABLE: 
            case COMMAND: 
            case COMMAND_REPEATING: 
            case COMMAND_CHAIN: 
            case BEACON: 
            case DAYLIGHT_DETECTOR: 
            case DAYLIGHT_DETECTOR_INVERTED: 
            case HOPPER: 
            case REDSTONE_COMPARATOR: 
            case FLOWER_POT_ITEM: 
            case SHIELD: 
            case STRUCTURE_BLOCK: 
            case WHITE_SHULKER_BOX: 
            case ORANGE_SHULKER_BOX: 
            case MAGENTA_SHULKER_BOX: 
            case LIGHT_BLUE_SHULKER_BOX: 
            case YELLOW_SHULKER_BOX: 
            case LIME_SHULKER_BOX: 
            case PINK_SHULKER_BOX: 
            case GRAY_SHULKER_BOX: 
            case SILVER_SHULKER_BOX: 
            case CYAN_SHULKER_BOX: 
            case PURPLE_SHULKER_BOX: 
            case BLUE_SHULKER_BOX: 
            case BROWN_SHULKER_BOX: 
            case GREEN_SHULKER_BOX: 
            case RED_SHULKER_BOX: 
            case BLACK_SHULKER_BOX: 
            case ENDER_CHEST: {
                return new CraftMetaBlockState(meta, material);
            }
        }
        return new CraftMetaItem(meta);
    }

    @Override
    public boolean equals(ItemMeta meta1, ItemMeta meta2) {
        if (meta1 == meta2) {
            return true;
        }
        if (meta1 != null && !(meta1 instanceof CraftMetaItem)) {
            throw new IllegalArgumentException("First meta of " + meta1.getClass().getName() + " does not belong to " + CraftItemFactory.class.getName());
        }
        if (meta2 != null && !(meta2 instanceof CraftMetaItem)) {
            throw new IllegalArgumentException("Second meta " + meta2.getClass().getName() + " does not belong to " + CraftItemFactory.class.getName());
        }
        if (meta1 == null) {
            return ((CraftMetaItem)meta2).isEmpty();
        }
        if (meta2 == null) {
            return ((CraftMetaItem)meta1).isEmpty();
        }
        return this.equals((CraftMetaItem)meta1, (CraftMetaItem)meta2);
    }

    boolean equals(CraftMetaItem meta1, CraftMetaItem meta2) {
        return meta1.equalsCommon(meta2) && meta1.notUncommon(meta2) && meta2.notUncommon(meta1);
    }

    public static CraftItemFactory instance() {
        return instance;
    }

    @Override
    public ItemMeta asMetaFor(ItemMeta meta, ItemStack stack) {
        Validate.notNull((Object)stack, (String)"Stack cannot be null", (Object[])new Object[0]);
        return this.asMetaFor(meta, stack.getType());
    }

    @Override
    public ItemMeta asMetaFor(ItemMeta meta, Material material) {
        Validate.notNull((Object)((Object)material), (String)"Material cannot be null", (Object[])new Object[0]);
        if (!(meta instanceof CraftMetaItem)) {
            throw new IllegalArgumentException("Meta of " + (meta != null ? meta.getClass().toString() : "null") + " not created by " + CraftItemFactory.class.getName());
        }
        return this.getItemMeta(material, (CraftMetaItem)meta);
    }

    @Override
    public Color getDefaultLeatherColor() {
        return DEFAULT_LEATHER_COLOR;
    }

    static {
        instance = new CraftItemFactory();
        ConfigurationSerialization.registerClass(CraftMetaItem.SerializableMeta.class);
        KNOWN_NBT_ATTRIBUTE_NAMES = ImmutableSet.builder().add((Object)"generic.armor").add((Object)"generic.armorToughness").add((Object)"generic.attackDamage").add((Object)"generic.followRange").add((Object)"generic.knockbackResistance").add((Object)"generic.maxHealth").add((Object)"generic.movementSpeed").add((Object)"generic.flyingSpeed").add((Object)"generic.attackSpeed").add((Object)"generic.luck").add((Object)"horse.jumpStrength").add((Object)"zombie.spawnReinforcements").add((Object)"generic.reachDistance").build();
    }

}

