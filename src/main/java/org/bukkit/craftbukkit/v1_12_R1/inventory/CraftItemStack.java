/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 *  org.apache.commons.lang3.Validate
 */
package org.bukkit.craftbukkit.v1_12_R1.inventory;

import com.google.common.collect.ImmutableMap;

import net.minecraft.item.Item;

import java.util.Map;
import org.apache.commons.lang3.Validate;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.v1_12_R1.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemFactory;
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
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

@DelegateDeserialization(value=ItemStack.class)
public final class CraftItemStack
extends ItemStack {
    aip handle;

    public static aip asNMSCopy(ItemStack original) {
        if (original instanceof CraftItemStack) {
            CraftItemStack stack = (CraftItemStack)original;
            return stack.handle == null ? aip.a : stack.handle.l();
        }
        if (original == null || original.getTypeId() <= 0) {
            return aip.a;
        }
        ain item = CraftMagicNumbers.getItem(original.getType());
        if (item == null) {
            return aip.a;
        }
        aip stack = new aip(item, original.getAmount(), (int)original.getDurability());
        if (original.hasItemMeta()) {
            CraftItemStack.setItemMeta(stack, original.getItemMeta());
        }
        return stack;
    }

    public static aip copyNMSStack(aip original, int amount) {
        aip stack = original.l();
        stack.e(amount);
        return stack;
    }

    public static ItemStack asBukkitCopy(aip original) {
        if (original.b()) {
            return new ItemStack(Material.AIR);
        }
        ItemStack stack = new ItemStack(CraftMagicNumbers.getMaterial(original.c()), original.E(), (short)original.j());
        if (CraftItemStack.hasItemMeta(original)) {
            stack.setItemMeta(CraftItemStack.getItemMeta(original));
        }
        return stack;
    }

    public static CraftItemStack asCraftMirror(aip original) {
        return new CraftItemStack(original == null || original.b() ? null : original);
    }

    public static CraftItemStack asCraftCopy(ItemStack original) {
        if (original instanceof CraftItemStack) {
            CraftItemStack stack = (CraftItemStack)original;
            return new CraftItemStack(stack.handle == null ? null : stack.handle.l());
        }
        return new CraftItemStack(original);
    }

    public static CraftItemStack asNewCraftStack(Item item) {
        return CraftItemStack.asNewCraftStack(item, 1);
    }

    public static CraftItemStack asNewCraftStack(Item item, int amount) {
        return new CraftItemStack(CraftMagicNumbers.getMaterial(item), amount, 0, null);
    }

    private CraftItemStack(aip item) {
        this.handle = item;
    }

    private CraftItemStack(ItemStack item) {
        this(item.getTypeId(), item.getAmount(), item.getDurability(), item.hasItemMeta() ? item.getItemMeta() : null);
    }

    private CraftItemStack(Material type, int amount, short durability, ItemMeta itemMeta) {
        this.setType(type);
        this.setAmount(amount);
        this.setDurability(durability);
        this.setItemMeta(itemMeta);
    }

    private CraftItemStack(int typeId, int amount, short durability, ItemMeta itemMeta) {
        this(Material.getMaterial(typeId), amount, durability, itemMeta);
    }

    @Override
    public int getTypeId() {
        return this.handle != null ? CraftMagicNumbers.getId(this.handle.c()) : 0;
    }

    @Override
    public void setTypeId(int type) {
        if (this.getTypeId() == type) {
            return;
        }
        if (type == 0) {
            this.handle = null;
        } else if (CraftMagicNumbers.getItem(type) == null) {
            this.handle = null;
        } else if (this.handle == null) {
            this.handle = new aip(CraftMagicNumbers.getItem(type), 1, 0);
        } else {
            this.handle.setItem(CraftMagicNumbers.getItem(type));
            if (this.hasItemMeta()) {
                CraftItemStack.setItemMeta(this.handle, CraftItemStack.getItemMeta(this.handle));
            }
        }
        this.setData(null);
    }

    @Override
    public int getAmount() {
        return this.handle != null ? this.handle.E() : 0;
    }

    @Override
    public void setAmount(int amount) {
        if (this.handle == null) {
            return;
        }
        this.handle.e(amount);
        if (amount == 0) {
            this.handle = null;
        }
    }

    @Override
    public void setDurability(short durability) {
        if (this.handle != null) {
            this.handle.b(durability);
        }
    }

    @Override
    public short getDurability() {
        if (this.handle != null) {
            return (short)this.handle.j();
        }
        return -1;
    }

    @Override
    public int getMaxStackSize() {
        return this.handle == null ? Material.AIR.getMaxStackSize() : this.handle.c().getItemStackLimit(this.handle);
    }

    @Override
    public void addUnsafeEnchantment(Enchantment ench, int level) {
        Validate.notNull((Object)ench, (String)"Cannot add null enchantment", (Object[])new Object[0]);
        if (!CraftItemStack.makeTag(this.handle)) {
            return;
        }
        ge list = CraftItemStack.getEnchantmentList(this.handle);
        if (list == null) {
            list = new ge();
            this.handle.p().a(CraftMetaItem.ENCHANTMENTS.NBT, list);
        }
        int size = list.c();
        for (int i2 = 0; i2 < size; ++i2) {
            fy tag = (fy)list.i(i2);
            short id2 = tag.g(CraftMetaItem.ENCHANTMENTS_ID.NBT);
            if (id2 != ench.getId()) continue;
            tag.a(CraftMetaItem.ENCHANTMENTS_LVL.NBT, (short)level);
            return;
        }
        fy tag = new fy();
        tag.a(CraftMetaItem.ENCHANTMENTS_ID.NBT, (short)ench.getId());
        tag.a(CraftMetaItem.ENCHANTMENTS_LVL.NBT, (short)level);
        list.a(tag);
    }

    static boolean makeTag(aip item) {
        if (item == null) {
            return false;
        }
        if (item.p() == null) {
            item.b(new fy());
        }
        return true;
    }

    @Override
    public boolean containsEnchantment(Enchantment ench) {
        return this.getEnchantmentLevel(ench) > 0;
    }

    @Override
    public int getEnchantmentLevel(Enchantment ench) {
        Validate.notNull((Object)ench, (String)"Cannot find null enchantment", (Object[])new Object[0]);
        if (this.handle == null) {
            return 0;
        }
        return alm.a(CraftEnchantment.getRaw(ench), this.handle);
    }

    @Override
    public int removeEnchantment(Enchantment ench) {
        int i2;
        Validate.notNull((Object)ench, (String)"Cannot remove null enchantment", (Object[])new Object[0]);
        ge list = CraftItemStack.getEnchantmentList(this.handle);
        if (list == null) {
            return 0;
        }
        int index = Integer.MIN_VALUE;
        int level = Integer.MIN_VALUE;
        int size = list.c();
        for (i2 = 0; i2 < size; ++i2) {
            fy enchantment = (fy)list.i(i2);
            int id2 = 65535 & enchantment.g(CraftMetaItem.ENCHANTMENTS_ID.NBT);
            if (id2 != ench.getId()) continue;
            index = i2;
            level = 65535 & enchantment.g(CraftMetaItem.ENCHANTMENTS_LVL.NBT);
            break;
        }
        if (index == Integer.MIN_VALUE) {
            return 0;
        }
        if (size == 1) {
            this.handle.p().r(CraftMetaItem.ENCHANTMENTS.NBT);
            if (this.handle.p().b_()) {
                this.handle.b((fy)null);
            }
            return level;
        }
        ge listCopy = new ge();
        for (i2 = 0; i2 < size; ++i2) {
            if (i2 == index) continue;
            listCopy.a(list.i(i2));
        }
        this.handle.p().a(CraftMetaItem.ENCHANTMENTS.NBT, listCopy);
        return level;
    }

    @Override
    public Map<Enchantment, Integer> getEnchantments() {
        return CraftItemStack.getEnchantments(this.handle);
    }

    static Map<Enchantment, Integer> getEnchantments(aip item) {
        ge list;
        ge ge2 = list = item != null && item.x() ? item.q() : null;
        if (list == null || list.c() == 0) {
            return ImmutableMap.of();
        }
        ImmutableMap.Builder result = ImmutableMap.builder();
        for (int i2 = 0; i2 < list.c(); ++i2) {
            int id2 = 65535 & ((fy)list.i(i2)).g(CraftMetaItem.ENCHANTMENTS_ID.NBT);
            int level = 65535 & ((fy)list.i(i2)).g(CraftMetaItem.ENCHANTMENTS_LVL.NBT);
            result.put((Object)Enchantment.getById(id2), (Object)level);
        }
        return result.build();
    }

    static ge getEnchantmentList(aip item) {
        return item != null && item.x() ? item.q() : null;
    }

    @Override
    public CraftItemStack clone() {
        CraftItemStack itemStack = (CraftItemStack)super.clone();
        if (this.handle != null) {
            itemStack.handle = this.handle.l();
        }
        return itemStack;
    }

    @Override
    public ItemMeta getItemMeta() {
        return CraftItemStack.getItemMeta(this.handle);
    }

    public static ItemMeta getItemMeta(aip item) {
        if (!CraftItemStack.hasItemMeta(item)) {
            return CraftItemFactory.instance().getItemMeta(CraftItemStack.getType(item));
        }
        switch (CraftItemStack.getType(item)) {
            case WRITTEN_BOOK: {
                return new CraftMetaBookSigned(item.p());
            }
            case BOOK_AND_QUILL: {
                return new CraftMetaBook(item.p());
            }
            case SKULL_ITEM: {
                return new CraftMetaSkull(item.p());
            }
            case LEATHER_HELMET: 
            case LEATHER_CHESTPLATE: 
            case LEATHER_LEGGINGS: 
            case LEATHER_BOOTS: {
                return new CraftMetaLeatherArmor(item.p());
            }
            case POTION: 
            case SPLASH_POTION: 
            case LINGERING_POTION: 
            case TIPPED_ARROW: {
                return new CraftMetaPotion(item.p());
            }
            case MAP: {
                return new CraftMetaMap(item.p());
            }
            case FIREWORK: {
                return new CraftMetaFirework(item.p());
            }
            case FIREWORK_CHARGE: {
                return new CraftMetaCharge(item.p());
            }
            case ENCHANTED_BOOK: {
                return new CraftMetaEnchantedBook(item.p());
            }
            case BANNER: {
                return new CraftMetaBanner(item.p());
            }
            case MONSTER_EGG: {
                return new CraftMetaSpawnEgg(item.p());
            }
            case KNOWLEDGE_BOOK: {
                return new CraftMetaKnowledgeBook(item.p());
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
                return new CraftMetaBlockState(item.p(), CraftMagicNumbers.getMaterial(item.c()));
            }
        }
        return new CraftMetaItem(item.p());
    }

    static Material getType(aip item) {
        Material material = Material.getMaterial(item == null ? 0 : CraftMagicNumbers.getId(item.c()));
        return material == null ? Material.AIR : material;
    }

    @Override
    public boolean setItemMeta(ItemMeta itemMeta) {
        return CraftItemStack.setItemMeta(this.handle, itemMeta);
    }

    public static boolean setItemMeta(aip item, ItemMeta itemMeta) {
        if (item == null) {
            return false;
        }
        if (CraftItemFactory.instance().equals(itemMeta, null)) {
            item.b((fy)null);
            return true;
        }
        if (!CraftItemFactory.instance().isApplicable(itemMeta, CraftItemStack.getType(item))) {
            return false;
        }
        itemMeta = CraftItemFactory.instance().asMetaFor(itemMeta, CraftItemStack.getType(item));
        if (itemMeta == null) {
            return true;
        }
        fy tag = new fy();
        item.b(tag);
        ((CraftMetaItem)itemMeta).applyToItem(tag);
        return true;
    }

    @Override
    public boolean isSimilar(ItemStack stack) {
        if (stack == null) {
            return false;
        }
        if (stack == this) {
            return true;
        }
        if (!(stack instanceof CraftItemStack)) {
            return stack.getClass() == ItemStack.class && stack.isSimilar(this);
        }
        CraftItemStack that = (CraftItemStack)stack;
        if (this.handle == that.handle) {
            return true;
        }
        if (this.handle == null || that.handle == null) {
            return false;
        }
        if (that.getTypeId() != this.getTypeId() || this.getDurability() != that.getDurability()) {
            return false;
        }
        return this.hasItemMeta() ? that.hasItemMeta() && this.handle.p().equals(that.handle.p()) : !that.hasItemMeta();
    }

    @Override
    public boolean hasItemMeta() {
        return CraftItemStack.hasItemMeta(this.handle);
    }

    static boolean hasItemMeta(aip item) {
        return item != null && item.p() != null && !item.p().b_();
    }

}

