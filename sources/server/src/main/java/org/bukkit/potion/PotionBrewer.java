/*
 * Akarin Forge
 */
package org.bukkit.potion;

import java.util.Collection;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public interface PotionBrewer {
    public PotionEffect createEffect(PotionEffectType var1, int var2, int var3);

    @Deprecated
    public Collection<PotionEffect> getEffectsFromDamage(int var1);

    public Collection<PotionEffect> getEffects(PotionType var1, boolean var2, boolean var3);
}

