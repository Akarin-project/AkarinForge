/*
 * Akarin Forge
 */
package org.bukkit.inventory.meta;

import java.util.List;
import org.bukkit.Color;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public interface PotionMeta
extends ItemMeta {
    public void setBasePotionData(PotionData var1);

    public PotionData getBasePotionData();

    public boolean hasCustomEffects();

    public List<PotionEffect> getCustomEffects();

    public boolean addCustomEffect(PotionEffect var1, boolean var2);

    public boolean removeCustomEffect(PotionEffectType var1);

    public boolean hasCustomEffect(PotionEffectType var1);

    @Deprecated
    public boolean setMainEffect(PotionEffectType var1);

    public boolean clearCustomEffects();

    public boolean hasColor();

    public Color getColor();

    public void setColor(Color var1);

    @Override
    public PotionMeta clone();
}

