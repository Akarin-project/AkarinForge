/*
 * Akarin Forge
 */
package org.bukkit;

import java.util.List;
import org.bukkit.Achievement;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Statistic;
import org.bukkit.advancement.Advancement;
import org.bukkit.inventory.ItemStack;

@Deprecated
public interface UnsafeValues {
    public Material getMaterialFromInternalName(String var1);

    public List<String> tabCompleteInternalMaterialName(String var1, List<String> var2);

    public ItemStack modifyItemStack(ItemStack var1, String var2);

    public Statistic getStatisticFromInternalName(String var1);

    public Achievement getAchievementFromInternalName(String var1);

    public List<String> tabCompleteInternalStatisticOrAchievementName(String var1, List<String> var2);

    public Advancement loadAdvancement(NamespacedKey var1, String var2);

    public boolean removeAdvancement(NamespacedKey var1);
}

