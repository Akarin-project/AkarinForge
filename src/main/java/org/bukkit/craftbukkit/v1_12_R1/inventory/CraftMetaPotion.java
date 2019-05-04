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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.Validate;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaItem;
import org.bukkit.craftbukkit.v1_12_R1.potion.CraftPotionUtil;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

@DelegateDeserialization(value=CraftMetaItem.SerializableMeta.class)
class CraftMetaPotion
extends CraftMetaItem
implements PotionMeta {
    static final CraftMetaItem.ItemMetaKey AMPLIFIER = new CraftMetaItem.ItemMetaKey("Amplifier", "amplifier");
    static final CraftMetaItem.ItemMetaKey AMBIENT = new CraftMetaItem.ItemMetaKey("Ambient", "ambient");
    static final CraftMetaItem.ItemMetaKey DURATION = new CraftMetaItem.ItemMetaKey("Duration", "duration");
    static final CraftMetaItem.ItemMetaKey SHOW_PARTICLES = new CraftMetaItem.ItemMetaKey("ShowParticles", "has-particles");
    static final CraftMetaItem.ItemMetaKey POTION_EFFECTS = new CraftMetaItem.ItemMetaKey("CustomPotionEffects", "custom-effects");
    static final CraftMetaItem.ItemMetaKey POTION_COLOR = new CraftMetaItem.ItemMetaKey("CustomPotionColor", "custom-color");
    static final CraftMetaItem.ItemMetaKey ID = new CraftMetaItem.ItemMetaKey("Id", "potion-id");
    static final CraftMetaItem.ItemMetaKey DEFAULT_POTION = new CraftMetaItem.ItemMetaKey("Potion", "potion-type");
    private PotionData type = new PotionData(PotionType.UNCRAFTABLE, false, false);
    private List<PotionEffect> customEffects;
    private Color color;

    CraftMetaPotion(CraftMetaItem meta) {
        super(meta);
        if (!(meta instanceof CraftMetaPotion)) {
            return;
        }
        CraftMetaPotion potionMeta = (CraftMetaPotion)meta;
        this.type = potionMeta.type;
        this.color = potionMeta.color;
        if (potionMeta.hasCustomEffects()) {
            this.customEffects = new ArrayList<PotionEffect>(potionMeta.customEffects);
        }
    }

    CraftMetaPotion(fy tag) {
        super(tag);
        if (tag.e(CraftMetaPotion.DEFAULT_POTION.NBT)) {
            this.type = CraftPotionUtil.toBukkit(tag.l(CraftMetaPotion.DEFAULT_POTION.NBT));
        }
        if (tag.e(CraftMetaPotion.POTION_COLOR.NBT)) {
            this.color = Color.fromRGB(tag.h(CraftMetaPotion.POTION_COLOR.NBT));
        }
        if (tag.e(CraftMetaPotion.POTION_EFFECTS.NBT)) {
            ge list = tag.c(CraftMetaPotion.POTION_EFFECTS.NBT, 10);
            int length = list.c();
            this.customEffects = new ArrayList<PotionEffect>(length);
            for (int i2 = 0; i2 < length; ++i2) {
                fy effect = list.b(i2);
                PotionEffectType type = PotionEffectType.getById(effect.f(CraftMetaPotion.ID.NBT));
                if (type == null) continue;
                byte amp2 = effect.f(CraftMetaPotion.AMPLIFIER.NBT);
                int duration = effect.h(CraftMetaPotion.DURATION.NBT);
                boolean ambient = effect.q(CraftMetaPotion.AMBIENT.NBT);
                boolean particles = effect.q(CraftMetaPotion.SHOW_PARTICLES.NBT);
                this.customEffects.add(new PotionEffect(type, duration, amp2, ambient, particles));
            }
        }
    }

    CraftMetaPotion(Map<String, Object> map) {
        Iterable rawEffectList;
        super(map);
        this.type = CraftPotionUtil.toBukkit(CraftMetaItem.SerializableMeta.getString(map, CraftMetaPotion.DEFAULT_POTION.BUKKIT, true));
        Color color = CraftMetaItem.SerializableMeta.getObject(Color.class, map, CraftMetaPotion.POTION_COLOR.BUKKIT, true);
        if (color != null) {
            this.setColor(color);
        }
        if ((rawEffectList = CraftMetaItem.SerializableMeta.getObject(Iterable.class, map, CraftMetaPotion.POTION_EFFECTS.BUKKIT, true)) == null) {
            return;
        }
        for (Object obj : rawEffectList) {
            if (!(obj instanceof PotionEffect)) {
                throw new IllegalArgumentException("Object in effect list is not valid. " + obj.getClass());
            }
            this.addCustomEffect((PotionEffect)obj, true);
        }
    }

    @Override
    void applyToItem(fy tag) {
        super.applyToItem(tag);
        tag.a(CraftMetaPotion.DEFAULT_POTION.NBT, CraftPotionUtil.fromBukkit(this.type));
        if (this.hasColor()) {
            tag.a(CraftMetaPotion.POTION_COLOR.NBT, this.color.asRGB());
        }
        if (this.customEffects != null) {
            ge effectList = new ge();
            tag.a(CraftMetaPotion.POTION_EFFECTS.NBT, effectList);
            for (PotionEffect effect : this.customEffects) {
                fy effectData = new fy();
                effectData.a(CraftMetaPotion.ID.NBT, (byte)effect.getType().getId());
                effectData.a(CraftMetaPotion.AMPLIFIER.NBT, (byte)effect.getAmplifier());
                effectData.a(CraftMetaPotion.DURATION.NBT, effect.getDuration());
                effectData.a(CraftMetaPotion.AMBIENT.NBT, effect.isAmbient());
                effectData.a(CraftMetaPotion.SHOW_PARTICLES.NBT, effect.hasParticles());
                effectList.a(effectData);
            }
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isPotionEmpty();
    }

    boolean isPotionEmpty() {
        return this.type.getType() == PotionType.UNCRAFTABLE && !this.hasCustomEffects() && !this.hasColor();
    }

    @Override
    boolean applicableTo(Material type) {
        switch (type) {
            case POTION: 
            case SPLASH_POTION: 
            case LINGERING_POTION: 
            case TIPPED_ARROW: {
                return true;
            }
        }
        return false;
    }

    @Override
    public CraftMetaPotion clone() {
        CraftMetaPotion clone = (CraftMetaPotion)super.clone();
        clone.type = this.type;
        if (this.customEffects != null) {
            clone.customEffects = new ArrayList<PotionEffect>(this.customEffects);
        }
        return clone;
    }

    @Override
    public void setBasePotionData(PotionData data) {
        Validate.notNull((Object)data, (String)"PotionData cannot be null", (Object[])new Object[0]);
        this.type = data;
    }

    @Override
    public PotionData getBasePotionData() {
        return this.type;
    }

    @Override
    public boolean hasCustomEffects() {
        return this.customEffects != null;
    }

    @Override
    public List<PotionEffect> getCustomEffects() {
        if (this.hasCustomEffects()) {
            return ImmutableList.copyOf(this.customEffects);
        }
        return ImmutableList.of();
    }

    @Override
    public boolean addCustomEffect(PotionEffect effect, boolean overwrite) {
        Validate.notNull((Object)effect, (String)"Potion effect must not be null", (Object[])new Object[0]);
        int index = this.indexOfEffect(effect.getType());
        if (index != -1) {
            if (overwrite) {
                PotionEffect old = this.customEffects.get(index);
                if (old.getAmplifier() == effect.getAmplifier() && old.getDuration() == effect.getDuration() && old.isAmbient() == effect.isAmbient()) {
                    return false;
                }
                this.customEffects.set(index, effect);
                return true;
            }
            return false;
        }
        if (this.customEffects == null) {
            this.customEffects = new ArrayList<PotionEffect>();
        }
        this.customEffects.add(effect);
        return true;
    }

    @Override
    public boolean removeCustomEffect(PotionEffectType type) {
        Validate.notNull((Object)type, (String)"Potion effect type must not be null", (Object[])new Object[0]);
        if (!this.hasCustomEffects()) {
            return false;
        }
        boolean changed = false;
        Iterator<PotionEffect> iterator = this.customEffects.iterator();
        while (iterator.hasNext()) {
            PotionEffect effect = iterator.next();
            if (!type.equals(effect.getType())) continue;
            iterator.remove();
            changed = true;
        }
        if (this.customEffects.isEmpty()) {
            this.customEffects = null;
        }
        return changed;
    }

    @Override
    public boolean hasCustomEffect(PotionEffectType type) {
        Validate.notNull((Object)type, (String)"Potion effect type must not be null", (Object[])new Object[0]);
        return this.indexOfEffect(type) != -1;
    }

    @Override
    public boolean setMainEffect(PotionEffectType type) {
        Validate.notNull((Object)type, (String)"Potion effect type must not be null", (Object[])new Object[0]);
        int index = this.indexOfEffect(type);
        if (index == -1 || index == 0) {
            return false;
        }
        PotionEffect old = this.customEffects.get(0);
        this.customEffects.set(0, this.customEffects.get(index));
        this.customEffects.set(index, old);
        return true;
    }

    private int indexOfEffect(PotionEffectType type) {
        if (!this.hasCustomEffects()) {
            return -1;
        }
        for (int i2 = 0; i2 < this.customEffects.size(); ++i2) {
            if (!this.customEffects.get(i2).getType().equals(type)) continue;
            return i2;
        }
        return -1;
    }

    @Override
    public boolean clearCustomEffects() {
        boolean changed = this.hasCustomEffects();
        this.customEffects = null;
        return changed;
    }

    @Override
    public boolean hasColor() {
        return this.color != null;
    }

    @Override
    public Color getColor() {
        return this.color;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    int applyHash() {
        int original;
        int hash = original = super.applyHash();
        if (this.type.getType() != PotionType.UNCRAFTABLE) {
            hash = 73 * hash + this.type.hashCode();
        }
        if (this.hasColor()) {
            hash = 73 * hash + this.color.hashCode();
        }
        if (this.hasCustomEffects()) {
            hash = 73 * hash + this.customEffects.hashCode();
        }
        return original != hash ? CraftMetaPotion.class.hashCode() ^ hash : hash;
    }

    @Override
    public boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaPotion) {
            CraftMetaPotion that = (CraftMetaPotion)meta;
            return this.type.equals(that.type) && (this.hasCustomEffects() ? that.hasCustomEffects() && this.customEffects.equals(that.customEffects) : !that.hasCustomEffects()) && (this.hasColor() ? that.hasColor() && this.color.equals(that.color) : !that.hasColor());
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaPotion || this.isPotionEmpty());
    }

    @Override
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);
        if (this.type.getType() != PotionType.UNCRAFTABLE) {
            builder.put((Object)CraftMetaPotion.DEFAULT_POTION.BUKKIT, (Object)CraftPotionUtil.fromBukkit(this.type));
        }
        if (this.hasColor()) {
            builder.put((Object)CraftMetaPotion.POTION_COLOR.BUKKIT, (Object)this.getColor());
        }
        if (this.hasCustomEffects()) {
            builder.put((Object)CraftMetaPotion.POTION_EFFECTS.BUKKIT, (Object)ImmutableList.copyOf(this.customEffects));
        }
        return builder;
    }

}

