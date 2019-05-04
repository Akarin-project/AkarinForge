/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.Validate
 */
package org.bukkit.potion;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang.Validate;
import org.bukkit.Color;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectTypeWrapper;

public abstract class PotionEffectType {
    public static final PotionEffectType SPEED = new PotionEffectTypeWrapper(1);
    public static final PotionEffectType SLOW = new PotionEffectTypeWrapper(2);
    public static final PotionEffectType FAST_DIGGING = new PotionEffectTypeWrapper(3);
    public static final PotionEffectType SLOW_DIGGING = new PotionEffectTypeWrapper(4);
    public static final PotionEffectType INCREASE_DAMAGE = new PotionEffectTypeWrapper(5);
    public static final PotionEffectType HEAL = new PotionEffectTypeWrapper(6);
    public static final PotionEffectType HARM = new PotionEffectTypeWrapper(7);
    public static final PotionEffectType JUMP = new PotionEffectTypeWrapper(8);
    public static final PotionEffectType CONFUSION = new PotionEffectTypeWrapper(9);
    public static final PotionEffectType REGENERATION = new PotionEffectTypeWrapper(10);
    public static final PotionEffectType DAMAGE_RESISTANCE = new PotionEffectTypeWrapper(11);
    public static final PotionEffectType FIRE_RESISTANCE = new PotionEffectTypeWrapper(12);
    public static final PotionEffectType WATER_BREATHING = new PotionEffectTypeWrapper(13);
    public static final PotionEffectType INVISIBILITY = new PotionEffectTypeWrapper(14);
    public static final PotionEffectType BLINDNESS = new PotionEffectTypeWrapper(15);
    public static final PotionEffectType NIGHT_VISION = new PotionEffectTypeWrapper(16);
    public static final PotionEffectType HUNGER = new PotionEffectTypeWrapper(17);
    public static final PotionEffectType WEAKNESS = new PotionEffectTypeWrapper(18);
    public static final PotionEffectType POISON = new PotionEffectTypeWrapper(19);
    public static final PotionEffectType WITHER = new PotionEffectTypeWrapper(20);
    public static final PotionEffectType HEALTH_BOOST = new PotionEffectTypeWrapper(21);
    public static final PotionEffectType ABSORPTION = new PotionEffectTypeWrapper(22);
    public static final PotionEffectType SATURATION = new PotionEffectTypeWrapper(23);
    public static final PotionEffectType GLOWING = new PotionEffectTypeWrapper(24);
    public static final PotionEffectType LEVITATION = new PotionEffectTypeWrapper(25);
    public static final PotionEffectType LUCK = new PotionEffectTypeWrapper(26);
    public static final PotionEffectType UNLUCK = new PotionEffectTypeWrapper(27);
    private final int id;
    private static final PotionEffectType[] byId = new PotionEffectType[255];
    private static final Map<String, PotionEffectType> byName = new HashMap<String, PotionEffectType>();
    private static boolean acceptingNew = true;

    protected PotionEffectType(int id2) {
        this.id = id2;
    }

    public PotionEffect createEffect(int duration, int amplifier) {
        return new PotionEffect(this, this.isInstant() ? 1 : (int)((double)duration * this.getDurationModifier()), amplifier);
    }

    public abstract double getDurationModifier();

    @Deprecated
    public int getId() {
        return this.id;
    }

    public abstract String getName();

    public abstract boolean isInstant();

    public abstract Color getColor();

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof PotionEffectType)) {
            return false;
        }
        PotionEffectType other = (PotionEffectType)obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.id;
    }

    public String toString() {
        return "PotionEffectType[" + this.id + ", " + this.getName() + "]";
    }

    @Deprecated
    public static PotionEffectType getById(int id2) {
        if (id2 >= byId.length || id2 < 0) {
            return null;
        }
        return byId[id2];
    }

    public static PotionEffectType getByName(String name) {
        Validate.notNull((Object)name, (String)"name cannot be null");
        return byName.get(name.toLowerCase(Locale.ENGLISH));
    }

    public static void registerPotionEffectType(PotionEffectType type) {
        if (byId[type.id] != null || byName.containsKey(type.getName().toLowerCase(Locale.ENGLISH))) {
            throw new IllegalArgumentException("Cannot set already-set type");
        }
        if (!acceptingNew) {
            throw new IllegalStateException("No longer accepting new potion effect types (can only be done by the server implementation)");
        }
        PotionEffectType.byId[type.id] = type;
        byName.put(type.getName().toLowerCase(Locale.ENGLISH), type);
    }

    public static void stopAcceptingRegistrations() {
        acceptingNew = false;
    }

    public static PotionEffectType[] values() {
        return (PotionEffectType[])byId.clone();
    }
}

