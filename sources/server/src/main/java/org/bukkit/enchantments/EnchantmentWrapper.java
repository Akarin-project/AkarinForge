/*
 * Akarin Forge
 */
package org.bukkit.enchantments;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class EnchantmentWrapper
extends Enchantment {
    public EnchantmentWrapper(int id2) {
        super(id2);
    }

    public Enchantment getEnchantment() {
        return Enchantment.getById(this.getId());
    }

    @Override
    public int getMaxLevel() {
        return this.getEnchantment().getMaxLevel();
    }

    @Override
    public int getStartLevel() {
        return this.getEnchantment().getStartLevel();
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return this.getEnchantment().getItemTarget();
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return this.getEnchantment().canEnchantItem(item);
    }

    @Override
    public String getName() {
        return this.getEnchantment().getName();
    }

    @Override
    public boolean isTreasure() {
        return this.getEnchantment().isTreasure();
    }

    @Override
    public boolean isCursed() {
        return this.getEnchantment().isCursed();
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        return this.getEnchantment().conflictsWith(other);
    }
}

