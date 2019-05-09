/*
 * Akarin Forge
 */
package org.bukkit.map;

public final class MapCursor {
    private byte x;
    private byte y;
    private byte direction;
    private byte type;
    private boolean visible;

    @Deprecated
    public MapCursor(byte x2, byte y2, byte direction, byte type, boolean visible) {
        this.x = x2;
        this.y = y2;
        this.setDirection(direction);
        this.setRawType(type);
        this.visible = visible;
    }

    public MapCursor(byte x2, byte y2, byte direction, Type type, boolean visible) {
        this.x = x2;
        this.y = y2;
        this.setDirection(direction);
        this.setType(type);
        this.visible = visible;
    }

    public byte getX() {
        return this.x;
    }

    public byte getY() {
        return this.y;
    }

    public byte getDirection() {
        return this.direction;
    }

    public Type getType() {
        return Type.byValue(this.type);
    }

    @Deprecated
    public byte getRawType() {
        return this.type;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setX(byte x2) {
        this.x = x2;
    }

    public void setY(byte y2) {
        this.y = y2;
    }

    public void setDirection(byte direction) {
        if (direction < 0 || direction > 15) {
            throw new IllegalArgumentException("Direction must be in the range 0-15");
        }
        this.direction = direction;
    }

    public void setType(Type type) {
        this.setRawType(type.value);
    }

    @Deprecated
    public void setRawType(byte type) {
        if (type < 0 || type > 15) {
            throw new IllegalArgumentException("Type must be in the range 0-15");
        }
        this.type = type;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public static enum Type {
        WHITE_POINTER(0),
        GREEN_POINTER(1),
        RED_POINTER(2),
        BLUE_POINTER(3),
        WHITE_CROSS(4),
        RED_MARKER(5),
        WHITE_CIRCLE(6),
        SMALL_WHITE_CIRCLE(7),
        MANSION(8),
        TEMPLE(9);
        
        private byte value;

        private Type(int value) {
            this.value = (byte)value;
        }

        @Deprecated
        public byte getValue() {
            return this.value;
        }

        @Deprecated
        public static Type byValue(byte value) {
            for (Type t2 : Type.values()) {
                if (t2.value != value) continue;
                return t2;
            }
            return null;
        }
    }

}

