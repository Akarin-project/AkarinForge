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

public enum Instrument {
    PIANO(0),
    BASS_DRUM(1),
    SNARE_DRUM(2),
    STICKS(3),
    BASS_GUITAR(4),
    FLUTE(5),
    BELL(6),
    GUITAR(7),
    CHIME(8),
    XYLOPHONE(9);
    
    private final byte type;
    private static final Map<Byte, Instrument> BY_DATA;

    private Instrument(int type) {
        this.type = (byte)type;
    }

    @Deprecated
    public byte getType() {
        return this.type;
    }

    @Deprecated
    public static Instrument getByType(byte type) {
        return BY_DATA.get(Byte.valueOf(type));
    }

    static {
        BY_DATA = Maps.newHashMap();
        for (Instrument instrument : Instrument.values()) {
            BY_DATA.put(Byte.valueOf(instrument.getType()), instrument);
        }
    }
}

