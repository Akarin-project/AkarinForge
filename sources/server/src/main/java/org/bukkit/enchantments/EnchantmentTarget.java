/*
 * Akarin Forge
 */
package org.bukkit.enchantments;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum EnchantmentTarget {
    ALL{

        @Override
        public boolean includes(Material item) {
            return true;
        }
    }
    ,
    ARMOR{

        @Override
        public boolean includes(Material item) {
            return ARMOR_FEET.includes(item) || ARMOR_LEGS.includes(item) || ARMOR_HEAD.includes(item) || ARMOR_TORSO.includes(item);
        }
    }
    ,
    ARMOR_FEET{

        @Override
        public boolean includes(Material item) {
            return item.equals((Object)Material.LEATHER_BOOTS) || item.equals((Object)Material.CHAINMAIL_BOOTS) || item.equals((Object)Material.IRON_BOOTS) || item.equals((Object)Material.DIAMOND_BOOTS) || item.equals((Object)Material.GOLD_BOOTS);
        }
    }
    ,
    ARMOR_LEGS{

        @Override
        public boolean includes(Material item) {
            return item.equals((Object)Material.LEATHER_LEGGINGS) || item.equals((Object)Material.CHAINMAIL_LEGGINGS) || item.equals((Object)Material.IRON_LEGGINGS) || item.equals((Object)Material.DIAMOND_LEGGINGS) || item.equals((Object)Material.GOLD_LEGGINGS);
        }
    }
    ,
    ARMOR_TORSO{

        @Override
        public boolean includes(Material item) {
            return item.equals((Object)Material.LEATHER_CHESTPLATE) || item.equals((Object)Material.CHAINMAIL_CHESTPLATE) || item.equals((Object)Material.IRON_CHESTPLATE) || item.equals((Object)Material.DIAMOND_CHESTPLATE) || item.equals((Object)Material.GOLD_CHESTPLATE);
        }
    }
    ,
    ARMOR_HEAD{

        @Override
        public boolean includes(Material item) {
            return item.equals((Object)Material.LEATHER_HELMET) || item.equals((Object)Material.CHAINMAIL_HELMET) || item.equals((Object)Material.DIAMOND_HELMET) || item.equals((Object)Material.IRON_HELMET) || item.equals((Object)Material.GOLD_HELMET);
        }
    }
    ,
    WEAPON{

        @Override
        public boolean includes(Material item) {
            return item.equals((Object)Material.WOOD_SWORD) || item.equals((Object)Material.STONE_SWORD) || item.equals((Object)Material.IRON_SWORD) || item.equals((Object)Material.DIAMOND_SWORD) || item.equals((Object)Material.GOLD_SWORD);
        }
    }
    ,
    TOOL{

        @Override
        public boolean includes(Material item) {
            return item.equals((Object)Material.WOOD_SPADE) || item.equals((Object)Material.STONE_SPADE) || item.equals((Object)Material.IRON_SPADE) || item.equals((Object)Material.DIAMOND_SPADE) || item.equals((Object)Material.GOLD_SPADE) || item.equals((Object)Material.WOOD_PICKAXE) || item.equals((Object)Material.STONE_PICKAXE) || item.equals((Object)Material.IRON_PICKAXE) || item.equals((Object)Material.DIAMOND_PICKAXE) || item.equals((Object)Material.GOLD_PICKAXE) || item.equals((Object)Material.WOOD_HOE) || item.equals((Object)Material.STONE_HOE) || item.equals((Object)Material.IRON_HOE) || item.equals((Object)Material.DIAMOND_HOE) || item.equals((Object)Material.GOLD_HOE) || item.equals((Object)Material.WOOD_AXE) || item.equals((Object)Material.STONE_AXE) || item.equals((Object)Material.IRON_AXE) || item.equals((Object)Material.DIAMOND_AXE) || item.equals((Object)Material.GOLD_AXE) || item.equals((Object)Material.SHEARS) || item.equals((Object)Material.FLINT_AND_STEEL);
        }
    }
    ,
    BOW{

        @Override
        public boolean includes(Material item) {
            return item.equals((Object)Material.BOW);
        }
    }
    ,
    FISHING_ROD{

        @Override
        public boolean includes(Material item) {
            return item.equals((Object)Material.FISHING_ROD);
        }
    }
    ,
    BREAKABLE{

        @Override
        public boolean includes(Material item) {
            return item.getMaxDurability() > 0 && item.getMaxStackSize() == 1;
        }
    }
    ,
    WEARABLE{

        @Override
        public boolean includes(Material item) {
            return ARMOR.includes(item) || item.equals((Object)Material.ELYTRA) || item.equals((Object)Material.PUMPKIN) || item.equals((Object)Material.JACK_O_LANTERN) || item.equals((Object)Material.SKULL_ITEM);
        }
    };
    

    private EnchantmentTarget() {
    }

    public abstract boolean includes(Material var1);

    public boolean includes(ItemStack item) {
        return this.includes(item.getType());
    }

}

