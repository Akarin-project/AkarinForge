/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  org.apache.commons.lang.Validate
 */
package org.bukkit.potion;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.NoSuchElementException;
import org.apache.commons.lang.Validate;
import org.bukkit.Color;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffectType;

@SerializableAs(value="PotionEffect")
public class PotionEffect
implements ConfigurationSerializable {
    private static final String AMPLIFIER = "amplifier";
    private static final String DURATION = "duration";
    private static final String TYPE = "effect";
    private static final String AMBIENT = "ambient";
    private static final String PARTICLES = "has-particles";
    private final int amplifier;
    private final int duration;
    private final PotionEffectType type;
    private final boolean ambient;
    private final boolean particles;
    private final Color color;

    public PotionEffect(PotionEffectType type, int duration, int amplifier, boolean ambient, boolean particles, Color color) {
        Validate.notNull((Object)type, (String)"effect type cannot be null");
        this.type = type;
        this.duration = duration;
        this.amplifier = amplifier;
        this.ambient = ambient;
        this.particles = particles;
        this.color = color;
    }

    public PotionEffect(PotionEffectType type, int duration, int amplifier, boolean ambient, boolean particles) {
        this(type, duration, amplifier, ambient, particles, null);
    }

    public PotionEffect(PotionEffectType type, int duration, int amplifier, boolean ambient) {
        this(type, duration, amplifier, ambient, true);
    }

    public PotionEffect(PotionEffectType type, int duration, int amplifier) {
        this(type, duration, amplifier, true);
    }

    public PotionEffect(Map<String, Object> map) {
        this(PotionEffect.getEffectType(map), PotionEffect.getInt(map, "duration"), PotionEffect.getInt(map, "amplifier"), PotionEffect.getBool(map, "ambient", false), PotionEffect.getBool(map, "has-particles", true));
    }

    private static PotionEffectType getEffectType(Map<?, ?> map) {
        int type = PotionEffect.getInt(map, "effect");
        PotionEffectType effect = PotionEffectType.getById(type);
        if (effect != null) {
            return effect;
        }
        throw new NoSuchElementException(map + " does not contain " + "effect");
    }

    private static int getInt(Map<?, ?> map, Object key) {
        Object num = map.get(key);
        if (num instanceof Integer) {
            return (Integer)num;
        }
        throw new NoSuchElementException(map + " does not contain " + key);
    }

    private static boolean getBool(Map<?, ?> map, Object key, boolean def) {
        Object bool = map.get(key);
        if (bool instanceof Boolean) {
            return (Boolean)bool;
        }
        return def;
    }

    @Override
    public Map<String, Object> serialize() {
        return ImmutableMap.of("effect", this.type.getId(), "duration", this.duration, "amplifier", this.amplifier, "ambient", this.ambient, "has-particles", this.particles);
    }

    public boolean apply(LivingEntity entity) {
        return entity.addPotionEffect(this);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PotionEffect)) {
            return false;
        }
        PotionEffect that = (PotionEffect)obj;
        return this.type.equals(that.type) && this.ambient == that.ambient && this.amplifier == that.amplifier && this.duration == that.duration && this.particles == that.particles;
    }

    public int getAmplifier() {
        return this.amplifier;
    }

    public int getDuration() {
        return this.duration;
    }

    public PotionEffectType getType() {
        return this.type;
    }

    public boolean isAmbient() {
        return this.ambient;
    }

    public boolean hasParticles() {
        return this.particles;
    }

    public Color getColor() {
        return this.color;
    }

    public int hashCode() {
        int hash = 1;
        hash = hash * 31 + this.type.hashCode();
        hash = hash * 31 + this.amplifier;
        hash = hash * 31 + this.duration;
        hash ^= 572662306 >> (this.ambient ? 1 : -1);
        return hash ^= 572662306 >> (this.particles ? 1 : -1);
    }

    public String toString() {
        return this.type.getName() + (this.ambient ? ":(" : ":") + this.duration + "t-x" + this.amplifier + (this.ambient ? ")" : "");
    }
}

