/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  org.apache.commons.lang.Validate
 */
package org.bukkit;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.apache.commons.lang.Validate;
import org.bukkit.DyeColor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

@SerializableAs(value="Color")
public final class Color
implements ConfigurationSerializable {
    private static final int BIT_MASK = 255;
    public static final Color WHITE = Color.fromRGB(16777215);
    public static final Color SILVER = Color.fromRGB(12632256);
    public static final Color GRAY = Color.fromRGB(8421504);
    public static final Color BLACK = Color.fromRGB(0);
    public static final Color RED = Color.fromRGB(16711680);
    public static final Color MAROON = Color.fromRGB(8388608);
    public static final Color YELLOW = Color.fromRGB(16776960);
    public static final Color OLIVE = Color.fromRGB(8421376);
    public static final Color LIME = Color.fromRGB(65280);
    public static final Color GREEN = Color.fromRGB(32768);
    public static final Color AQUA = Color.fromRGB(65535);
    public static final Color TEAL = Color.fromRGB(32896);
    public static final Color BLUE = Color.fromRGB(255);
    public static final Color NAVY = Color.fromRGB(128);
    public static final Color FUCHSIA = Color.fromRGB(16711935);
    public static final Color PURPLE = Color.fromRGB(8388736);
    public static final Color ORANGE = Color.fromRGB(16753920);
    private final byte red;
    private final byte green;
    private final byte blue;

    public static Color fromRGB(int red, int green, int blue) throws IllegalArgumentException {
        return new Color(red, green, blue);
    }

    public static Color fromBGR(int blue, int green, int red) throws IllegalArgumentException {
        return new Color(red, green, blue);
    }

    public static Color fromRGB(int rgb) throws IllegalArgumentException {
        Validate.isTrue((boolean)(rgb >> 24 == 0), (String)"Extrenuous data in: ", (long)rgb);
        return Color.fromRGB(rgb >> 16 & 255, rgb >> 8 & 255, rgb >> 0 & 255);
    }

    public static Color fromBGR(int bgr) throws IllegalArgumentException {
        Validate.isTrue((boolean)(bgr >> 24 == 0), (String)"Extrenuous data in: ", (long)bgr);
        return Color.fromBGR(bgr >> 16 & 255, bgr >> 8 & 255, bgr >> 0 & 255);
    }

    private Color(int red, int green, int blue) {
        Validate.isTrue((boolean)(red >= 0 && red <= 255), (String)"Red is not between 0-255: ", (long)red);
        Validate.isTrue((boolean)(green >= 0 && green <= 255), (String)"Green is not between 0-255: ", (long)green);
        Validate.isTrue((boolean)(blue >= 0 && blue <= 255), (String)"Blue is not between 0-255: ", (long)blue);
        this.red = (byte)red;
        this.green = (byte)green;
        this.blue = (byte)blue;
    }

    public int getRed() {
        return 255 & this.red;
    }

    public Color setRed(int red) {
        return Color.fromRGB(red, this.getGreen(), this.getBlue());
    }

    public int getGreen() {
        return 255 & this.green;
    }

    public Color setGreen(int green) {
        return Color.fromRGB(this.getRed(), green, this.getBlue());
    }

    public int getBlue() {
        return 255 & this.blue;
    }

    public Color setBlue(int blue) {
        return Color.fromRGB(this.getRed(), this.getGreen(), blue);
    }

    public int asRGB() {
        return this.getRed() << 16 | this.getGreen() << 8 | this.getBlue() << 0;
    }

    public int asBGR() {
        return this.getBlue() << 16 | this.getGreen() << 8 | this.getRed() << 0;
    }

    public /* varargs */ Color mixDyes(DyeColor ... colors) {
        Validate.noNullElements((Object[])colors, (String)"Colors cannot be null");
        Color[] toPass = new Color[colors.length];
        for (int i2 = 0; i2 < colors.length; ++i2) {
            toPass[i2] = colors[i2].getColor();
        }
        return this.mixColors(toPass);
    }

    public /* varargs */ Color mixColors(Color ... colors) {
        Validate.noNullElements((Object[])colors, (String)"Colors cannot be null");
        int totalRed = this.getRed();
        int totalGreen = this.getGreen();
        int totalBlue = this.getBlue();
        int totalMax = Math.max(Math.max(totalRed, totalGreen), totalBlue);
        for (Color color : colors) {
            totalRed += color.getRed();
            totalGreen += color.getGreen();
            totalBlue += color.getBlue();
            totalMax += Math.max(Math.max(color.getRed(), color.getGreen()), color.getBlue());
        }
        float averageRed = totalRed / (colors.length + 1);
        float averageGreen = totalGreen / (colors.length + 1);
        float averageBlue = totalBlue / (colors.length + 1);
        float averageMax = totalMax / (colors.length + 1);
        float maximumOfAverages = Math.max(Math.max(averageRed, averageGreen), averageBlue);
        float gainFactor = averageMax / maximumOfAverages;
        return Color.fromRGB((int)(averageRed * gainFactor), (int)(averageGreen * gainFactor), (int)(averageBlue * gainFactor));
    }

    public boolean equals(Object o2) {
        if (!(o2 instanceof Color)) {
            return false;
        }
        Color that = (Color)o2;
        return this.blue == that.blue && this.green == that.green && this.red == that.red;
    }

    public int hashCode() {
        return this.asRGB() ^ Color.class.hashCode();
    }

    @Override
    public Map<String, Object> serialize() {
        return ImmutableMap.of("RED", this.getRed(), "BLUE", this.getBlue(), "GREEN", this.getGreen());
    }

    public static Color deserialize(Map<String, Object> map) {
        return Color.fromRGB(Color.asInt("RED", map), Color.asInt("GREEN", map), Color.asInt("BLUE", map));
    }

    private static int asInt(String string, Map<String, Object> map) {
        Object value = map.get(string);
        if (value == null) {
            throw new IllegalArgumentException(string + " not in map " + map);
        }
        if (!(value instanceof Number)) {
            throw new IllegalArgumentException(string + '(' + value + ") is not a number");
        }
        return ((Number)value).intValue();
    }

    public String toString() {
        return "Color:[rgb0x" + Integer.toHexString(this.getRed()).toUpperCase() + Integer.toHexString(this.getGreen()).toUpperCase() + Integer.toHexString(this.getBlue()).toUpperCase() + "]";
    }
}

