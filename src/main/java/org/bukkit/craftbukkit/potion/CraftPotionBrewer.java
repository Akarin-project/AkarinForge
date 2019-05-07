package org.bukkit.craftbukkit.potion;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import sun.nio.ch.Net;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.bukkit.craftbukkit.potion.CraftPotionUtil;
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
        List<net.minecraft.potion.PotionEffect> mcEffects = net.minecraft.potion.PotionType.getPotionTypeForName(CraftPotionUtil.fromBukkit(new PotionData(damage, extended, upgraded))).getEffects();
        ImmutableList.Builder builder = new ImmutableList.Builder();
        for (net.minecraft.potion.PotionEffect effect : mcEffects) {
            builder.add(CraftPotionUtil.toBukkit(effect));
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

