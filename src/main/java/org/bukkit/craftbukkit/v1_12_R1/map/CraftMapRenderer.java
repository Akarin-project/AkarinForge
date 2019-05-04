/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.map;

import java.util.Map;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.map.CraftMapView;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapCursor;
import org.bukkit.map.MapCursorCollection;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class CraftMapRenderer
extends MapRenderer {
    private final bev worldMap;

    public CraftMapRenderer(CraftMapView mapView, bev worldMap) {
        super(false);
        this.worldMap = worldMap;
    }

    @Override
    public void render(MapView map, MapCanvas canvas, Player player) {
        for (int x2 = 0; x2 < 128; ++x2) {
            for (int y2 = 0; y2 < 128; ++y2) {
                canvas.setPixel(x2, y2, this.worldMap.h[y2 * 128 + x2]);
            }
        }
        MapCursorCollection cursors = canvas.getCursors();
        while (cursors.size() > 0) {
            cursors.removeCursor(cursors.getCursor(0));
        }
        for (String key : this.worldMap.j.keySet()) {
            Player other = Bukkit.getPlayerExact(key);
            if (other != null && !player.canSee(other)) continue;
            beu decoration = this.worldMap.j.get(key);
            cursors.addCursor(decoration.c(), decoration.d(), (byte)(decoration.e() & 15), decoration.a());
        }
    }
}

