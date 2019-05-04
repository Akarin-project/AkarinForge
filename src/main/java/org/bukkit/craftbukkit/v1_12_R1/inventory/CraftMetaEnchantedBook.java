/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 */
package org.bukkit.craftbukkit.v1_12_R1.inventory;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaItem;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;

@DelegateDeserialization(value=CraftMetaItem.SerializableMeta.class)
class CraftMetaEnchantedBook
extends CraftMetaItem
implements EnchantmentStorageMeta {
    static final CraftMetaItem.ItemMetaKey STORED_ENCHANTMENTS = new CraftMetaItem.ItemMetaKey("StoredEnchantments", "stored-enchants");
    private Map<Enchantment, Integer> enchantments;

    CraftMetaEnchantedBook(CraftMetaItem meta) {
        super(meta);
        if (!(meta instanceof CraftMetaEnchantedBook)) {
            return;
        }
        CraftMetaEnchantedBook that = (CraftMetaEnchantedBook)meta;
        if (that.hasEnchants()) {
            this.enchantments = new HashMap<Enchantment, Integer>(that.enchantments);
        }
    }

    CraftMetaEnchantedBook(fy tag) {
        super(tag);
        if (!tag.e(CraftMetaEnchantedBook.STORED_ENCHANTMENTS.NBT)) {
            return;
        }
        this.enchantments = CraftMetaEnchantedBook.buildEnchantments(tag, STORED_ENCHANTMENTS);
    }

    CraftMetaEnchantedBook(Map<String, Object> map) {
        super(map);
        this.enchantments = CraftMetaEnchantedBook.buildEnchantments(map, STORED_ENCHANTMENTS);
    }

    @Override
    void applyToItem(fy itemTag) {
        super.applyToItem(itemTag);
        CraftMetaEnchantedBook.applyEnchantments(this.enchantments, itemTag, STORED_ENCHANTMENTS);
    }

    @Override
    boolean applicableTo(Material type) {
        switch (type) {
            case ENCHANTED_BOOK: {
                return true;
            }
        }
        return false;
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isEnchantedEmpty();
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaEnchantedBook) {
            CraftMetaEnchantedBook that = (CraftMetaEnchantedBook)meta;
            return this.hasStoredEnchants() ? that.hasStoredEnchants() && this.enchantments.equals(that.enchantments) : !that.hasStoredEnchants();
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaEnchantedBook || this.isEnchantedEmpty());
    }

    @Override
    int applyHash() {
        int original;
        int hash = original = super.applyHash();
        if (this.hasStoredEnchants()) {
            hash = 61 * hash + this.enchantments.hashCode();
        }
        return original != hash ? CraftMetaEnchantedBook.class.hashCode() ^ hash : hash;
    }

    @Override
    public CraftMetaEnchantedBook clone() {
        CraftMetaEnchantedBook meta = (CraftMetaEnchantedBook)super.clone();
        if (this.enchantments != null) {
            meta.enchantments = new HashMap<Enchantment, Integer>(this.enchantments);
        }
        return meta;
    }

    @Override
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);
        CraftMetaEnchantedBook.serializeEnchantments(this.enchantments, builder, STORED_ENCHANTMENTS);
        return builder;
    }

    boolean isEnchantedEmpty() {
        return !this.hasStoredEnchants();
    }

    @Override
    public boolean hasStoredEnchant(Enchantment ench) {
        return this.hasStoredEnchants() && this.enchantments.containsKey(ench);
    }

    @Override
    public int getStoredEnchantLevel(Enchantment ench) {
        Integer level;
        Integer n2 = level = this.hasStoredEnchants() ? this.enchantments.get(ench) : null;
        if (level == null) {
            return 0;
        }
        return level;
    }

    @Override
    public Map<Enchantment, Integer> getStoredEnchants() {
        return this.hasStoredEnchants() ? ImmutableMap.copyOf(this.enchantments) : ImmutableMap.of();
    }

    @Override
    public boolean addStoredEnchant(Enchantment ench, int level, boolean ignoreRestrictions) {
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
    public boolean removeStoredEnchant(Enchantment ench) {
        return this.hasStoredEnchants() && this.enchantments.remove(ench) != null;
    }

    @Override
    public boolean hasStoredEnchants() {
        return this.enchantments != null && !this.enchantments.isEmpty();
    }

    @Override
    public boolean hasConflictingStoredEnchant(Enchantment ench) {
        return CraftMetaEnchantedBook.checkConflictingEnchants(this.enchantments, ench);
    }

}

