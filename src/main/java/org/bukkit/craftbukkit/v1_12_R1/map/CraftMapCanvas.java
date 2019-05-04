/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.map;

import java.awt.Image;
import java.awt.image.ImageObserver;
import java.util.Arrays;
import org.bukkit.craftbukkit.v1_12_R1.map.CraftMapView;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapCursorCollection;
import org.bukkit.map.MapFont;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapView;

public class CraftMapCanvas
implements MapCanvas {
    private final byte[] buffer = new byte[16384];
    private final CraftMapView mapView;
    private byte[] base;
    private MapCursorCollection cursors = new MapCursorCollection();

    protected CraftMapCanvas(CraftMapView mapView) {
        this.mapView = mapView;
        Arrays.fill(this.buffer, -1);
    }

    @Override
    public CraftMapView getMapView() {
        return this.mapView;
    }

    @Override
    public MapCursorCollection getCursors() {
        return this.cursors;
    }

    @Override
    public void setCursors(MapCursorCollection cursors) {
        this.cursors = cursors;
    }

    @Override
    public void setPixel(int x2, int y2, byte color) {
        if (x2 < 0 || y2 < 0 || x2 >= 128 || y2 >= 128) {
            return;
        }
        if (this.buffer[y2 * 128 + x2] != color) {
            this.buffer[y2 * 128 + x2] = color;
            this.mapView.worldMap.a(x2, y2);
        }
    }

    @Override
    public byte getPixel(int x2, int y2) {
        if (x2 < 0 || y2 < 0 || x2 >= 128 || y2 >= 128) {
            return 0;
        }
        return this.buffer[y2 * 128 + x2];
    }

    @Override
    public byte getBasePixel(int x2, int y2) {
        if (x2 < 0 || y2 < 0 || x2 >= 128 || y2 >= 128) {
            return 0;
        }
        return this.base[y2 * 128 + x2];
    }

    protected void setBase(byte[] base) {
        this.base = base;
    }

    protected byte[] getBuffer() {
        return this.buffer;
    }

    @Override
    public void drawImage(int x2, int y2, Image image) {
        byte[] bytes = MapPalette.imageToBytes(image);
        for (int x22 = 0; x22 < image.getWidth(null); ++x22) {
            for (int y22 = 0; y22 < image.getHeight(null); ++y22) {
                this.setPixel(x2 + x22, y2 + y22, bytes[y22 * image.getWidth(null) + x22]);
            }
        }
    }

    @Override
    public void drawText(int x2, int y2, MapFont font, String text) {
        int xStart = x2;
        byte color = 44;
        if (!font.isValid(text)) {
            throw new IllegalArgumentException("text contains invalid characters");
        }
        for (int i2 = 0; i2 < text.length(); ++i2) {
            char ch2 = text.charAt(i2);
            if (ch2 == '\n') {
                x2 = xStart;
                y2 += font.getHeight() + 1;
                continue;
            }
            if (ch2 == '\u00a7') {
                int j2 = text.indexOf(59, i2);
                if (j2 >= 0) {
                    try {
                        color = Byte.parseByte(text.substring(i2 + 1, j2));
                        i2 = j2;
                        continue;
                    }
                    catch (NumberFormatException numberFormatException) {
                        // empty catch block
                    }
                }
                throw new IllegalArgumentException("Text contains unterminated color string");
            }
            MapFont.CharacterSprite sprite = font.getChar(text.charAt(i2));
            for (int r2 = 0; r2 < font.getHeight(); ++r2) {
                for (int c2 = 0; c2 < sprite.getWidth(); ++c2) {
                    if (!sprite.get(r2, c2)) continue;
                    this.setPixel(x2 + c2, y2 + r2, color);
                }
            }
            x2 += sprite.getWidth() + 1;
        }
    }
}

