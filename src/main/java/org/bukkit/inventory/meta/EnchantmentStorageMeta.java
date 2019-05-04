/*
 * Akarin Forge
 */
package org.bukkit.inventory.meta;

import java.util.Map;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;

public interface EnchantmentStorageMeta
extends ItemMeta {
    public boolean hasStoredEnchants();

    public boolean hasStoredEnchant(Enchantment var1);

    public int getStoredEnchantLevel(Enchantment var1);

    public Map<Enchantment, Integer> getStoredEnchants();

    public boolean addStoredEnchant(Enchantment var1, int var2, boolean var3);

    public boolean removeStoredEnchant(Enchantment var1) throws IllegalArgumentException;

    public boolean hasConflictingStoredEnchant(Enchantment var1);

    @Override
    public EnchantmentStorageMeta clone();
}

