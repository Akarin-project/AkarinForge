/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  org.apache.commons.lang.Validate
 */
package org.bukkit;

import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.Locale;
import org.apache.commons.lang.Validate;

public enum Art {
    KEBAB(0, 1, 1),
    AZTEC(1, 1, 1),
    ALBAN(2, 1, 1),
    AZTEC2(3, 1, 1),
    BOMB(4, 1, 1),
    PLANT(5, 1, 1),
    WASTELAND(6, 1, 1),
    POOL(7, 2, 1),
    COURBET(8, 2, 1),
    SEA(9, 2, 1),
    SUNSET(10, 2, 1),
    CREEBET(11, 2, 1),
    WANDERER(12, 1, 2),
    GRAHAM(13, 1, 2),
    MATCH(14, 2, 2),
    BUST(15, 2, 2),
    STAGE(16, 2, 2),
    VOID(17, 2, 2),
    SKULL_AND_ROSES(18, 2, 2),
    WITHER(19, 2, 2),
    FIGHTERS(20, 4, 2),
    POINTER(21, 4, 4),
    PIGSCENE(22, 4, 4),
    BURNINGSKULL(23, 4, 4),
    SKELETON(24, 4, 3),
    DONKEYKONG(25, 4, 3);
    
    private int id;
    private int width;
    private int height;
    private static final HashMap<String, Art> BY_NAME;
    private static final HashMap<Integer, Art> BY_ID;

    private Art(int id2, int width, int height) {
        this.id = id2;
        this.width = width;
        this.height = height;
    }

    public int getBlockWidth() {
        return this.width;
    }

    public int getBlockHeight() {
        return this.height;
    }

    @Deprecated
    public int getId() {
        return this.id;
    }

    @Deprecated
    public static Art getById(int id2) {
        return BY_ID.get(id2);
    }

    public static Art getByName(String name) {
        Validate.notNull((Object)name, (String)"Name cannot be null");
        return BY_NAME.get(name.toLowerCase(Locale.ENGLISH).replaceAll("_", ""));
    }

    static {
        BY_NAME = Maps.newHashMap();
        BY_ID = Maps.newHashMap();
        for (Art art2 : Art.values()) {
            BY_ID.put(art2.id, art2);
            BY_NAME.put(art2.toString().toLowerCase(Locale.ENGLISH).replaceAll("_", ""), art2);
        }
    }
}

