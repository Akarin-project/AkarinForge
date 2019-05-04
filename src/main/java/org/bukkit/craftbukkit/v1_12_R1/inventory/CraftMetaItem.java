/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Strings
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 *  com.google.common.collect.ImmutableSet
 *  com.google.common.collect.Sets
 *  org.apache.commons.codec.binary.Base64
 *  org.apache.commons.lang3.Validate
 */
package org.bukkit.craftbukkit.v1_12_R1.inventory;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.Validate;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.craftbukkit.v1_12_R1.Overridden;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemFactory;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaBanner;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaBlockState;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaBook;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaBookSigned;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaCharge;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaEnchantedBook;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaFirework;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaKnowledgeBook;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaLeatherArmor;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaMap;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaPotion;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaSkull;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaSpawnEgg;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;

@DelegateDeserialization(value=SerializableMeta.class)
class CraftMetaItem
implements ItemMeta,
Repairable {
    static final ItemMetaKey NAME = new ItemMetaKey("Name", "display-name");
    static final ItemMetaKey LOCNAME = new ItemMetaKey("LocName", "loc-name");
    static final ItemMetaKey DISPLAY = new ItemMetaKey("display");
    static final ItemMetaKey LORE = new ItemMetaKey("Lore", "lore");
    static final ItemMetaKey ENCHANTMENTS = new ItemMetaKey("ench", "enchants");
    static final ItemMetaKey ENCHANTMENTS_ID = new ItemMetaKey("id");
    static final ItemMetaKey ENCHANTMENTS_LVL = new ItemMetaKey("lvl");
    static final ItemMetaKey REPAIR = new ItemMetaKey("RepairCost", "repair-cost");
    static final ItemMetaKey ATTRIBUTES = new ItemMetaKey("AttributeModifiers");
    static final ItemMetaKey ATTRIBUTES_IDENTIFIER = new ItemMetaKey("AttributeName");
    static final ItemMetaKey ATTRIBUTES_NAME = new ItemMetaKey("Name");
    static final ItemMetaKey ATTRIBUTES_VALUE = new ItemMetaKey("Amount");
    static final ItemMetaKey ATTRIBUTES_TYPE = new ItemMetaKey("Operation");
    static final ItemMetaKey ATTRIBUTES_UUID_HIGH = new ItemMetaKey("UUIDMost");
    static final ItemMetaKey ATTRIBUTES_UUID_LOW = new ItemMetaKey("UUIDLeast");
    static final ItemMetaKey HIDEFLAGS = new ItemMetaKey("HideFlags", "ItemFlags");
    static final ItemMetaKey UNBREAKABLE = new ItemMetaKey("Unbreakable");
    private String displayName;
    private String locName;
    private List<String> lore;
    private Map<Enchantment, Integer> enchantments;
    private int repairCost;
    private int hideFlag;
    private boolean unbreakable;
    private static final Set<String> HANDLED_TAGS = Sets.newHashSet();
    private fy internalTag;
    private final Map<String, gn> unhandledTags = new HashMap<String, gn>();
    private final ItemMeta.Spigot spigot;

    CraftMetaItem(CraftMetaItem meta) {
        this.spigot = new ItemMeta.Spigot(){

            @Override
            public void setUnbreakable(boolean setUnbreakable) {
                CraftMetaItem.this.setUnbreakable(setUnbreakable);
            }

            @Override
            public boolean isUnbreakable() {
                return CraftMetaItem.this.unbreakable;
            }
        };
        if (meta == null) {
            return;
        }
        this.displayName = meta.displayName;
        this.locName = meta.locName;
        if (meta.hasLore()) {
            this.lore = new ArrayList<String>(meta.lore);
        }
        if (meta.hasEnchants()) {
            this.enchantments = new HashMap<Enchantment, Integer>(meta.enchantments);
        }
        this.repairCost = meta.repairCost;
        this.hideFlag = meta.hideFlag;
        this.unbreakable = meta.unbreakable;
        this.unhandledTags.putAll(meta.unhandledTags);
        this.internalTag = meta.internalTag;
        if (this.internalTag != null) {
            this.deserializeInternal(this.internalTag);
        }
    }

    CraftMetaItem(fy tag) {
        this.spigot = new ;
        if (tag.e(CraftMetaItem.DISPLAY.NBT)) {
            fy display = tag.p(CraftMetaItem.DISPLAY.NBT);
            if (display.e(CraftMetaItem.NAME.NBT)) {
                this.displayName = display.l(CraftMetaItem.NAME.NBT);
            }
            if (display.e(CraftMetaItem.LOCNAME.NBT)) {
                this.locName = display.l(CraftMetaItem.LOCNAME.NBT);
            }
            if (display.e(CraftMetaItem.LORE.NBT)) {
                ge list = display.c(CraftMetaItem.LORE.NBT, 8);
                this.lore = new ArrayList<String>(list.c());
                for (int index = 0; index < list.c(); ++index) {
                    String line = list.h(index);
                    this.lore.add(line);
                }
            }
        }
        this.enchantments = CraftMetaItem.buildEnchantments(tag, ENCHANTMENTS);
        if (tag.e(CraftMetaItem.REPAIR.NBT)) {
            this.repairCost = tag.h(CraftMetaItem.REPAIR.NBT);
        }
        if (tag.e(CraftMetaItem.HIDEFLAGS.NBT)) {
            this.hideFlag = tag.h(CraftMetaItem.HIDEFLAGS.NBT);
        }
        if (tag.e(CraftMetaItem.UNBREAKABLE.NBT)) {
            this.unbreakable = tag.q(CraftMetaItem.UNBREAKABLE.NBT);
        }
        if (tag.c(CraftMetaItem.ATTRIBUTES.NBT) instanceof ge) {
            ge save = null;
            ge nbttaglist = tag.c(CraftMetaItem.ATTRIBUTES.NBT, 10);
            for (int i2 = 0; i2 < nbttaglist.c(); ++i2) {
                fy nbttagcompound;
                if (!(nbttaglist.i(i2) instanceof fy) || !(nbttagcompound = (fy)nbttaglist.i(i2)).b(CraftMetaItem.ATTRIBUTES_UUID_HIGH.NBT, 99) || !nbttagcompound.b(CraftMetaItem.ATTRIBUTES_UUID_LOW.NBT, 99) || !(nbttagcompound.c(CraftMetaItem.ATTRIBUTES_IDENTIFIER.NBT) instanceof gm) || !CraftItemFactory.KNOWN_NBT_ATTRIBUTE_NAMES.contains(nbttagcompound.l(CraftMetaItem.ATTRIBUTES_IDENTIFIER.NBT)) || !(nbttagcompound.c(CraftMetaItem.ATTRIBUTES_NAME.NBT) instanceof gm) || nbttagcompound.l(CraftMetaItem.ATTRIBUTES_NAME.NBT).isEmpty() || !nbttagcompound.b(CraftMetaItem.ATTRIBUTES_VALUE.NBT, 99) || !nbttagcompound.b(CraftMetaItem.ATTRIBUTES_TYPE.NBT, 99) || nbttagcompound.h(CraftMetaItem.ATTRIBUTES_TYPE.NBT) < 0 || nbttagcompound.h(CraftMetaItem.ATTRIBUTES_TYPE.NBT) > 2) continue;
                if (save == null) {
                    save = new ge();
                }
                fy entry = new fy();
                entry.a(CraftMetaItem.ATTRIBUTES_UUID_HIGH.NBT, nbttagcompound.c(CraftMetaItem.ATTRIBUTES_UUID_HIGH.NBT));
                entry.a(CraftMetaItem.ATTRIBUTES_UUID_LOW.NBT, nbttagcompound.c(CraftMetaItem.ATTRIBUTES_UUID_LOW.NBT));
                entry.a(CraftMetaItem.ATTRIBUTES_IDENTIFIER.NBT, nbttagcompound.c(CraftMetaItem.ATTRIBUTES_IDENTIFIER.NBT));
                entry.a(CraftMetaItem.ATTRIBUTES_NAME.NBT, nbttagcompound.c(CraftMetaItem.ATTRIBUTES_NAME.NBT));
                entry.a(CraftMetaItem.ATTRIBUTES_VALUE.NBT, nbttagcompound.c(CraftMetaItem.ATTRIBUTES_VALUE.NBT));
                entry.a(CraftMetaItem.ATTRIBUTES_TYPE.NBT, nbttagcompound.c(CraftMetaItem.ATTRIBUTES_TYPE.NBT));
                save.a(entry);
            }
            this.unhandledTags.put(CraftMetaItem.ATTRIBUTES.NBT, save);
        }
        Set<String> keys = tag.c();
        for (String key : keys) {
            if (CraftMetaItem.getHandledTags().contains(key)) continue;
            this.unhandledTags.put(key, tag.c(key));
        }
    }

    static Map<Enchantment, Integer> buildEnchantments(fy tag, ItemMetaKey key) {
        if (!tag.e(key.NBT)) {
            return null;
        }
        ge ench = tag.c(key.NBT, 10);
        HashMap<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>(ench.c());
        for (int i2 = 0; i2 < ench.c(); ++i2) {
            int id2 = 65535 & ((fy)ench.i(i2)).g(CraftMetaItem.ENCHANTMENTS_ID.NBT);
            int level = 65535 & ((fy)ench.i(i2)).g(CraftMetaItem.ENCHANTMENTS_LVL.NBT);
            Enchantment enchant = Enchantment.getById(id2);
            if (enchant == null) continue;
            enchantments.put(enchant, level);
        }
        return enchantments;
    }

    CraftMetaItem(Map<String, Object> map) {
        Iterable hideFlags;
        String internal;
        Boolean unbreakable;
        this.spigot = new ;
        this.setDisplayName(SerializableMeta.getString(map, CraftMetaItem.NAME.BUKKIT, true));
        this.setLocalizedName(SerializableMeta.getString(map, CraftMetaItem.LOCNAME.BUKKIT, true));
        Iterable lore = SerializableMeta.getObject(Iterable.class, map, CraftMetaItem.LORE.BUKKIT, true);
        if (lore != null) {
            this.lore = new ArrayList<String>();
            CraftMetaItem.safelyAdd(lore, this.lore, Integer.MAX_VALUE);
        }
        this.enchantments = CraftMetaItem.buildEnchantments(map, ENCHANTMENTS);
        Integer repairCost = SerializableMeta.getObject(Integer.class, map, CraftMetaItem.REPAIR.BUKKIT, true);
        if (repairCost != null) {
            this.setRepairCost(repairCost);
        }
        if ((hideFlags = SerializableMeta.getObject(Iterable.class, map, CraftMetaItem.HIDEFLAGS.BUKKIT, true)) != null) {
            for (Object hideFlagObject : hideFlags) {
                String hideFlagString = (String)hideFlagObject;
                try {
                    ItemFlag hideFlatEnum = ItemFlag.valueOf(hideFlagString);
                    this.addItemFlags(hideFlatEnum);
                }
                catch (IllegalArgumentException hideFlatEnum) {}
            }
        }
        if ((unbreakable = SerializableMeta.getObject(Boolean.class, map, CraftMetaItem.UNBREAKABLE.BUKKIT, true)) != null) {
            this.setUnbreakable(unbreakable);
        }
        if ((internal = SerializableMeta.getString(map, "internal", true)) != null) {
            ByteArrayInputStream buf = new ByteArrayInputStream(Base64.decodeBase64((String)internal));
            try {
                this.internalTag = gi.a(buf);
                this.deserializeInternal(this.internalTag);
                Set<String> keys = this.internalTag.c();
                for (String key : keys) {
                    if (CraftMetaItem.getHandledTags().contains(key)) continue;
                    this.unhandledTags.put(key, this.internalTag.c(key));
                }
            }
            catch (IOException ex2) {
                Logger.getLogger(CraftMetaItem.class.getName()).log(Level.SEVERE, null, ex2);
            }
        }
    }

    void deserializeInternal(fy tag) {
    }

    static Map<Enchantment, Integer> buildEnchantments(Map<String, Object> map, ItemMetaKey key) {
        Map ench = SerializableMeta.getObject(Map.class, map, key.BUKKIT, true);
        if (ench == null) {
            return null;
        }
        HashMap<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>(ench.size());
        for (Map.Entry entry : ench.entrySet()) {
            Enchantment enchantment;
            String enchantKey = entry.getKey().toString();
            if (enchantKey.equals("SWEEPING")) {
                enchantKey = "SWEEPING_EDGE";
            }
            if ((enchantment = Enchantment.getByName(enchantKey)) == null || !(entry.getValue() instanceof Integer)) continue;
            enchantments.put(enchantment, (Integer)entry.getValue());
        }
        return enchantments;
    }

    @Overridden
    void applyToItem(fy itemTag) {
        if (this.hasDisplayName()) {
            this.setDisplayTag(itemTag, CraftMetaItem.NAME.NBT, new gm(this.displayName));
        }
        if (this.hasLocalizedName()) {
            this.setDisplayTag(itemTag, CraftMetaItem.LOCNAME.NBT, new gm(this.locName));
        }
        if (this.hasLore()) {
            this.setDisplayTag(itemTag, CraftMetaItem.LORE.NBT, CraftMetaItem.createStringList(this.lore));
        }
        if (this.hideFlag != 0) {
            itemTag.a(CraftMetaItem.HIDEFLAGS.NBT, this.hideFlag);
        }
        CraftMetaItem.applyEnchantments(this.enchantments, itemTag, ENCHANTMENTS);
        if (this.hasRepairCost()) {
            itemTag.a(CraftMetaItem.REPAIR.NBT, this.repairCost);
        }
        if (this.isUnbreakable()) {
            itemTag.a(CraftMetaItem.UNBREAKABLE.NBT, this.unbreakable);
        }
        for (Map.Entry<String, gn> e2 : this.unhandledTags.entrySet()) {
            itemTag.a(e2.getKey(), e2.getValue());
        }
    }

    static ge createStringList(List<String> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        ge tagList = new ge();
        for (String value : list) {
            tagList.a(new gm(value));
        }
        return tagList;
    }

    static void applyEnchantments(Map<Enchantment, Integer> enchantments, fy tag, ItemMetaKey key) {
        if (enchantments == null || enchantments.size() == 0) {
            return;
        }
        ge list = new ge();
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            fy subtag = new fy();
            subtag.a(CraftMetaItem.ENCHANTMENTS_ID.NBT, (short)entry.getKey().getId());
            subtag.a(CraftMetaItem.ENCHANTMENTS_LVL.NBT, entry.getValue().shortValue());
            list.a(subtag);
        }
        tag.a(key.NBT, list);
    }

    void setDisplayTag(fy tag, String key, gn value) {
        fy display = tag.p(CraftMetaItem.DISPLAY.NBT);
        if (!tag.e(CraftMetaItem.DISPLAY.NBT)) {
            tag.a(CraftMetaItem.DISPLAY.NBT, display);
        }
        display.a(key, value);
    }

    @Overridden
    boolean applicableTo(Material type) {
        return type != Material.AIR;
    }

    @Overridden
    boolean isEmpty() {
        return !this.hasDisplayName() && !this.hasLocalizedName() && !this.hasEnchants() && !this.hasLore() && !this.hasRepairCost() && this.unhandledTags.isEmpty() && this.hideFlag == 0 && !this.isUnbreakable();
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public final void setDisplayName(String name) {
        this.displayName = name;
    }

    @Override
    public boolean hasDisplayName() {
        return !Strings.isNullOrEmpty((String)this.displayName);
    }

    @Override
    public String getLocalizedName() {
        return this.locName;
    }

    @Override
    public void setLocalizedName(String name) {
        this.locName = name;
    }

    @Override
    public boolean hasLocalizedName() {
        return !Strings.isNullOrEmpty((String)this.locName);
    }

    @Override
    public boolean hasLore() {
        return this.lore != null && !this.lore.isEmpty();
    }

    @Override
    public boolean hasRepairCost() {
        return this.repairCost > 0;
    }

    @Override
    public boolean hasEnchant(Enchantment ench) {
        Validate.notNull((Object)ench, (String)"Enchantment cannot be null", (Object[])new Object[0]);
        return this.hasEnchants() && this.enchantments.containsKey(ench);
    }

    @Override
    public int getEnchantLevel(Enchantment ench) {
        Integer level;
        Validate.notNull((Object)ench, (String)"Enchantment cannot be null", (Object[])new Object[0]);
        Integer n2 = level = this.hasEnchants() ? this.enchantments.get(ench) : null;
        if (level == null) {
            return 0;
        }
        return level;
    }

    @Override
    public Map<Enchantment, Integer> getEnchants() {
        return this.hasEnchants() ? ImmutableMap.copyOf(this.enchantments) : ImmutableMap.of();
    }

    @Override
    public boolean addEnchant(Enchantment ench, int level, boolean ignoreRestrictions) {
        Validate.notNull((Object)ench, (String)"Enchantment cannot be null", (Object[])new Object[0]);
        if (this.enchantments == null) {
            this.enchantments = new HashMap<Enchantment, Integer>(4);
        }
        if (ignoreRestrictions || level >= ench.getStartLevel() && level <= ench.getMaxLevel()) {
            Integer old = this.enchantments.put(ench, level);
            return old == null || old != level;
        }
        return false;
    }

    @Override
    public boolean removeEnchant(Enchantment ench) {
        Validate.notNull((Object)ench, (String)"Enchantment cannot be null", (Object[])new Object[0]);
        return this.hasEnchants() && this.enchantments.remove(ench) != null;
    }

    @Override
    public boolean hasEnchants() {
        return this.enchantments != null && !this.enchantments.isEmpty();
    }

    @Override
    public boolean hasConflictingEnchant(Enchantment ench) {
        return CraftMetaItem.checkConflictingEnchants(this.enchantments, ench);
    }

    @Override
    public /* varargs */ void addItemFlags(ItemFlag ... hideFlags) {
        for (ItemFlag f2 : hideFlags) {
            this.hideFlag |= this.getBitModifier(f2);
        }
    }

    @Override
    public /* varargs */ void removeItemFlags(ItemFlag ... hideFlags) {
        for (ItemFlag f2 : hideFlags) {
            this.hideFlag &= ~ this.getBitModifier(f2);
        }
    }

    @Override
    public Set<ItemFlag> getItemFlags() {
        EnumSet<ItemFlag> currentFlags = EnumSet.noneOf(ItemFlag.class);
        for (ItemFlag f2 : ItemFlag.values()) {
            if (!this.hasItemFlag(f2)) continue;
            currentFlags.add(f2);
        }
        return currentFlags;
    }

    @Override
    public boolean hasItemFlag(ItemFlag flag) {
        byte bitModifier = this.getBitModifier(flag);
        return (this.hideFlag & bitModifier) == bitModifier;
    }

    private byte getBitModifier(ItemFlag hideFlag) {
        return (byte)(1 << hideFlag.ordinal());
    }

    @Override
    public List<String> getLore() {
        return this.lore == null ? null : new ArrayList<String>(this.lore);
    }

    @Override
    public void setLore(List<String> lore) {
        if (lore == null) {
            this.lore = null;
        } else if (this.lore == null) {
            this.lore = new ArrayList<String>(lore.size());
            CraftMetaItem.safelyAdd(lore, this.lore, Integer.MAX_VALUE);
        } else {
            this.lore.clear();
            CraftMetaItem.safelyAdd(lore, this.lore, Integer.MAX_VALUE);
        }
    }

    @Override
    public int getRepairCost() {
        return this.repairCost;
    }

    @Override
    public void setRepairCost(int cost) {
        this.repairCost = cost;
    }

    @Override
    public boolean isUnbreakable() {
        return this.unbreakable;
    }

    @Override
    public void setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
    }

    public final boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object == this) {
            return true;
        }
        if (!(object instanceof CraftMetaItem)) {
            return false;
        }
        return CraftItemFactory.instance().equals((ItemMeta)this, (ItemMeta)object);
    }

    @Overridden
    boolean equalsCommon(CraftMetaItem that) {
        return (this.hasDisplayName() ? that.hasDisplayName() && this.displayName.equals(that.displayName) : !that.hasDisplayName()) && (this.hasLocalizedName() ? that.hasLocalizedName() && this.locName.equals(that.locName) : !that.hasLocalizedName()) && (this.hasEnchants() ? that.hasEnchants() && this.enchantments.equals(that.enchantments) : !that.hasEnchants()) && (this.hasLore() ? that.hasLore() && this.lore.equals(that.lore) : !that.hasLore()) && (this.hasRepairCost() ? that.hasRepairCost() && this.repairCost == that.repairCost : !that.hasRepairCost()) && this.unhandledTags.equals(that.unhandledTags) && this.hideFlag == that.hideFlag && this.isUnbreakable() == that.isUnbreakable();
    }

    @Overridden
    boolean notUncommon(CraftMetaItem meta) {
        return true;
    }

    public final int hashCode() {
        return this.applyHash();
    }

    @Overridden
    int applyHash() {
        int hash = 3;
        hash = 61 * hash + (this.hasDisplayName() ? this.displayName.hashCode() : 0);
        hash = 61 * hash + (this.hasLocalizedName() ? this.locName.hashCode() : 0);
        hash = 61 * hash + (this.hasLore() ? this.lore.hashCode() : 0);
        hash = 61 * hash + (this.hasEnchants() ? this.enchantments.hashCode() : 0);
        hash = 61 * hash + (this.hasRepairCost() ? this.repairCost : 0);
        hash = 61 * hash + this.unhandledTags.hashCode();
        hash = 61 * hash + this.hideFlag;
        hash = 61 * hash + (this.isUnbreakable() ? 1231 : 1237);
        return hash;
    }

    @Overridden
    @Override
    public CraftMetaItem clone() {
        try {
            CraftMetaItem clone = (CraftMetaItem)super.clone();
            if (this.lore != null) {
                clone.lore = new ArrayList<String>(this.lore);
            }
            if (this.enchantments != null) {
                clone.enchantments = new HashMap<Enchantment, Integer>(this.enchantments);
            }
            clone.hideFlag = this.hideFlag;
            clone.unbreakable = this.unbreakable;
            return clone;
        }
        catch (CloneNotSupportedException e2) {
            throw new Error(e2);
        }
    }

    @Override
    public final Map<String, Object> serialize() {
        ImmutableMap.Builder map = ImmutableMap.builder();
        map.put((Object)"meta-type", SerializableMeta.classMap.get(this.getClass()));
        this.serialize(map);
        return map.build();
    }

    @Overridden
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        if (this.hasDisplayName()) {
            builder.put((Object)CraftMetaItem.NAME.BUKKIT, (Object)this.displayName);
        }
        if (this.hasLocalizedName()) {
            builder.put((Object)CraftMetaItem.LOCNAME.BUKKIT, (Object)this.locName);
        }
        if (this.hasLore()) {
            builder.put((Object)CraftMetaItem.LORE.BUKKIT, (Object)ImmutableList.copyOf(this.lore));
        }
        CraftMetaItem.serializeEnchantments(this.enchantments, builder, ENCHANTMENTS);
        if (this.hasRepairCost()) {
            builder.put((Object)CraftMetaItem.REPAIR.BUKKIT, (Object)this.repairCost);
        }
        ArrayList<String> hideFlags = new ArrayList<String>();
        for (ItemFlag hideFlagEnum : this.getItemFlags()) {
            hideFlags.add(hideFlagEnum.name());
        }
        if (!hideFlags.isEmpty()) {
            builder.put((Object)CraftMetaItem.HIDEFLAGS.BUKKIT, hideFlags);
        }
        if (this.isUnbreakable()) {
            builder.put((Object)CraftMetaItem.UNBREAKABLE.BUKKIT, (Object)this.unbreakable);
        }
        HashMap<String, gn> internalTags = new HashMap<String, gn>(this.unhandledTags);
        this.serializeInternal(internalTags);
        if (!internalTags.isEmpty()) {
            fy internal = new fy();
            for (Map.Entry<String, gn> e2 : internalTags.entrySet()) {
                internal.a(e2.getKey(), e2.getValue());
            }
            try {
                ByteArrayOutputStream buf = new ByteArrayOutputStream();
                gi.a(internal, buf);
                builder.put((Object)"internal", (Object)Base64.encodeBase64String((byte[])buf.toByteArray()));
            }
            catch (IOException ex2) {
                Logger.getLogger(CraftMetaItem.class.getName()).log(Level.SEVERE, null, ex2);
            }
        }
        return builder;
    }

    void serializeInternal(Map<String, gn> unhandledTags) {
    }

    static void serializeEnchantments(Map<Enchantment, Integer> enchantments, ImmutableMap.Builder<String, Object> builder, ItemMetaKey key) {
        if (enchantments == null || enchantments.isEmpty()) {
            return;
        }
        ImmutableMap.Builder enchants = ImmutableMap.builder();
        for (Map.Entry<Enchantment, Integer> enchant : enchantments.entrySet()) {
            enchants.put((Object)enchant.getKey().getName(), (Object)enchant.getValue());
        }
        builder.put((Object)key.BUKKIT, (Object)enchants.build());
    }

    static void safelyAdd(Iterable<?> addFrom, Collection<String> addTo, int maxItemLength) {
        if (addFrom == null) {
            return;
        }
        for (Object object : addFrom) {
            if (!(object instanceof String)) {
                if (object != null) {
                    throw new IllegalArgumentException(addFrom + " cannot contain non-string " + object.getClass().getName());
                }
                addTo.add("");
                continue;
            }
            String page = object.toString();
            if (page.length() > maxItemLength) {
                page = page.substring(0, maxItemLength);
            }
            addTo.add(page);
        }
    }

    static boolean checkConflictingEnchants(Map<Enchantment, Integer> enchantments, Enchantment ench) {
        if (enchantments == null || enchantments.isEmpty()) {
            return false;
        }
        for (Enchantment enchant : enchantments.keySet()) {
            if (!enchant.conflictsWith(ench)) continue;
            return true;
        }
        return false;
    }

    public final String toString() {
        return (String)SerializableMeta.classMap.get(this.getClass()) + "_META:" + this.serialize();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static Set<String> getHandledTags() {
        Set<String> set = HANDLED_TAGS;
        synchronized (set) {
            if (HANDLED_TAGS.isEmpty()) {
                HANDLED_TAGS.addAll(Arrays.asList(CraftMetaItem.DISPLAY.NBT, CraftMetaItem.REPAIR.NBT, CraftMetaItem.ENCHANTMENTS.NBT, CraftMetaItem.HIDEFLAGS.NBT, CraftMetaItem.UNBREAKABLE.NBT, CraftMetaMap.MAP_SCALING.NBT, CraftMetaPotion.POTION_EFFECTS.NBT, CraftMetaPotion.DEFAULT_POTION.NBT, CraftMetaSkull.SKULL_OWNER.NBT, CraftMetaSkull.SKULL_PROFILE.NBT, CraftMetaSpawnEgg.ENTITY_TAG.NBT, CraftMetaBlockState.BLOCK_ENTITY_TAG.NBT, CraftMetaBook.BOOK_TITLE.NBT, CraftMetaBook.BOOK_AUTHOR.NBT, CraftMetaBook.BOOK_PAGES.NBT, CraftMetaBook.RESOLVED.NBT, CraftMetaBook.GENERATION.NBT, CraftMetaFirework.FIREWORKS.NBT, CraftMetaEnchantedBook.STORED_ENCHANTMENTS.NBT, CraftMetaCharge.EXPLOSION.NBT, CraftMetaBlockState.BLOCK_ENTITY_TAG.NBT, CraftMetaKnowledgeBook.BOOK_RECIPES.NBT));
            }
            return HANDLED_TAGS;
        }
    }

    @Override
    public ItemMeta.Spigot spigot() {
        return this.spigot;
    }

    @SerializableAs(value="ItemMeta")
    public static class SerializableMeta
    implements ConfigurationSerializable {
        static final String TYPE_FIELD = "meta-type";
        static final ImmutableMap<Class<? extends CraftMetaItem>, String> classMap = ImmutableMap.builder().put(CraftMetaBanner.class, (Object)"BANNER").put(CraftMetaBlockState.class, (Object)"TILE_ENTITY").put(CraftMetaBook.class, (Object)"BOOK").put(CraftMetaBookSigned.class, (Object)"BOOK_SIGNED").put(CraftMetaSkull.class, (Object)"SKULL").put(CraftMetaLeatherArmor.class, (Object)"LEATHER_ARMOR").put(CraftMetaMap.class, (Object)"MAP").put(CraftMetaPotion.class, (Object)"POTION").put(CraftMetaSpawnEgg.class, (Object)"SPAWN_EGG").put(CraftMetaEnchantedBook.class, (Object)"ENCHANTED").put(CraftMetaFirework.class, (Object)"FIREWORK").put(CraftMetaCharge.class, (Object)"FIREWORK_EFFECT").put(CraftMetaKnowledgeBook.class, (Object)"KNOWLEDGE_BOOK").put(CraftMetaItem.class, (Object)"UNSPECIFIC").build();
        static final ImmutableMap<String, Constructor<? extends CraftMetaItem>> constructorMap;

        private SerializableMeta() {
        }

        public static ItemMeta deserialize(Map<String, Object> map) throws Throwable {
            Validate.notNull(map, (String)"Cannot deserialize null map", (Object[])new Object[0]);
            String type = SerializableMeta.getString(map, "meta-type", false);
            Constructor constructor = (Constructor)constructorMap.get((Object)type);
            if (constructor == null) {
                throw new IllegalArgumentException(type + " is not a valid " + "meta-type");
            }
            try {
                return (ItemMeta)constructor.newInstance(map);
            }
            catch (InstantiationException e2) {
                throw new AssertionError(e2);
            }
            catch (IllegalAccessException e3) {
                throw new AssertionError(e3);
            }
            catch (InvocationTargetException e4) {
                throw e4.getCause();
            }
        }

        @Override
        public Map<String, Object> serialize() {
            throw new AssertionError();
        }

        static String getString(Map<?, ?> map, Object field, boolean nullable) {
            return SerializableMeta.getObject(String.class, map, field, nullable);
        }

        static boolean getBoolean(Map<?, ?> map, Object field) {
            Boolean value = SerializableMeta.getObject(Boolean.class, map, field, true);
            return value != null && value != false;
        }

        static <T> T getObject(Class<T> clazz, Map<?, ?> map, Object field, boolean nullable) {
            Object object = map.get(field);
            if (clazz.isInstance(object)) {
                return clazz.cast(object);
            }
            if (object == null) {
                if (!nullable) {
                    throw new NoSuchElementException(map + " does not contain " + field);
                }
                return null;
            }
            throw new IllegalArgumentException(field + "(" + object + ") is not a valid " + clazz);
        }

        static {
            ImmutableMap.Builder classConstructorBuilder = ImmutableMap.builder();
            for (Map.Entry mapping : classMap.entrySet()) {
                try {
                    classConstructorBuilder.put(mapping.getValue(), ((Class)mapping.getKey()).getDeclaredConstructor(Map.class));
                    continue;
                }
                catch (NoSuchMethodException e2) {
                    throw new AssertionError(e2);
                }
            }
            constructorMap = classConstructorBuilder.build();
        }
    }

    static class ItemMetaKey {
        final String BUKKIT;
        final String NBT;

        ItemMetaKey(String both) {
            this(both, both);
        }

        ItemMetaKey(String nbt, String bukkit) {
            this.NBT = nbt;
            this.BUKKIT = bukkit;
        }

        @Retention(value=RetentionPolicy.SOURCE)
        @Target(value={ElementType.FIELD})
        static @interface Specific {
            public To value();

            public static enum To {
                BUKKIT,
                NBT;
                

                private To() {
                }
            }

        }

    }

}

