/*
 * Akarin Forge
 */
package org.bukkit.inventory.meta;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

public interface ItemMeta
extends Cloneable,
ConfigurationSerializable {
    public boolean hasDisplayName();

    public String getDisplayName();

    public void setDisplayName(String var1);

    public boolean hasLocalizedName();

    public String getLocalizedName();

    public void setLocalizedName(String var1);

    public boolean hasLore();

    public List<String> getLore();

    public void setLore(List<String> var1);

    public boolean hasEnchants();

    public boolean hasEnchant(Enchantment var1);

    public int getEnchantLevel(Enchantment var1);

    public Map<Enchantment, Integer> getEnchants();

    public boolean addEnchant(Enchantment var1, int var2, boolean var3);

    public boolean removeEnchant(Enchantment var1);

    public boolean hasConflictingEnchant(Enchantment var1);

    public /* varargs */ void addItemFlags(ItemFlag ... var1);

    public /* varargs */ void removeItemFlags(ItemFlag ... var1);

    public Set<ItemFlag> getItemFlags();

    public boolean hasItemFlag(ItemFlag var1);

    public boolean isUnbreakable();

    public void setUnbreakable(boolean var1);

    public ItemMeta clone();

    public Spigot spigot();

    public static class Spigot {
        @Deprecated
        public void setUnbreakable(boolean unbreakable) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Deprecated
        public boolean isUnbreakable() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

}

