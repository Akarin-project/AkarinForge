/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.Validate
 */
package org.bukkit.enchantments;

import org.apache.commons.lang.Validate;
import org.bukkit.enchantments.Enchantment;

public class EnchantmentOffer {
    private Enchantment enchantment;
    private int enchantmentLevel;
    private int cost;

    public EnchantmentOffer(Enchantment enchantment, int enchantmentLevel, int cost) {
        this.enchantment = enchantment;
        this.enchantmentLevel = enchantmentLevel;
        this.cost = cost;
    }

    public Enchantment getEnchantment() {
        return this.enchantment;
    }

    public void setEnchantment(Enchantment enchantment) {
        Validate.notNull((Object)enchantment, (String)"The enchantment may not be null!");
        this.enchantment = enchantment;
    }

    public int getEnchantmentLevel() {
        return this.enchantmentLevel;
    }

    public void setEnchantmentLevel(int enchantmentLevel) {
        Validate.isTrue((boolean)(enchantmentLevel > 0), (String)"The enchantment level must be greater than 0!");
        this.enchantmentLevel = enchantmentLevel;
    }

    public int getCost() {
        return this.cost;
    }

    public void setCost(int cost) {
        Validate.isTrue((boolean)(cost > 0), (String)"The cost must be greater than 0!");
        this.cost = cost;
    }
}

