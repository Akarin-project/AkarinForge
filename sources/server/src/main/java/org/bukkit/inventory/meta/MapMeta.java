/*
 * Akarin Forge
 */
package org.bukkit.inventory.meta;

import org.bukkit.Color;
import org.bukkit.inventory.meta.ItemMeta;

public interface MapMeta
extends ItemMeta {
    public boolean isScaling();

    public void setScaling(boolean var1);

    public boolean hasLocationName();

    public String getLocationName();

    public void setLocationName(String var1);

    public boolean hasColor();

    public Color getColor();

    public void setColor(Color var1);

    @Override
    public MapMeta clone();
}

