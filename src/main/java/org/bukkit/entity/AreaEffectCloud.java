/*
 * Akarin Forge
 */
package org.bukkit.entity;

import java.util.List;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

public interface AreaEffectCloud
extends Entity {
    public int getDuration();

    public void setDuration(int var1);

    public int getWaitTime();

    public void setWaitTime(int var1);

    public int getReapplicationDelay();

    public void setReapplicationDelay(int var1);

    public int getDurationOnUse();

    public void setDurationOnUse(int var1);

    public float getRadius();

    public void setRadius(float var1);

    public float getRadiusOnUse();

    public void setRadiusOnUse(float var1);

    public float getRadiusPerTick();

    public void setRadiusPerTick(float var1);

    public Particle getParticle();

    public void setParticle(Particle var1);

    public void setBasePotionData(PotionData var1);

    public PotionData getBasePotionData();

    public boolean hasCustomEffects();

    public List<PotionEffect> getCustomEffects();

    public boolean addCustomEffect(PotionEffect var1, boolean var2);

    public boolean removeCustomEffect(PotionEffectType var1);

    public boolean hasCustomEffect(PotionEffectType var1);

    public void clearCustomEffects();

    public Color getColor();

    public void setColor(Color var1);

    public ProjectileSource getSource();

    public void setSource(ProjectileSource var1);
}

