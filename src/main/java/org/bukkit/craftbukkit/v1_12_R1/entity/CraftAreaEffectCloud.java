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
import org.apache.commons.lang3.Validate;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_12_R1.CraftParticle;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_12_R1.potion.CraftPotionUtil;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

public class CraftAreaEffectCloud
extends CraftEntity
implements AreaEffectCloud {
    public CraftAreaEffectCloud(CraftServer server, ve entity) {
        super(server, entity);
    }

    @Override
    public ve getHandle() {
        return (ve)super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftAreaEffectCloud";
    }

    @Override
    public EntityType getType() {
        return EntityType.AREA_EFFECT_CLOUD;
    }

    @Override
    public int getDuration() {
        return this.getHandle().r();
    }

    @Override
    public void setDuration(int duration) {
        this.getHandle().e(duration);
    }

    @Override
    public int getWaitTime() {
        return this.getHandle().av;
    }

    @Override
    public void setWaitTime(int waitTime) {
        this.getHandle().g(waitTime);
    }

    @Override
    public int getReapplicationDelay() {
        return this.getHandle().aw;
    }

    @Override
    public void setReapplicationDelay(int delay) {
        this.getHandle().aw = delay;
    }

    @Override
    public int getDurationOnUse() {
        return this.getHandle().ay;
    }

    @Override
    public void setDurationOnUse(int duration) {
        this.getHandle().ay = duration;
    }

    @Override
    public float getRadius() {
        return this.getHandle().j();
    }

    @Override
    public void setRadius(float radius) {
        this.getHandle().a(radius);
    }

    @Override
    public float getRadiusOnUse() {
        return this.getHandle().az;
    }

    @Override
    public void setRadiusOnUse(float radius) {
        this.getHandle().b(radius);
    }

    @Override
    public float getRadiusPerTick() {
        return this.getHandle().aA;
    }

    @Override
    public void setRadiusPerTick(float radius) {
        this.getHandle().c(radius);
    }

    @Override
    public Particle getParticle() {
        return CraftParticle.toBukkit(this.getHandle().l());
    }

    @Override
    public void setParticle(Particle particle) {
        this.getHandle().a(CraftParticle.toNMS(particle));
    }

    @Override
    public Color getColor() {
        return Color.fromRGB(this.getHandle().k());
    }

    @Override
    public void setColor(Color color) {
        this.getHandle().a(color.asRGB());
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
        this.getHandle().h.remove(existing);
        this.getHandle().refreshEffects();
        return true;
    }

    @Override
    public void setBasePotionData(PotionData data) {
        Validate.notNull((Object)data, (String)"PotionData cannot be null", (Object[])new Object[0]);
        this.getHandle().setType(CraftPotionUtil.fromBukkit(data));
    }

    @Override
    public PotionData getBasePotionData() {
        return CraftPotionUtil.toBukkit(this.getHandle().getType());
    }

    @Override
    public ProjectileSource getSource() {
        vp source = this.getHandle().y();
        return source == null ? null : (LivingEntity)((Object)source.getBukkitEntity());
    }

    @Override
    public void setSource(ProjectileSource shooter) {
        if (shooter instanceof CraftLivingEntity) {
            this.getHandle().a((vq)((CraftLivingEntity)shooter).getHandle());
        } else {
            this.getHandle().a((vp)null);
        }
    }
}

