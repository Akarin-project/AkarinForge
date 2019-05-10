/*
 * Akarin Forge
 */
package org.bukkit.potion;

import org.bukkit.potion.PotionEffectType;

public enum PotionType {
    UNCRAFTABLE(null, false, false),
    WATER(null, false, false),
    MUNDANE(null, false, false),
    THICK(null, false, false),
    AWKWARD(null, false, false),
    NIGHT_VISION(PotionEffectType.NIGHT_VISION, false, true),
    INVISIBILITY(PotionEffectType.INVISIBILITY, false, true),
    JUMP(PotionEffectType.JUMP, true, true),
    FIRE_RESISTANCE(PotionEffectType.FIRE_RESISTANCE, false, true),
    SPEED(PotionEffectType.SPEED, true, true),
    SLOWNESS(PotionEffectType.SLOW, false, true),
    WATER_BREATHING(PotionEffectType.WATER_BREATHING, false, true),
    INSTANT_HEAL(PotionEffectType.HEAL, true, false),
    INSTANT_DAMAGE(PotionEffectType.HARM, true, false),
    POISON(PotionEffectType.POISON, true, true),
    REGEN(PotionEffectType.REGENERATION, true, true),
    STRENGTH(PotionEffectType.INCREASE_DAMAGE, true, true),
    WEAKNESS(PotionEffectType.WEAKNESS, false, true),
    LUCK(PotionEffectType.LUCK, false, false);
    
    private final PotionEffectType effect;
    private final boolean upgradeable;
    private final boolean extendable;

    private PotionType(PotionEffectType effect, boolean upgradeable, boolean extendable) {
        this.effect = effect;
        this.upgradeable = upgradeable;
        this.extendable = extendable;
    }

    public PotionEffectType getEffectType() {
        return this.effect;
    }

    public boolean isInstant() {
        return this.effect != null && this.effect.isInstant();
    }

    public boolean isUpgradeable() {
        return this.upgradeable;
    }

    public boolean isExtendable() {
        return this.extendable;
    }

    @Deprecated
    public int getDamageValue() {
        return this.ordinal();
    }

    public int getMaxLevel() {
        return this.upgradeable ? 2 : 1;
    }

    @Deprecated
    public static PotionType getByDamageValue(int damage) {
        return null;
    }

    @Deprecated
    public static PotionType getByEffect(PotionEffectType effectType) {
        if (effectType == null) {
            return WATER;
        }
        for (PotionType type : PotionType.values()) {
            if (!effectType.equals(type.effect)) continue;
            return type;
        }
        return null;
    }
}

