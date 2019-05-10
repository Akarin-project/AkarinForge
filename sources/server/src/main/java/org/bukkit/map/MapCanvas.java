/*
 * Akarin Forge
 */
package org.bukkit.map;

import java.awt.Image;
import org.bukkit.map.MapCursorCollection;
import org.bukkit.map.MapFont;
import org.bukkit.map.MapView;

public interface MapCanvas {
    public MapView getMapView();

    public MapCursorCollection getCursors();

    public void setCursors(MapCursorCollection var1);

    public void setPixel(int var1, int var2, byte var3);

    public byte getPixel(int var1, int var2);

    public byte getBasePixel(int var1, int var2);

    public void drawImage(int var1, int var2, Image var3);

    public void drawText(int var1, int var2, MapFont var3, String var4);
}

