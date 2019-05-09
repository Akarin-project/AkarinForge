/*
 * Akarin Forge
 */
package org.bukkit.util;

public final class NumberConversions {
    private NumberConversions() {
    }

    public static int floor(double num) {
        int floor = (int)num;
        return (double)floor == num ? floor : floor - (int)(Double.doubleToRawLongBits(num) >>> 63);
    }

    public static int ceil(double num) {
        int floor = (int)num;
        return (double)floor == num ? floor : floor + (int)((Double.doubleToRawLongBits(num) ^ -1) >>> 63);
    }

    public static int round(double num) {
        return NumberConversions.floor(num + 0.5);
    }

    public static double square(double num) {
        return num * num;
    }

    public static int toInt(Object object) {
        if (object instanceof Number) {
            return ((Number)object).intValue();
        }
        try {
            return Integer.parseInt(object.toString());
        }
        catch (NumberFormatException numberFormatException) {
        }
        catch (NullPointerException nullPointerException) {
            // empty catch block
        }
        return 0;
    }

    public static float toFloat(Object object) {
        if (object instanceof Number) {
            return ((Number)object).floatValue();
        }
        try {
            return Float.parseFloat(object.toString());
        }
        catch (NumberFormatException numberFormatException) {
        }
        catch (NullPointerException nullPointerException) {
            // empty catch block
        }
        return 0.0f;
    }

    public static double toDouble(Object object) {
        if (object instanceof Number) {
            return ((Number)object).doubleValue();
        }
        try {
            return Double.parseDouble(object.toString());
        }
        catch (NumberFormatException numberFormatException) {
        }
        catch (NullPointerException nullPointerException) {
            // empty catch block
        }
        return 0.0;
    }

    public static long toLong(Object object) {
        if (object instanceof Number) {
            return ((Number)object).longValue();
        }
        try {
            return Long.parseLong(object.toString());
        }
        catch (NumberFormatException numberFormatException) {
        }
        catch (NullPointerException nullPointerException) {
            // empty catch block
        }
        return 0;
    }

    public static short toShort(Object object) {
        if (object instanceof Number) {
            return ((Number)object).shortValue();
        }
        try {
            return Short.parseShort(object.toString());
        }
        catch (NumberFormatException numberFormatException) {
        }
        catch (NullPointerException nullPointerException) {
            // empty catch block
        }
        return 0;
    }

    public static byte toByte(Object object) {
        if (object instanceof Number) {
            return ((Number)object).byteValue();
        }
        try {
            return Byte.parseByte(object.toString());
        }
        catch (NumberFormatException numberFormatException) {
        }
        catch (NullPointerException nullPointerException) {
            // empty catch block
        }
        return 0;
    }

    public static boolean isFinite(double d2) {
        return Math.abs(d2) <= Double.MAX_VALUE;
    }

    public static boolean isFinite(float f2) {
        return Math.abs(f2) <= Float.MAX_VALUE;
    }

    public static void checkFinite(double d2, String message) {
        if (!NumberConversions.isFinite(d2)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void checkFinite(float d2, String message) {
        if (!NumberConversions.isFinite(d2)) {
            throw new IllegalArgumentException(message);
        }
    }
}

