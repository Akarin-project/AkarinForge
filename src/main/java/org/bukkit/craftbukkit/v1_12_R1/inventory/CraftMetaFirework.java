/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 *  org.apache.commons.lang3.Validate
 */
package org.bukkit.craftbukkit.v1_12_R1.inventory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.Validate;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaItem;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;

@DelegateDeserialization(value=CraftMetaItem.SerializableMeta.class)
class CraftMetaFirework
extends CraftMetaItem
implements FireworkMeta {
    static final CraftMetaItem.ItemMetaKey FIREWORKS = new CraftMetaItem.ItemMetaKey("Fireworks");
    static final CraftMetaItem.ItemMetaKey FLIGHT = new CraftMetaItem.ItemMetaKey("Flight", "power");
    static final CraftMetaItem.ItemMetaKey EXPLOSIONS = new CraftMetaItem.ItemMetaKey("Explosions", "firework-effects");
    static final CraftMetaItem.ItemMetaKey EXPLOSION_COLORS = new CraftMetaItem.ItemMetaKey("Colors");
    static final CraftMetaItem.ItemMetaKey EXPLOSION_TYPE = new CraftMetaItem.ItemMetaKey("Type");
    static final CraftMetaItem.ItemMetaKey EXPLOSION_TRAIL = new CraftMetaItem.ItemMetaKey("Trail");
    static final CraftMetaItem.ItemMetaKey EXPLOSION_FLICKER = new CraftMetaItem.ItemMetaKey("Flicker");
    static final CraftMetaItem.ItemMetaKey EXPLOSION_FADE = new CraftMetaItem.ItemMetaKey("FadeColors");
    private List<FireworkEffect> effects;
    private int power;

    CraftMetaFirework(CraftMetaItem meta) {
        super(meta);
        if (!(meta instanceof CraftMetaFirework)) {
            return;
        }
        CraftMetaFirework that = (CraftMetaFirework)meta;
        this.power = that.power;
        if (that.hasEffects()) {
            this.effects = new ArrayList<FireworkEffect>(that.effects);
        }
    }

    CraftMetaFirework(fy tag) {
        super(tag);
        if (!tag.e(CraftMetaFirework.FIREWORKS.NBT)) {
            return;
        }
        fy fireworks = tag.p(CraftMetaFirework.FIREWORKS.NBT);
        this.power = 255 & fireworks.f(CraftMetaFirework.FLIGHT.NBT);
        if (!fireworks.e(CraftMetaFirework.EXPLOSIONS.NBT)) {
            return;
        }
        ge fireworkEffects = fireworks.c(CraftMetaFirework.EXPLOSIONS.NBT, 10);
        this.effects = new ArrayList<FireworkEffect>(fireworkEffects.c());
        ArrayList<FireworkEffect> effects = this.effects;
        for (int i2 = 0; i2 < fireworkEffects.c(); ++i2) {
            effects.add(CraftMetaFirework.getEffect((fy)fireworkEffects.i(i2)));
        }
    }

    static FireworkEffect getEffect(fy explosion) {
        FireworkEffect.Builder effect = FireworkEffect.builder().flicker(explosion.q(CraftMetaFirework.EXPLOSION_FLICKER.NBT)).trail(explosion.q(CraftMetaFirework.EXPLOSION_TRAIL.NBT)).with(CraftMetaFirework.getEffectType(255 & explosion.f(CraftMetaFirework.EXPLOSION_TYPE.NBT)));
        int[] colors = explosion.n(CraftMetaFirework.EXPLOSION_COLORS.NBT);
        if (colors.length == 0) {
            effect.withColor(Color.WHITE);
        }
        for (int color : colors) {
            effect.withColor(Color.fromRGB(color));
        }
        for (int color : explosion.n(CraftMetaFirework.EXPLOSION_FADE.NBT)) {
            effect.withFade(Color.fromRGB(color));
        }
        return effect.build();
    }

    static fy getExplosion(FireworkEffect effect) {
        fy explosion = new fy();
        if (effect.hasFlicker()) {
            explosion.a(CraftMetaFirework.EXPLOSION_FLICKER.NBT, true);
        }
        if (effect.hasTrail()) {
            explosion.a(CraftMetaFirework.EXPLOSION_TRAIL.NBT, true);
        }
        CraftMetaFirework.addColors(explosion, EXPLOSION_COLORS, effect.getColors());
        CraftMetaFirework.addColors(explosion, EXPLOSION_FADE, effect.getFadeColors());
        explosion.a(CraftMetaFirework.EXPLOSION_TYPE.NBT, (byte)CraftMetaFirework.getNBT(effect.getType()));
        return explosion;
    }

    static int getNBT(FireworkEffect.Type type) {
        switch (type) {
            case BALL: {
                return 0;
            }
            case BALL_LARGE: {
                return 1;
            }
            case STAR: {
                return 2;
            }
            case CREEPER: {
                return 3;
            }
            case BURST: {
                return 4;
            }
        }
        throw new IllegalArgumentException("Unknown effect type " + (Object)((Object)type));
    }

    static FireworkEffect.Type getEffectType(int nbt) {
        switch (nbt) {
            case 0: {
                return FireworkEffect.Type.BALL;
            }
            case 1: {
                return FireworkEffect.Type.BALL_LARGE;
            }
            case 2: {
                return FireworkEffect.Type.STAR;
            }
            case 3: {
                return FireworkEffect.Type.CREEPER;
            }
            case 4: {
                return FireworkEffect.Type.BURST;
            }
        }
        throw new IllegalArgumentException("Unknown effect type " + nbt);
    }

    CraftMetaFirework(Map<String, Object> map) {
        super(map);
        Integer power = CraftMetaItem.SerializableMeta.getObject(Integer.class, map, CraftMetaFirework.FLIGHT.BUKKIT, true);
        if (power != null) {
            this.setPower(power);
        }
        Iterable effects = CraftMetaItem.SerializableMeta.getObject(Iterable.class, map, CraftMetaFirework.EXPLOSIONS.BUKKIT, true);
        this.safelyAddEffects(effects);
    }

    @Override
    public boolean hasEffects() {
        return this.effects != null && !this.effects.isEmpty();
    }

    void safelyAddEffects(Iterable<?> collection) {
        if (collection == null || collection instanceof Collection && ((Collection)collection).isEmpty()) {
            return;
        }
        List<FireworkEffect> effects = this.effects;
        if (effects == null) {
            effects = this.effects = new ArrayList<FireworkEffect>();
        }
        for (Object obj : collection) {
            if (obj instanceof FireworkEffect) {
                effects.add((FireworkEffect)obj);
                continue;
            }
            throw new IllegalArgumentException(obj + " in " + collection + " is not a FireworkEffect");
        }
    }

    @Override
    void applyToItem(fy itemTag) {
        super.applyToItem(itemTag);
        if (this.isFireworkEmpty()) {
            return;
        }
        fy fireworks = itemTag.p(CraftMetaFirework.FIREWORKS.NBT);
        itemTag.a(CraftMetaFirework.FIREWORKS.NBT, fireworks);
        if (this.hasEffects()) {
            ge effects = new ge();
            for (FireworkEffect effect : this.effects) {
                effects.a(CraftMetaFirework.getExplosion(effect));
            }
            if (effects.c() > 0) {
                fireworks.a(CraftMetaFirework.EXPLOSIONS.NBT, effects);
            }
        }
        if (this.hasPower()) {
            fireworks.a(CraftMetaFirework.FLIGHT.NBT, (byte)this.power);
        }
    }

    static void addColors(fy compound, CraftMetaItem.ItemMetaKey key, List<Color> colors) {
        if (colors.isEmpty()) {
            return;
        }
        int[] colorArray = new int[colors.size()];
        int i2 = 0;
        for (Color color : colors) {
            colorArray[i2++] = color.asRGB();
        }
        compound.a(key.NBT, colorArray);
    }

    @Override
    boolean applicableTo(Material type) {
        switch (type) {
            case FIREWORK: {
                return true;
            }
        }
        return false;
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isFireworkEmpty();
    }

    boolean isFireworkEmpty() {
        return !this.hasEffects() && !this.hasPower();
    }

    boolean hasPower() {
        return this.power != 0;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaFirework) {
            CraftMetaFirework that = (CraftMetaFirework)meta;
            return (this.hasPower() ? that.hasPower() && this.power == that.power : !that.hasPower()) && (this.hasEffects() ? that.hasEffects() && this.effects.equals(that.effects) : !that.hasEffects());
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaFirework || this.isFireworkEmpty());
    }

    @Override
    int applyHash() {
        int original;
        int hash = original = super.applyHash();
        if (this.hasPower()) {
            hash = 61 * hash + this.power;
        }
        if (this.hasEffects()) {
            hash = 61 * hash + 13 * this.effects.hashCode();
        }
        return hash != original ? CraftMetaFirework.class.hashCode() ^ hash : hash;
    }

    @Override
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);
        if (this.hasEffects()) {
            builder.put((Object)CraftMetaFirework.EXPLOSIONS.BUKKIT, (Object)ImmutableList.copyOf(this.effects));
        }
        if (this.hasPower()) {
            builder.put((Object)CraftMetaFirework.FLIGHT.BUKKIT, (Object)this.power);
        }
        return builder;
    }

    @Override
    public CraftMetaFirework clone() {
        CraftMetaFirework meta = (CraftMetaFirework)super.clone();
        if (this.effects != null) {
            meta.effects = new ArrayList<FireworkEffect>(this.effects);
        }
        return meta;
    }

    @Override
    public void addEffect(FireworkEffect effect) {
        Validate.notNull((Object)effect, (String)"Effect cannot be null", (Object[])new Object[0]);
        if (this.effects == null) {
            this.effects = new ArrayList<FireworkEffect>();
        }
        this.effects.add(effect);
    }

    @Override
    public /* varargs */ void addEffects(FireworkEffect ... effects) {
        Validate.notNull((Object)effects, (String)"Effects cannot be null", (Object[])new Object[0]);
        if (effects.length == 0) {
            return;
        }
        List<FireworkEffect> list = this.effects;
        if (list == null) {
            list = this.effects = new ArrayList<FireworkEffect>();
        }
        for (FireworkEffect effect : effects) {
            Validate.notNull((Object)effect, (String)"Effect cannot be null", (Object[])new Object[0]);
            list.add(effect);
        }
    }

    @Override
    public void addEffects(Iterable<FireworkEffect> effects) {
        Validate.notNull(effects, (String)"Effects cannot be null", (Object[])new Object[0]);
        this.safelyAddEffects(effects);
    }

    @Override
    public List<FireworkEffect> getEffects() {
        return this.effects == null ? ImmutableList.of() : ImmutableList.copyOf(this.effects);
    }

    @Override
    public int getEffectsSize() {
        return this.effects == null ? 0 : this.effects.size();
    }

    @Override
    public void removeEffect(int index) {
        if (this.effects == null) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: 0");
        }
        this.effects.remove(index);
    }

    @Override
    public void clearEffects() {
        this.effects = null;
    }

    @Override
    public int getPower() {
        return this.power;
    }

    @Override
    public void setPower(int power) {
        Validate.isTrue((boolean)(power >= 0), (String)"Power cannot be less than zero: ", (long)power);
        Validate.isTrue((boolean)(power < 128), (String)"Power cannot be more than 127: ", (long)power);
        this.power = power;
    }

}

