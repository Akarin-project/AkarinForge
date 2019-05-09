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

public enum GrassSpecies {
    DEAD(0),
    NORMAL(1),
    FERN_LIKE(2);
    
    private final byte data;
    private static final Map<Byte, GrassSpecies> BY_DATA;

    private GrassSpecies(int data) {
        this.data = (byte)data;
    }

    @Deprecated
    public byte getData() {
        return this.data;
    }

    @Deprecated
    public static GrassSpecies getByData(byte data) {
        return BY_DATA.get(Byte.valueOf(data));
    }

    static {
        BY_DATA = Maps.newHashMap();
        for (GrassSpecies grassSpecies : GrassSpecies.values()) {
            BY_DATA.put(Byte.valueOf(grassSpecies.getData()), grassSpecies);
        }
    }
}

