/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.entity.AbstractHorse;
import org.bukkit.inventory.AbstractHorseInventory;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.Inventory;

public interface Horse
extends AbstractHorse {
    public Color getColor();

    public void setColor(Color var1);

    public Style getStyle();

    public void setStyle(Style var1);

    @Deprecated
    public boolean isCarryingChest();

    @Deprecated
    public void setCarryingChest(boolean var1);

    @Override
    public HorseInventory getInventory();

    public static enum Style {
        NONE,
        WHITE,
        WHITEFIELD,
        WHITE_DOTS,
        BLACK_DOTS;
        

        private Style() {
        }
    }

    public static enum Color {
        WHITE,
        CREAMY,
        CHESTNUT,
        BROWN,
        BLACK,
        GRAY,
        DARK_BROWN;
        

        private Color() {
        }
    }

    @Deprecated
    public static enum Variant {
        HORSE,
        DONKEY,
        MULE,
        UNDEAD_HORSE,
        SKELETON_HORSE,
        LLAMA;
        

        private Variant() {
        }
    }

}

