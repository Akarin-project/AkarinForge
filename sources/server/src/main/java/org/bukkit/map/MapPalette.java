/*
 * Akarin Forge
 */
package org.bukkit.map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public final class MapPalette {
    static final Color[] colors = new Color[]{MapPalette.c(0, 0, 0), MapPalette.c(0, 0, 0), MapPalette.c(0, 0, 0), MapPalette.c(0, 0, 0), MapPalette.c(89, 125, 39), MapPalette.c(109, 153, 48), MapPalette.c(127, 178, 56), MapPalette.c(67, 94, 29), MapPalette.c(174, 164, 115), MapPalette.c(213, 201, 140), MapPalette.c(247, 233, 163), MapPalette.c(130, 123, 86), MapPalette.c(140, 140, 140), MapPalette.c(171, 171, 171), MapPalette.c(199, 199, 199), MapPalette.c(105, 105, 105), MapPalette.c(180, 0, 0), MapPalette.c(220, 0, 0), MapPalette.c(255, 0, 0), MapPalette.c(135, 0, 0), MapPalette.c(112, 112, 180), MapPalette.c(138, 138, 220), MapPalette.c(160, 160, 255), MapPalette.c(84, 84, 135), MapPalette.c(117, 117, 117), MapPalette.c(144, 144, 144), MapPalette.c(167, 167, 167), MapPalette.c(88, 88, 88), MapPalette.c(0, 87, 0), MapPalette.c(0, 106, 0), MapPalette.c(0, 124, 0), MapPalette.c(0, 65, 0), MapPalette.c(180, 180, 180), MapPalette.c(220, 220, 220), MapPalette.c(255, 255, 255), MapPalette.c(135, 135, 135), MapPalette.c(115, 118, 129), MapPalette.c(141, 144, 158), MapPalette.c(164, 168, 184), MapPalette.c(86, 88, 97), MapPalette.c(106, 76, 54), MapPalette.c(130, 94, 66), MapPalette.c(151, 109, 77), MapPalette.c(79, 57, 40), MapPalette.c(79, 79, 79), MapPalette.c(96, 96, 96), MapPalette.c(112, 112, 112), MapPalette.c(59, 59, 59), MapPalette.c(45, 45, 180), MapPalette.c(55, 55, 220), MapPalette.c(64, 64, 255), MapPalette.c(33, 33, 135), MapPalette.c(100, 84, 50), MapPalette.c(123, 102, 62), MapPalette.c(143, 119, 72), MapPalette.c(75, 63, 38), MapPalette.c(180, 177, 172), MapPalette.c(220, 217, 211), MapPalette.c(255, 252, 245), MapPalette.c(135, 133, 129), MapPalette.c(152, 89, 36), MapPalette.c(186, 109, 44), MapPalette.c(216, 127, 51), MapPalette.c(114, 67, 27), MapPalette.c(125, 53, 152), MapPalette.c(153, 65, 186), MapPalette.c(178, 76, 216), MapPalette.c(94, 40, 114), MapPalette.c(72, 108, 152), MapPalette.c(88, 132, 186), MapPalette.c(102, 153, 216), MapPalette.c(54, 81, 114), MapPalette.c(161, 161, 36), MapPalette.c(197, 197, 44), MapPalette.c(229, 229, 51), MapPalette.c(121, 121, 27), MapPalette.c(89, 144, 17), MapPalette.c(109, 176, 21), MapPalette.c(127, 204, 25), MapPalette.c(67, 108, 13), MapPalette.c(170, 89, 116), MapPalette.c(208, 109, 142), MapPalette.c(242, 127, 165), MapPalette.c(128, 67, 87), MapPalette.c(53, 53, 53), MapPalette.c(65, 65, 65), MapPalette.c(76, 76, 76), MapPalette.c(40, 40, 40), MapPalette.c(108, 108, 108), MapPalette.c(132, 132, 132), MapPalette.c(153, 153, 153), MapPalette.c(81, 81, 81), MapPalette.c(53, 89, 108), MapPalette.c(65, 109, 132), MapPalette.c(76, 127, 153), MapPalette.c(40, 67, 81), MapPalette.c(89, 44, 125), MapPalette.c(109, 54, 153), MapPalette.c(127, 63, 178), MapPalette.c(67, 33, 94), MapPalette.c(36, 53, 125), MapPalette.c(44, 65, 153), MapPalette.c(51, 76, 178), MapPalette.c(27, 40, 94), MapPalette.c(72, 53, 36), MapPalette.c(88, 65, 44), MapPalette.c(102, 76, 51), MapPalette.c(54, 40, 27), MapPalette.c(72, 89, 36), MapPalette.c(88, 109, 44), MapPalette.c(102, 127, 51), MapPalette.c(54, 67, 27), MapPalette.c(108, 36, 36), MapPalette.c(132, 44, 44), MapPalette.c(153, 51, 51), MapPalette.c(81, 27, 27), MapPalette.c(17, 17, 17), MapPalette.c(21, 21, 21), MapPalette.c(25, 25, 25), MapPalette.c(13, 13, 13), MapPalette.c(176, 168, 54), MapPalette.c(215, 205, 66), MapPalette.c(250, 238, 77), MapPalette.c(132, 126, 40), MapPalette.c(64, 154, 150), MapPalette.c(79, 188, 183), MapPalette.c(92, 219, 213), MapPalette.c(48, 115, 112), MapPalette.c(52, 90, 180), MapPalette.c(63, 110, 220), MapPalette.c(74, 128, 255), MapPalette.c(39, 67, 135), MapPalette.c(0, 153, 40), MapPalette.c(0, 187, 50), MapPalette.c(0, 217, 58), MapPalette.c(0, 114, 30), MapPalette.c(91, 60, 34), MapPalette.c(111, 74, 42), MapPalette.c(129, 86, 49), MapPalette.c(68, 45, 25), MapPalette.c(79, 1, 0), MapPalette.c(96, 1, 0), MapPalette.c(112, 2, 0), MapPalette.c(59, 1, 0), MapPalette.c(147, 124, 113), MapPalette.c(180, 152, 138), MapPalette.c(209, 177, 161), MapPalette.c(110, 93, 85), MapPalette.c(112, 57, 25), MapPalette.c(137, 70, 31), MapPalette.c(159, 82, 36), MapPalette.c(84, 43, 19), MapPalette.c(105, 61, 76), MapPalette.c(128, 75, 93), MapPalette.c(149, 87, 108), MapPalette.c(78, 46, 57), MapPalette.c(79, 76, 97), MapPalette.c(96, 93, 119), MapPalette.c(112, 108, 138), MapPalette.c(59, 57, 73), MapPalette.c(131, 93, 25), MapPalette.c(160, 114, 31), MapPalette.c(186, 133, 36), MapPalette.c(98, 70, 19), MapPalette.c(72, 82, 37), MapPalette.c(88, 100, 45), MapPalette.c(103, 117, 53), MapPalette.c(54, 61, 28), MapPalette.c(112, 54, 55), MapPalette.c(138, 66, 67), MapPalette.c(160, 77, 78), MapPalette.c(84, 40, 41), MapPalette.c(40, 28, 24), MapPalette.c(49, 35, 30), MapPalette.c(57, 41, 35), MapPalette.c(30, 21, 18), MapPalette.c(95, 75, 69), MapPalette.c(116, 92, 84), MapPalette.c(135, 107, 98), MapPalette.c(71, 56, 51), MapPalette.c(61, 64, 64), MapPalette.c(75, 79, 79), MapPalette.c(87, 92, 92), MapPalette.c(46, 48, 48), MapPalette.c(86, 51, 62), MapPalette.c(105, 62, 75), MapPalette.c(122, 73, 88), MapPalette.c(64, 38, 46), MapPalette.c(53, 43, 64), MapPalette.c(65, 53, 79), MapPalette.c(76, 62, 92), MapPalette.c(40, 32, 48), MapPalette.c(53, 35, 24), MapPalette.c(65, 43, 30), MapPalette.c(76, 50, 35), MapPalette.c(40, 26, 18), MapPalette.c(53, 57, 29), MapPalette.c(65, 70, 36), MapPalette.c(76, 82, 42), MapPalette.c(40, 43, 22), MapPalette.c(100, 42, 32), MapPalette.c(122, 51, 39), MapPalette.c(142, 60, 46), MapPalette.c(75, 31, 24), MapPalette.c(26, 15, 11), MapPalette.c(31, 18, 13), MapPalette.c(37, 22, 16), MapPalette.c(19, 11, 8)};
    @Deprecated
    public static final byte TRANSPARENT = 0;
    @Deprecated
    public static final byte LIGHT_GREEN = 4;
    @Deprecated
    public static final byte LIGHT_BROWN = 8;
    @Deprecated
    public static final byte GRAY_1 = 12;
    @Deprecated
    public static final byte RED = 16;
    @Deprecated
    public static final byte PALE_BLUE = 20;
    @Deprecated
    public static final byte GRAY_2 = 24;
    @Deprecated
    public static final byte DARK_GREEN = 28;
    @Deprecated
    public static final byte WHITE = 32;
    @Deprecated
    public static final byte LIGHT_GRAY = 36;
    @Deprecated
    public static final byte BROWN = 40;
    @Deprecated
    public static final byte DARK_GRAY = 44;
    @Deprecated
    public static final byte BLUE = 48;
    @Deprecated
    public static final byte DARK_BROWN = 52;

    private MapPalette() {
    }

    private static Color c(int r2, int g2, int b2) {
        return new Color(r2, g2, b2);
    }

    private static double getDistance(Color c1, Color c2) {
        double rmean = (double)(c1.getRed() + c2.getRed()) / 2.0;
        double r2 = c1.getRed() - c2.getRed();
        double g2 = c1.getGreen() - c2.getGreen();
        int b2 = c1.getBlue() - c2.getBlue();
        double weightR = 2.0 + rmean / 256.0;
        double weightG = 4.0;
        double weightB = 2.0 + (255.0 - rmean) / 256.0;
        return weightR * r2 * r2 + weightG * g2 * g2 + weightB * (double)b2 * (double)b2;
    }

    public static BufferedImage resizeImage(Image image) {
        BufferedImage result = new BufferedImage(128, 128, 2);
        Graphics2D graphics = result.createGraphics();
        graphics.drawImage(image, 0, 0, 128, 128, null);
        graphics.dispose();
        return result;
    }

    @Deprecated
    public static byte[] imageToBytes(Image image) {
        BufferedImage temp = new BufferedImage(image.getWidth(null), image.getHeight(null), 2);
        Graphics2D graphics = temp.createGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();
        int[] pixels = new int[temp.getWidth() * temp.getHeight()];
        temp.getRGB(0, 0, temp.getWidth(), temp.getHeight(), pixels, 0, temp.getWidth());
        byte[] result = new byte[temp.getWidth() * temp.getHeight()];
        for (int i2 = 0; i2 < pixels.length; ++i2) {
            result[i2] = MapPalette.matchColor(new Color(pixels[i2], true));
        }
        return result;
    }

    @Deprecated
    public static byte matchColor(int r2, int g2, int b2) {
        return MapPalette.matchColor(new Color(r2, g2, b2));
    }

    @Deprecated
    public static byte matchColor(Color color) {
        if (color.getAlpha() < 128) {
            return 0;
        }
        int index = 0;
        double best = -1.0;
        for (int i2 = 4; i2 < colors.length; ++i2) {
            double distance = MapPalette.getDistance(color, colors[i2]);
            if (distance >= best && best != -1.0) continue;
            best = distance;
            index = i2;
        }
        return (byte)(index < 128 ? index : -129 + (index - 127));
    }

    @Deprecated
    public static Color getColor(byte index) {
        if (index > -49 && index < 0 || index > 127) {
            throw new IndexOutOfBoundsException();
        }
        return colors[index >= 0 ? index : index + 256];
    }
}

