/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  org.apache.commons.lang.Validate
 */
package org.bukkit.inventory;

import com.google.common.collect.ImmutableMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

public class ItemStack
implements Cloneable,
ConfigurationSerializable {
    private int type = 0;
    private int amount = 0;
    private MaterialData data = null;
    private short durability = 0;
    private ItemMeta meta;

    protected ItemStack() {
    }

    @Deprecated
    public ItemStack(int type) {
        this(type, 1);
    }

    public ItemStack(Material type) {
        this(type, 1);
    }

    @Deprecated
    public ItemStack(int type, int amount) {
        this(type, amount, 0);
    }

    public ItemStack(Material type, int amount) {
        this(type.getId(), amount);
    }

    @Deprecated
    public ItemStack(int type, int amount, short damage) {
        this.type = type;
        this.amount = amount;
        this.durability = damage;
    }

    public ItemStack(Material type, int amount, short damage) {
        this(type.getId(), amount, damage);
    }

    @Deprecated
    public ItemStack(int type, int amount, short damage, Byte data) {
        this.type = type;
        this.amount = amount;
        this.durability = damage;
        if (data != null) {
            this.createData(data.byteValue());
            this.durability = data.byteValue();
        }
    }

    @Deprecated
    public ItemStack(Material type, int amount, short damage, Byte data) {
        this(type.getId(), amount, damage, data);
    }

    public ItemStack(ItemStack stack) throws IllegalArgumentException {
        Validate.notNull((Object)stack, (String)"Cannot copy null stack");
        this.type = stack.getTypeId();
        this.amount = stack.getAmount();
        this.durability = stack.getDurability();
        this.data = stack.getData();
        if (stack.hasItemMeta()) {
            this.setItemMeta0(stack.getItemMeta(), this.getType0());
        }
    }

    public Material getType() {
        return ItemStack.getType0(this.getTypeId());
    }

    private Material getType0() {
        return ItemStack.getType0(this.type);
    }

    private static Material getType0(int id2) {
        Material material = Material.getMaterial(id2);
        return material == null ? Material.AIR : material;
    }

    public void setType(Material type) {
        Validate.notNull((Object)((Object)type), (String)"Material cannot be null");
        this.setTypeId(type.getId());
    }

    @Deprecated
    public int getTypeId() {
        return this.type;
    }

    @Deprecated
    public void setTypeId(int type) {
        this.type = type;
        if (this.meta != null) {
            this.meta = Bukkit.getItemFactory().asMetaFor(this.meta, this.getType0());
        }
        this.createData(0);
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public MaterialData getData() {
        Material mat = this.getType();
        if (this.data == null && mat != null && mat.getData() != null) {
            this.data = mat.getNewData((byte)this.getDurability());
        }
        return this.data;
    }

    public void setData(MaterialData data) {
        Material mat = this.getType();
        if (data == null || mat == null || mat.getData() == null) {
            this.data = data;
        } else if (data.getClass() == mat.getData() || data.getClass() == MaterialData.class) {
            this.data = data;
        } else {
            throw new IllegalArgumentException("Provided data is not of type " + mat.getData().getName() + ", found " + data.getClass().getName());
        }
    }

    public void setDurability(short durability) {
        this.durability = durability;
    }

    public short getDurability() {
        return this.durability;
    }

    public int getMaxStackSize() {
        Material material = this.getType();
        if (material != null) {
            return material.getMaxStackSize();
        }
        return -1;
    }

    private void createData(byte data) {
        Material mat = Material.getMaterial(this.type);
        this.data = mat == null ? new MaterialData(this.type, data) : mat.getNewData(data);
    }

    public String toString() {
        StringBuilder toString = new StringBuilder("ItemStack{").append(this.getType().name()).append(" x ").append(this.getAmount());
        if (this.hasItemMeta()) {
            toString.append(", ").append(this.getItemMeta());
        }
        return toString.append('}').toString();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ItemStack)) {
            return false;
        }
        ItemStack stack = (ItemStack)obj;
        return this.getAmount() == stack.getAmount() && this.isSimilar(stack);
    }

    public boolean isSimilar(ItemStack stack) {
        if (stack == null) {
            return false;
        }
        if (stack == this) {
            return true;
        }
        return this.getTypeId() == stack.getTypeId() && this.getDurability() == stack.getDurability() && this.hasItemMeta() == stack.hasItemMeta() && (!this.hasItemMeta() || Bukkit.getItemFactory().equals(this.getItemMeta(), stack.getItemMeta()));
    }

    public ItemStack clone() {
        try {
            ItemStack itemStack = (ItemStack)super.clone();
            if (this.meta != null) {
                itemStack.meta = this.meta.clone();
            }
            if (this.data != null) {
                itemStack.data = this.data.clone();
            }
            return itemStack;
        }
        catch (CloneNotSupportedException e2) {
            throw new Error(e2);
        }
    }

    public int hashCode() {
        int hash = 1;
        hash = hash * 31 + this.getTypeId();
        hash = hash * 31 + this.getAmount();
        hash = hash * 31 + (this.getDurability() & 65535);
        hash = hash * 31 + (this.hasItemMeta() ? (this.meta == null ? this.getItemMeta().hashCode() : this.meta.hashCode()) : 0);
        return hash;
    }

    public boolean containsEnchantment(Enchantment ench) {
        return this.meta == null ? false : this.meta.hasEnchant(ench);
    }

    public int getEnchantmentLevel(Enchantment ench) {
        return this.meta == null ? 0 : this.meta.getEnchantLevel(ench);
    }

    public Map<Enchantment, Integer> getEnchantments() {
        return this.meta == null ? ImmutableMap.of() : this.meta.getEnchants();
    }

    public void addEnchantments(Map<Enchantment, Integer> enchantments) {
        Validate.notNull(enchantments, (String)"Enchantments cannot be null");
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            this.addEnchantment(entry.getKey(), entry.getValue());
        }
    }

    public void addEnchantment(Enchantment ench, int level) {
        Validate.notNull((Object)ench, (String)"Enchantment cannot be null");
        if (level < ench.getStartLevel() || level > ench.getMaxLevel()) {
            throw new IllegalArgumentException("Enchantment level is either too low or too high (given " + level + ", bounds are " + ench.getStartLevel() + " to " + ench.getMaxLevel() + ")");
        }
        if (!ench.canEnchantItem(this)) {
            throw new IllegalArgumentException("Specified enchantment cannot be applied to this itemstack");
        }
        this.addUnsafeEnchantment(ench, level);
    }

    public void addUnsafeEnchantments(Map<Enchantment, Integer> enchantments) {
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            this.addUnsafeEnchantment(entry.getKey(), entry.getValue());
        }
    }

    public void addUnsafeEnchantment(Enchantment ench, int level) {
        ItemMeta itemMeta = this.meta == null ? (this.meta = Bukkit.getItemFactory().getItemMeta(this.getType0())) : this.meta;
        itemMeta.addEnchant(ench, level, true);
    }

    public int removeEnchantment(Enchantment ench) {
        int level = this.getEnchantmentLevel(ench);
        if (level == 0 || this.meta == null) {
            return level;
        }
        this.meta.removeEnchant(ench);
        return level;
    }

    @Override
    public Map<String, Object> serialize() {
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("type", this.getType().name());
        if (this.getDurability() != 0) {
            result.put("damage", this.getDurability());
        }
        if (this.getAmount() != 1) {
            result.put("amount", this.getAmount());
        }
        ItemMeta meta = this.getItemMeta();
        if (!Bukkit.getItemFactory().equals(meta, null)) {
            result.put("meta", meta);
        }
        return result;
    }

    public static ItemStack deserialize(Map<String, Object> args) {
        Object raw;
        Material type = Material.getMaterial((String)args.get("type"));
        short damage = 0;
        int amount = 1;
        if (args.containsKey("damage")) {
            damage = ((Number)args.get("damage")).shortValue();
        }
        if (args.containsKey("amount")) {
            amount = ((Number)args.get("amount")).intValue();
        }
        ItemStack result = new ItemStack(type, amount, damage);
        if (args.containsKey("enchantments")) {
            Object raw2 = args.get("enchantments");
            if (raw2 instanceof Map) {
                Map map = (Map)raw2;
                for (Map.Entry entry : map.entrySet()) {
                    Enchantment enchantment = Enchantment.getByName(entry.getKey().toString());
                    if (enchantment == null || !(entry.getValue() instanceof Integer)) continue;
                    result.addUnsafeEnchantment(enchantment, (Integer)entry.getValue());
                }
            }
        } else if (args.containsKey("meta") && (raw = args.get("meta")) instanceof ItemMeta) {
            result.setItemMeta((ItemMeta)raw);
        }
        return result;
    }

    public ItemMeta getItemMeta() {
        return this.meta == null ? Bukkit.getItemFactory().getItemMeta(this.getType0()) : this.meta.clone();
    }

    public boolean hasItemMeta() {
        return !Bukkit.getItemFactory().equals(this.meta, null);
    }

    public boolean setItemMeta(ItemMeta itemMeta) {
        return this.setItemMeta0(itemMeta, this.getType0());
    }

    private boolean setItemMeta0(ItemMeta itemMeta, Material material) {
        if (itemMeta == null) {
            this.meta = null;
            return true;
        }
        if (!Bukkit.getItemFactory().isApplicable(itemMeta, material)) {
            return false;
        }
        this.meta = Bukkit.getItemFactory().asMetaFor(itemMeta, material);
        if (this.meta == itemMeta) {
            this.meta = itemMeta.clone();
        }
        return true;
    }
}

