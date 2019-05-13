/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 */
package org.bukkit;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.bukkit.Color;

public enum DyeColor {
    WHITE(0, 15, Color.fromRGB(16383998), Color.fromRGB(15790320)),
    ORANGE(1, 14, Color.fromRGB(16351261), Color.fromRGB(15435844)),
    MAGENTA(2, 13, Color.fromRGB(13061821), Color.fromRGB(12801229)),
    LIGHT_BLUE(3, 12, Color.fromRGB(3847130), Color.fromRGB(6719955)),
    YELLOW(4, 11, Color.fromRGB(16701501), Color.fromRGB(14602026)),
    LIME(5, 10, Color.fromRGB(8439583), Color.fromRGB(4312372)),
    PINK(6, 9, Color.fromRGB(15961002), Color.fromRGB(14188952)),
    GRAY(7, 8, Color.fromRGB(4673362), Color.fromRGB(4408131)),
    SILVER(8, 7, Color.fromRGB(10329495), Color.fromRGB(11250603)),
    CYAN(9, 6, Color.fromRGB(1481884), Color.fromRGB(2651799)),
    PURPLE(10, 5, Color.fromRGB(8991416), Color.fromRGB(8073150)),
    BLUE(11, 4, Color.fromRGB(3949738), Color.fromRGB(2437522)),
    BROWN(12, 3, Color.fromRGB(8606770), Color.fromRGB(5320730)),
    GREEN(13, 2, Color.fromRGB(6192150), Color.fromRGB(3887386)),
    RED(14, 1, Color.fromRGB(11546150), Color.fromRGB(11743532)),
    BLACK(15, 0, Color.fromRGB(1908001), Color.fromRGB(1973019));
    
    private final byte woolData;
    private final byte dyeData;
    private final Color color;
    private final Color firework;
    private static final DyeColor[] BY_WOOL_DATA;
    private static final DyeColor[] BY_DYE_DATA;
    private static final Map<Color, DyeColor> BY_COLOR;
    private static final Map<Color, DyeColor> BY_FIREWORK;

    private DyeColor(int woolData, int dyeData, Color color, Color firework) {
        this.woolData = (byte)woolData;
        this.dyeData = (byte)dyeData;
        this.color = color;
        this.firework = firework;
    }

    @Deprecated
    public byte getWoolData() {
        return this.woolData;
    }

    @Deprecated
    public byte getDyeData() {
        return this.dyeData;
    }

    public Color getColor() {
        return this.color;
    }

    public Color getFireworkColor() {
        return this.firework;
    }

    @Deprecated
    public static DyeColor getByWoolData(byte data) {
        int i2 = 255 & data;
        if (i2 >= BY_WOOL_DATA.length) {
            return null;
        }
        return BY_WOOL_DATA[i2];
    }

    @Deprecated
    public static DyeColor getByDyeData(byte data) {
        int i2 = 255 & data;
        if (i2 >= BY_DYE_DATA.length) {
            return null;
        }
        return BY_DYE_DATA[i2];
    }

    public static DyeColor getByColor(Color color) {
        return BY_COLOR.get(color);
    }

    public static DyeColor getByFireworkColor(Color color) {
        return BY_FIREWORK.get(color);
    }

    static {
        BY_WOOL_DATA = DyeColor.values();
        BY_DYE_DATA = DyeColor.values();
        ImmutableMap.Builder byColor = ImmutableMap.builder();
        ImmutableMap.Builder byFirework = ImmutableMap.builder();
        DyeColor[] arrdyeColor = DyeColor.values();
        int n2 = arrdyeColor.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            DyeColor color = arrdyeColor[i2];
            DyeColor.BY_WOOL_DATA[color.woolData & 255] = color;
            DyeColor.BY_DYE_DATA[color.dyeData & 255] = color;
            byColor.put((Object)color.getColor(), (Object)color);
            byFirework.put((Object)color.getFireworkColor(), (Object)color);
        }
        BY_COLOR = byColor.build();
        BY_FIREWORK = byFirework.build();
    }
}

