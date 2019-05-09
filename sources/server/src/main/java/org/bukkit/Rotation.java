/*
 * Akarin Forge
 */
package org.bukkit;

public enum Rotation {
    NONE,
    CLOCKWISE_45,
    CLOCKWISE,
    CLOCKWISE_135,
    FLIPPED,
    FLIPPED_45,
    COUNTER_CLOCKWISE,
    COUNTER_CLOCKWISE_45;
    
    private static final Rotation[] rotations;

    private Rotation() {
    }

    public Rotation rotateClockwise() {
        return rotations[this.ordinal() + 1 & 7];
    }

    public Rotation rotateCounterClockwise() {
        return rotations[this.ordinal() - 1 & 7];
    }

    static {
        rotations = Rotation.values();
    }
}

