/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.ImmutableList$Builder
 *  org.apache.commons.lang3.Validate
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.Validate;
import org.bukkit.Color;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftArrow;
import org.bukkit.craftbukkit.v1_12_R1.potion.CraftPotionUtil;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TippedArrow;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class CraftTippedArrow
extends CraftArrow
implements TippedArrow {
    public CraftTippedArrow(CraftServer server, afa entity) {
        super(server, entity);
    }

    @Override
    public afa getHandle() {
        return (afa)this.entity;
    }

    @Override
    public String toString() {
        return "CraftTippedArrow";
    }

    @Override
    public EntityType getType() {
        return EntityType.TIPPED_ARROW;
    }

    @Override
    public boolean addCustomEffect(PotionEffect effect, boolean override) {
        int effectId = effect.getType().getId();
        va existing = null;
        for (va mobEffect : this.getHandle().h) {
            if (uz.a(mobEffect.a()) != effectId) continue;
            existing = mobEffect;
        }
        if (existing != null) {
            if (!override) {
                return false;
            }
            this.getHandle().h.remove(existing);
        }
        this.getHandle().a(CraftPotionUtil.fromBukkit(effect));
        this.getHandle().refreshEffects();
        return true;
    }

    @Override
    public void clearCustomEffects() {
        Validate.isTrue((boolean)(this.getBasePotionData().getType() != PotionType.UNCRAFTABLE), (String)"Tipped Arrows must have at least 1 effect", (Object[])new Object[0]);
        this.getHandle().h.clear();
        this.getHandle().refreshEffects();
    }

    @Override
    public List<PotionEffect> getCustomEffects() {
        ImmutableList.Builder builder = ImmutableList.builder();
        for (va effect : this.getHandle().h) {
            builder.add((Object)CraftPotionUtil.toBukkit(effect));
        }
        return builder.build();
    }

    @Override
    public boolean hasCustomEffect(PotionEffectType type) {
        for (va effect : this.getHandle().h) {
            if (!CraftPotionUtil.equals(effect.a(), type)) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean hasCustomEffects() {
        return !this.getHandle().h.isEmpty();
    }

    @Override
    public boolean removeCustomEffect(PotionEffectType effect) {
        int effectId = effect.getId();
        va existing = null;
        for (va mobEffect : this.getHandle().h) {
            if (uz.a(mobEffect.a()) != effectId) continue;
            existing = mobEffect;
        }
        if (existing == null) {
            return false;
        }
        Validate.isTrue((boolean)(this.getBasePotionData().getType() != PotionType.UNCRAFTABLE || !this.getHandle().h.isEmpty()), (String)"Tipped Arrows must have at least 1 effect", (Object[])new Object[0]);
        this.getHandle().h.remove(existing);
        this.getHandle().refreshEffects();
        return true;
    }

    @Override
    public void setBasePotionData(PotionData data) {
        Validate.notNull((Object)data, (String)"PotionData cannot be null", (Object[])new Object[0]);
        Validate.isTrue((boolean)(data.getType() != PotionType.UNCRAFTABLE || !this.getHandle().h.isEmpty()), (String)"Tipped Arrows must have at least 1 effect", (Object[])new Object[0]);
        this.getHandle().setType(CraftPotionUtil.fromBukkit(data));
    }

    @Override
    public PotionData getBasePotionData() {
        return CraftPotionUtil.toBukkit(this.getHandle().getType());
    }

    @Override
    public void setColor(Color color) {
        this.getHandle().d(color.asRGB());
    }

    @Override
    public Color getColor() {
        return Color.fromRGB(this.getHandle().p());
    }
}

