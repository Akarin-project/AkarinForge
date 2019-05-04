/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.potion;

import org.bukkit.Color;
import org.bukkit.potion.PotionEffectType;

public class CraftPotionEffectType
extends PotionEffectType {
    private final uz handle;

    public CraftPotionEffectType(uz handle) {
        super(uz.a(handle));
        this.handle = handle;
    }

    @Override
    public double getDurationModifier() {
        return this.handle.g;
    }

    public uz getHandle() {
        return this.handle;
    }

    @Override
    public String getName() {
        switch (this.getId()) {
            case 1: {
                return "SPEED";
            }
            case 2: {
                return "SLOW";
            }
            case 3: {
                return "FAST_DIGGING";
            }
            case 4: {
                return "SLOW_DIGGING";
            }
            case 5: {
                return "INCREASE_DAMAGE";
            }
            case 6: {
                return "HEAL";
            }
            case 7: {
                return "HARM";
            }
            case 8: {
                return "JUMP";
            }
            case 9: {
                return "CONFUSION";
            }
            case 10: {
                return "REGENERATION";
            }
            case 11: {
                return "DAMAGE_RESISTANCE";
            }
            case 12: {
                return "FIRE_RESISTANCE";
            }
            case 13: {
                return "WATER_BREATHING";
            }
            case 14: {
                return "INVISIBILITY";
            }
            case 15: {
                return "BLINDNESS";
            }
            case 16: {
                return "NIGHT_VISION";
            }
            case 17: {
                return "HUNGER";
            }
            case 18: {
                return "WEAKNESS";
            }
            case 19: {
                return "POISON";
            }
            case 20: {
                return "WITHER";
            }
            case 21: {
                return "HEALTH_BOOST";
            }
            case 22: {
                return "ABSORPTION";
            }
            case 23: {
                return "SATURATION";
            }
            case 24: {
                return "GLOWING";
            }
            case 25: {
                return "LEVITATION";
            }
            case 26: {
                return "LUCK";
            }
            case 27: {
                return "UNLUCK";
            }
        }
        return "UNKNOWN_EFFECT_TYPE_" + this.getId();
    }

    @Override
    public boolean isInstant() {
        return this.handle.b();
    }

    @Override
    public Color getColor() {
        return Color.fromRGB(this.handle.g());
    }
}

