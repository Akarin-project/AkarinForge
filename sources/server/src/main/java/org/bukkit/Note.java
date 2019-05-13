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
import java.util.Map;
import org.apache.commons.lang.Validate;

public class Note {
    private final byte note;

    public Note(int note) {
        Validate.isTrue((boolean)(note >= 0 && note <= 24), (String)"The note value has to be between 0 and 24.");
        this.note = (byte)note;
    }

    public Note(int octave, Tone tone, boolean sharped) {
        if (sharped && !tone.isSharpable()) {
            tone = Tone.values()[tone.ordinal() + 1];
            sharped = false;
        }
        if (octave < 0 || octave > 2 || octave == 2 && (tone != Tone.F || !sharped)) {
            throw new IllegalArgumentException("Tone and octave have to be between F#0 and F#2");
        }
        this.note = (byte)(octave * 12 + tone.getId(sharped));
    }

    public static Note flat(int octave, Tone tone) {
        Validate.isTrue((boolean)(octave != 2), (String)"Octave cannot be 2 for flats");
        tone = tone == Tone.G ? Tone.F : Tone.values()[tone.ordinal() - 1];
        return new Note(octave, tone, tone.isSharpable());
    }

    public static Note sharp(int octave, Tone tone) {
        return new Note(octave, tone, true);
    }

    public static Note natural(int octave, Tone tone) {
        Validate.isTrue((boolean)(octave != 2), (String)"Octave cannot be 2 for naturals");
        return new Note(octave, tone, false);
    }

    public Note sharped() {
        Validate.isTrue((boolean)(this.note < 24), (String)"This note cannot be sharped because it is the highest known note!");
        return new Note(this.note + 1);
    }

    public Note flattened() {
        Validate.isTrue((boolean)(this.note > 0), (String)"This note cannot be flattened because it is the lowest known note!");
        return new Note(this.note - 1);
    }

    @Deprecated
    public byte getId() {
        return this.note;
    }

    public int getOctave() {
        return this.note / 12;
    }

    private byte getToneByte() {
        return (byte)(this.note % 12);
    }

    public Tone getTone() {
        return Tone.getById(this.getToneByte());
    }

    public boolean isSharped() {
        byte note = this.getToneByte();
        return Tone.getById(note).isSharped(note);
    }

    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = 31 * result + this.note;
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        Note other = (Note)obj;
        if (this.note != other.note) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "Note{" + this.getTone().toString() + (this.isSharped() ? "#" : "") + "}";
    }

    public static enum Tone {
        G(1, true),
        A(3, true),
        B(5, false),
        C(6, true),
        D(8, true),
        E(10, false),
        F(11, true);
        
        private final boolean sharpable;
        private final byte id;
        private static final Map<Byte, Tone> BY_DATA;
        public static final byte TONES_COUNT = 12;

        private Tone(int id2, boolean sharpable) {
            this.id = (byte)(id2 % 12);
            this.sharpable = sharpable;
        }

        @Deprecated
        public byte getId() {
            return this.getId(false);
        }

        @Deprecated
        public byte getId(boolean sharped) {
            int id2 = sharped && this.sharpable ? this.id + 1 : this.id;
            return (byte)(id2 % 12);
        }

        public boolean isSharpable() {
            return this.sharpable;
        }

        @Deprecated
        public boolean isSharped(byte id2) {
            if (id2 == this.getId(false)) {
                return false;
            }
            if (id2 == this.getId(true)) {
                return true;
            }
            throw new IllegalArgumentException("The id isn't matching to the tone.");
        }

        @Deprecated
        public static Tone getById(byte id2) {
            return BY_DATA.get(Byte.valueOf(id2));
        }

        static {
            BY_DATA = Maps.newHashMap();
            for (Tone tone : Tone.values()) {
                int id2 = tone.id % 12;
                BY_DATA.put(Byte.valueOf((byte)id2), tone);
                if (!tone.isSharpable()) continue;
                id2 = (id2 + 1) % 12;
                BY_DATA.put(Byte.valueOf((byte)id2), tone);
            }
        }
    }

}

