/*
 * Akarin Forge
 */
package org.bukkit.map;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.map.MapCursor;

public final class MapCursorCollection {
    private List<MapCursor> cursors = new ArrayList<MapCursor>();

    public int size() {
        return this.cursors.size();
    }

    public MapCursor getCursor(int index) {
        return this.cursors.get(index);
    }

    public boolean removeCursor(MapCursor cursor) {
        return this.cursors.remove(cursor);
    }

    public MapCursor addCursor(MapCursor cursor) {
        this.cursors.add(cursor);
        return cursor;
    }

    public MapCursor addCursor(int x2, int y2, byte direction) {
        return this.addCursor(x2, y2, direction, 0, true);
    }

    @Deprecated
    public MapCursor addCursor(int x2, int y2, byte direction, byte type) {
        return this.addCursor(x2, y2, direction, type, true);
    }

    @Deprecated
    public MapCursor addCursor(int x2, int y2, byte direction, byte type, boolean visible) {
        return this.addCursor(new MapCursor((byte)x2, (byte)y2, direction, type, visible));
    }
}

