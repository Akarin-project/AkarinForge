/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 */
package org.bukkit;

import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.Map;

public enum Difficulty {
    PEACEFUL(0),
    EASY(1),
    NORMAL(2),
    HARD(3);
    
    private final int value;
    private static final Map<Integer, Difficulty> BY_ID;

    private Difficulty(int value) {
        this.value = value;
    }

    @Deprecated
    public int getValue() {
        return this.value;
    }

    @Deprecated
    public static Difficulty getByValue(int value) {
        return BY_ID.get(value);
    }

    static {
        BY_ID = Maps.newHashMap();
        for (Difficulty diff : Difficulty.values()) {
            BY_ID.put(diff.value, diff);
        }
    }
}

