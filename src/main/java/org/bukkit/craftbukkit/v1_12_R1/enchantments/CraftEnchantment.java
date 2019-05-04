/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.enchantments;

import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;

public class CraftEnchantment
extends Enchantment {
    private final alk target;

    public CraftEnchantment(alk target) {
        super(alk.b(target));
        this.target = target;
    }

    @Override
    public int getMaxLevel() {
        return this.target.b();
    }

    @Override
    public int getStartLevel() {
        return this.target.f();
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        switch (this.target.c) {
            case a: {
                return EnchantmentTarget.ALL;
            }
            case b: {
                return EnchantmentTarget.ARMOR;
            }
            case c: {
                return EnchantmentTarget.ARMOR_FEET;
            }
            case f: {
                return EnchantmentTarget.ARMOR_HEAD;
            }
            case d: {
                return EnchantmentTarget.ARMOR_LEGS;
            }
            case e: {
                return EnchantmentTarget.ARMOR_TORSO;
            }
            case h: {
                return EnchantmentTarget.TOOL;
            }
            case g: {
                return EnchantmentTarget.WEAPON;
            }
            case k: {
                return EnchantmentTarget.BOW;
            }
            case i: {
                return EnchantmentTarget.FISHING_ROD;
            }
            case j: {
                return EnchantmentTarget.BREAKABLE;
            }
            case l: {
                return EnchantmentTarget.WEARABLE;
            }
        }
        return null;
    }

    @Override
    public boolean isTreasure() {
        return this.target.c();
    }

    @Override
    public boolean isCursed() {
        return this.target.d();
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return this.target.a(CraftItemStack.asNMSCopy(item));
    }

    @Override
    public String getName() {
        switch (this.getId()) {
            case 0: {
                return "PROTECTION_ENVIRONMENTAL";
            }
            case 1: {
                return "PROTECTION_FIRE";
            }
            case 2: {
                return "PROTECTION_FALL";
            }
            case 3: {
                return "PROTECTION_EXPLOSIONS";
            }
            case 4: {
                return "PROTECTION_PROJECTILE";
            }
            case 5: {
                return "OXYGEN";
            }
            case 6: {
                return "WATER_WORKER";
            }
            case 7: {
                return "THORNS";
            }
            case 8: {
                return "DEPTH_STRIDER";
            }
            case 9: {
                return "FROST_WALKER";
            }
            case 10: {
                return "BINDING_CURSE";
            }
            case 16: {
                return "DAMAGE_ALL";
            }
            case 17: {
                return "DAMAGE_UNDEAD";
            }
            case 18: {
                return "DAMAGE_ARTHROPODS";
            }
            case 19: {
                return "KNOCKBACK";
            }
            case 20: {
                return "FIRE_ASPECT";
            }
            case 21: {
                return "LOOT_BONUS_MOBS";
            }
            case 22: {
                return "SWEEPING_EDGE";
            }
            case 32: {
                return "DIG_SPEED";
            }
            case 33: {
                return "SILK_TOUCH";
            }
            case 34: {
                return "DURABILITY";
            }
            case 35: {
                return "LOOT_BONUS_BLOCKS";
            }
            case 48: {
                return "ARROW_DAMAGE";
            }
            case 49: {
                return "ARROW_KNOCKBACK";
            }
            case 50: {
                return "ARROW_FIRE";
            }
            case 51: {
                return "ARROW_INFINITE";
            }
            case 61: {
                return "LUCK";
            }
            case 62: {
                return "LURE";
            }
            case 70: {
                return "MENDING";
            }
            case 71: {
                return "VANISHING_CURSE";
            }
        }
        return "UNKNOWN_ENCHANT_" + this.getId();
    }

    public static alk getRaw(Enchantment enchantment) {
        if (enchantment instanceof EnchantmentWrapper) {
            enchantment = ((EnchantmentWrapper)enchantment).getEnchantment();
        }
        if (enchantment instanceof CraftEnchantment) {
            return ((CraftEnchantment)enchantment).target;
        }
        return null;
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        if (other instanceof EnchantmentWrapper) {
            other = ((EnchantmentWrapper)other).getEnchantment();
        }
        if (!(other instanceof CraftEnchantment)) {
            return false;
        }
        CraftEnchantment ench = (CraftEnchantment)other;
        return !this.target.c(ench.target);
    }

    public alk getHandle() {
        return this.target;
    }

}

