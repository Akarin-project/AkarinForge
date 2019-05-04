/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.entity.ChestedHorse;
import org.bukkit.inventory.AbstractHorseInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.LlamaInventory;

public interface Llama
extends ChestedHorse {
    public Color getColor();

    public void setColor(Color var1);

    public int getStrength();

    public void setStrength(int var1);

    @Override
    public LlamaInventory getInventory();

    public static enum Color {
        CREAMY,
        WHITE,
        BROWN,
        GRAY;
        

        private Color() {
        }
    }

}

