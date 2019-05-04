/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.ImmutableList$Builder
 *  com.google.common.collect.Maps
 */
package org.bukkit.craftbukkit.v1_12_R1.potion;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.bukkit.craftbukkit.v1_12_R1.potion.CraftPotionUtil;
import org.bukkit.potion.PotionBrewer;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class CraftPotionBrewer
implements PotionBrewer {
    private static final Map<PotionType, Collection<PotionEffect>> cache = Maps.newHashMap();

    @Override
    public Collection<PotionEffect> getEffects(PotionType damage, boolean upgraded, boolean extended) {
        if (cache.containsKey((Object)damage)) {
            return cache.get((Object)damage);
        }
        List<va> mcEffects = akg.a(CraftPotionUtil.fromBukkit(new PotionData(damage, extended, upgraded))).a();
        ImmutableList.Builder builder = new ImmutableList.Builder();
        for (va effect : mcEffects) {
            builder.add((Object)CraftPotionUtil.toBukkit(effect));
        }
        cache.put(damage, (Collection<PotionEffect>)builder.build());
        return cache.get((Object)damage);
    }

    @Override
    public Collection<PotionEffect> getEffectsFromDamage(int damage) {
        return new ArrayList<PotionEffect>();
    }

    @Override
    public PotionEffect createEffect(PotionEffectType potion, int duration, int amplifier) {
        return new PotionEffect(potion, potion.isInstant() ? 1 : (int)((double)duration * potion.getDurationModifier()), amplifier);
    }
}

