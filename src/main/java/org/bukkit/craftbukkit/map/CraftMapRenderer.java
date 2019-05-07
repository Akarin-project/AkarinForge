package org.bukkit.craftbukkit.map;

import java.util.UUID;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapCursorCollection;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class CraftMapRenderer extends MapRenderer {

    private final MapData worldMap;

    public CraftMapRenderer(CraftMapView mapView, MapData worldMap) {
        super(false);
        this.worldMap = worldMap;
    }

    @Override
    public void render(MapView map, MapCanvas canvas, Player player) {
        // Map
        for (int x = 0; x < 128; ++x) {
            for (int y = 0; y < 128; ++y) {
                canvas.setPixel(x, y, worldMap.colors[y * 128 + x]);
            }
        }

        // Cursors
        MapCursorCollection cursors = canvas.getCursors();
        while (cursors.size() > 0) {
            cursors.removeCursor(cursors.getCursor(0));
        }

        for (String key : worldMap.mapDecorations.keySet()) { // Spigot string -> uuid.
            // If this cursor is for a player check visibility with vanish system
            Player other = Bukkit.getPlayerExact(key); // Spigot
            if (other != null && !player.canSee(other)) {
                continue;
            }

            MapDecoration decoration = (MapDecoration) worldMap.mapDecorations.get(key);
            cursors.addCursor(decoration.getX(), decoration.getY(), (byte) (decoration.getRotation() & 15), decoration.getImage());
        }
    }

}
