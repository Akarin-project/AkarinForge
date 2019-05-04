/*
 * Akarin Forge
 */
package org.bukkit.entity;

import java.util.List;
import org.bukkit.Color;
import org.bukkit.entity.Arrow;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public interface TippedArrow
extends Arrow {
    public void setBasePotionData(PotionData var1);

    public PotionData getBasePotionData();

    public Color getColor();

    public void setColor(Color var1);

    public boolean hasCustomEffects();

    public List<PotionEffect> getCustomEffects();

    public boolean addCustomEffect(PotionEffect var1, boolean var2);

    public boolean removeCustomEffect(PotionEffectType var1);

    public boolean hasCustomEffect(PotionEffectType var1);

    public void clearCustomEffects();
}

