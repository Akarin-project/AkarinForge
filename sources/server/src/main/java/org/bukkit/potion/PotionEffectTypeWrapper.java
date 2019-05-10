/*
 * Akarin Forge
 */
package org.bukkit.potion;

import org.bukkit.Color;
import org.bukkit.potion.PotionEffectType;

public class PotionEffectTypeWrapper
extends PotionEffectType {
    protected PotionEffectTypeWrapper(int id2) {
        super(id2);
    }

    @Override
    public double getDurationModifier() {
        return this.getType().getDurationModifier();
    }

    @Override
    public String getName() {
        return this.getType().getName();
    }

    public PotionEffectType getType() {
        return PotionEffectType.getById(this.getId());
    }

    @Override
    public boolean isInstant() {
        return this.getType().isInstant();
    }

    @Override
    public Color getColor() {
        return this.getType().getColor();
    }
}

