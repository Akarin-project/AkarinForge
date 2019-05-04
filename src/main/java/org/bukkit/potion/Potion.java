/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.Validate
 */
package org.bukkit.potion;

import java.util.Collection;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionBrewer;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

@Deprecated
public class Potion {
    private boolean extended = false;
    private boolean splash = false;
    private int level = 1;
    private PotionType type;
    private static PotionBrewer brewer;
    private static final int EXTENDED_BIT = 64;
    private static final int POTION_BIT = 15;
    private static final int SPLASH_BIT = 16384;
    private static final int TIER_BIT = 32;
    private static final int TIER_SHIFT = 5;

    public Potion(PotionType type) {
        Validate.notNull((Object)((Object)type), (String)"Null PotionType");
        this.type = type;
    }

    public Potion(PotionType type, int level) {
        this(type);
        Validate.notNull((Object)((Object)type), (String)"Type cannot be null");
        Validate.isTrue((boolean)(level > 0 && level < 3), (String)"Level must be 1 or 2");
        this.level = level;
    }

    @Deprecated
    public Potion(PotionType type, int level, boolean splash) {
        this(type, level);
        this.splash = splash;
    }

    @Deprecated
    public Potion(PotionType type, int level, boolean splash, boolean extended) {
        this(type, level, splash);
        this.extended = extended;
    }

    @Deprecated
    public Potion(int name) {
        this(PotionType.WATER);
    }

    public Potion splash() {
        this.setSplash(true);
        return this;
    }

    public Potion extend() {
        this.setHasExtendedDuration(true);
        return this;
    }

    public void apply(ItemStack to2) {
        Validate.notNull((Object)to2, (String)"itemstack cannot be null");
        Validate.isTrue((boolean)to2.hasItemMeta(), (String)"given itemstack is not a potion");
        Validate.isTrue((boolean)(to2.getItemMeta() instanceof PotionMeta), (String)"given itemstack is not a potion");
        PotionMeta meta = (PotionMeta)to2.getItemMeta();
        meta.setBasePotionData(new PotionData(this.type, this.extended, this.level == 2));
        to2.setItemMeta(meta);
    }

    public void apply(LivingEntity to2) {
        Validate.notNull((Object)to2, (String)"entity cannot be null");
        to2.addPotionEffects(this.getEffects());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Potion other = (Potion)obj;
        return this.extended == other.extended && this.splash == other.splash && this.level == other.level && this.type == other.type;
    }

    public Collection<PotionEffect> getEffects() {
        return Potion.getBrewer().getEffects(this.type, this.level == 2, this.extended);
    }

    public int getLevel() {
        return this.level;
    }

    public PotionType getType() {
        return this.type;
    }

    public boolean hasExtendedDuration() {
        return this.extended;
    }

    public int hashCode() {
        int prime = 31;
        int result = 31 + this.level;
        result = 31 * result + (this.extended ? 1231 : 1237);
        result = 31 * result + (this.splash ? 1231 : 1237);
        result = 31 * result + (this.type == null ? 0 : this.type.hashCode());
        return result;
    }

    public boolean isSplash() {
        return this.splash;
    }

    public void setHasExtendedDuration(boolean isExtended) {
        Validate.isTrue((boolean)(this.type == null || !this.type.isInstant()), (String)"Instant potions cannot be extended");
        this.extended = isExtended;
    }

    public void setSplash(boolean isSplash) {
        this.splash = isSplash;
    }

    public void setType(PotionType type) {
        this.type = type;
    }

    public void setLevel(int level) {
        Validate.notNull((Object)((Object)this.type), (String)"No-effect potions don't have a level.");
        Validate.isTrue((boolean)(level > 0 && level <= 2), (String)"Level must be between 1 and 2 for this potion");
        this.level = level;
    }

    @Deprecated
    public short toDamageValue() {
        return 0;
    }

    public ItemStack toItemStack(int amount) {
        Material material = this.isSplash() ? Material.SPLASH_POTION : Material.POTION;
        ItemStack itemStack = new ItemStack(material, amount);
        PotionMeta meta = (PotionMeta)itemStack.getItemMeta();
        meta.setBasePotionData(new PotionData(this.type, this.level == 2, this.extended));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static Potion fromDamage(int damage) {
        PotionType type;
        Potion potion;
        switch (damage & 15) {
            case 0: {
                type = PotionType.WATER;
                break;
            }
            case 1: {
                type = PotionType.REGEN;
                break;
            }
            case 2: {
                type = PotionType.SPEED;
                break;
            }
            case 3: {
                type = PotionType.FIRE_RESISTANCE;
                break;
            }
            case 4: {
                type = PotionType.POISON;
                break;
            }
            case 5: {
                type = PotionType.INSTANT_HEAL;
                break;
            }
            case 6: {
                type = PotionType.NIGHT_VISION;
                break;
            }
            case 8: {
                type = PotionType.WEAKNESS;
                break;
            }
            case 9: {
                type = PotionType.STRENGTH;
                break;
            }
            case 10: {
                type = PotionType.SLOWNESS;
                break;
            }
            case 11: {
                type = PotionType.JUMP;
                break;
            }
            case 12: {
                type = PotionType.INSTANT_DAMAGE;
                break;
            }
            case 13: {
                type = PotionType.WATER_BREATHING;
                break;
            }
            case 14: {
                type = PotionType.INVISIBILITY;
                break;
            }
            default: {
                type = PotionType.WATER;
            }
        }
        if (type == null || type == PotionType.WATER) {
            potion = new Potion(PotionType.WATER);
        } else {
            int level = (damage & 32) >> 5;
            potion = new Potion(type, ++level);
        }
        if ((damage & 16384) != 0) {
            potion = potion.splash();
        }
        if ((damage & 64) != 0) {
            potion = potion.extend();
        }
        return potion;
    }

    public static Potion fromItemStack(ItemStack item) {
        Validate.notNull((Object)item, (String)"item cannot be null");
        if (item.getType() != Material.POTION) {
            throw new IllegalArgumentException("item is not a potion");
        }
        return Potion.fromDamage(item.getDurability());
    }

    public static PotionBrewer getBrewer() {
        return brewer;
    }

    public static void setPotionBrewer(PotionBrewer other) {
        if (brewer != null) {
            throw new IllegalArgumentException("brewer can only be set internally");
        }
        brewer = other;
    }

    @Deprecated
    public int getNameId() {
        return 0;
    }
}

