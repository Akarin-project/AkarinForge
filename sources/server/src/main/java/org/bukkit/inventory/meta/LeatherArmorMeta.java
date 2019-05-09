/*
 * Akarin Forge
 */
package org.bukkit.inventory.meta;

import org.bukkit.Color;
import org.bukkit.inventory.meta.ItemMeta;

public interface LeatherArmorMeta
extends ItemMeta {
    public Color getColor();

    public void setColor(Color var1);

    @Override
    public LeatherArmorMeta clone();
}

