package net.minecraft.util;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.Explosion;

public class DamageSource
{
    public static final DamageSource IN_FIRE = (new DamageSource("inFire")).setFireDamage();
    public static final DamageSource LIGHTNING_BOLT = new DamageSource("lightningBolt");
    public static final DamageSource ON_FIRE = (new DamageSource("onFire")).setDamageBypassesArmor().setFireDamage();
    public static final DamageSource LAVA = (new DamageSource("lava")).setFireDamage();
    public static final DamageSource HOT_FLOOR = (new DamageSource("hotFloor")).setFireDamage();
    public static final DamageSource IN_WALL = (new DamageSource("inWall")).setDamageBypassesArmor();
    public static final DamageSource CRAMMING = (new DamageSource("cramming")).setDamageBypassesArmor();
    public static final DamageSource DROWN = (new DamageSource("drown")).setDamageBypassesArmor();
    public static final DamageSource STARVE = (new DamageSource("starve")).setDamageBypassesArmor().setDamageIsAbsolute();
    public static final DamageSource CACTUS = new DamageSource("cactus");
    public static final DamageSource FALL = (new DamageSource("fall")).setDamageBypassesArmor();
    public static final DamageSource FLY_INTO_WALL = (new DamageSource("flyIntoWall")).setDamageBypassesArmor();
    public static final DamageSource OUT_OF_WORLD = (new DamageSource("outOfWorld")).setDamageBypassesArmor().setDamageAllowedInCreativeMode();
    public static final DamageSource GENERIC = (new DamageSource("generic")).setDamageBypassesArmor();
    public static final DamageSource MAGIC = (new DamageSource("magic")).setDamageBypassesArmor().setMagicDamage();
    public static final DamageSource WITHER = (new DamageSource("wither")).setDamageBypassesArmor();
    public static final DamageSource ANVIL = new DamageSource("anvil");
    public static final DamageSource FALLING_BLOCK = new DamageSource("fallingBlock");
    public static final DamageSource DRAGON_BREATH = (new DamageSource("dragonBreath")).setDamageBypassesArmor();
    public static final DamageSource FIREWORKS = (new DamageSource("fireworks")).setExplosion();
    private boolean isUnblockable;
    private boolean isDamageAllowedInCreativeMode;
    private boolean damageIsAbsolute;
    private float hungerDamage = 0.1F;
    private boolean fireDamage;
    private boolean projectile;
    private boolean difficultyScaled;
    private boolean magicDamage;
    private boolean explosion;
    public String damageType;

    public static DamageSource causeMobDamage(EntityLivingBase mob)
    {
        return new EntityDamageSource("mob", mob);
    }

    public static DamageSource causeIndirectDamage(Entity source, EntityLivingBase indirectEntityIn)
    {
        return new EntityDamageSourceIndirect("mob", source, indirectEntityIn);
    }

    public static DamageSource causePlayerDamage(EntityPlayer player)
    {
        return new EntityDamageSource("player", player);
    }

    public static DamageSource causeArrowDamage(EntityArrow arrow, @Nullable Entity indirectEntityIn)
    {
        return (new EntityDamageSourceIndirect("arrow", arrow, indirectEntityIn)).setProjectile();
    }

    public static DamageSource causeFireballDamage(EntityFireball fireball, @Nullable Entity indirectEntityIn)
    {
        return indirectEntityIn == null ? (new EntityDamageSourceIndirect("onFire", fireball, fireball)).setFireDamage().setProjectile() : (new EntityDamageSourceIndirect("fireball", fireball, indirectEntityIn)).setFireDamage().setProjectile();
    }

    public static DamageSource causeThrownDamage(Entity source, @Nullable Entity indirectEntityIn)
    {
        return (new EntityDamageSourceIndirect("thrown", source, indirectEntityIn)).setProjectile();
    }

    public static DamageSource causeIndirectMagicDamage(Entity source, @Nullable Entity indirectEntityIn)
    {
        return (new EntityDamageSourceIndirect("indirectMagic", source, indirectEntityIn)).setDamageBypassesArmor().setMagicDamage();
    }

    public static DamageSource causeThornsDamage(Entity source)
    {
        return (new EntityDamageSource("thorns", source)).setIsThornsDamage().setMagicDamage();
    }

    public static DamageSource causeExplosionDamage(@Nullable Explosion explosionIn)
    {
        return explosionIn != null && explosionIn.getExplosivePlacedBy() != null ? (new EntityDamageSource("explosion.player", explosionIn.getExplosivePlacedBy())).setDifficultyScaled().setExplosion() : (new DamageSource("explosion")).setDifficultyScaled().setExplosion();
    }

    public static DamageSource causeExplosionDamage(@Nullable EntityLivingBase entityLivingBaseIn)
    {
        return entityLivingBaseIn != null ? (new EntityDamageSource("explosion.player", entityLivingBaseIn)).setDifficultyScaled().setExplosion() : (new DamageSource("explosion")).setDifficultyScaled().setExplosion();
    }

    public boolean isProjectile()
    {
        return this.projectile;
    }

    public DamageSource setProjectile()
    {
        this.projectile = true;
        return this;
    }

    public boolean isExplosion()
    {
        return this.explosion;
    }

    public DamageSource setExplosion()
    {
        this.explosion = true;
        return this;
    }

    public boolean isUnblockable()
    {
        return this.isUnblockable;
    }

    public float getHungerDamage()
    {
        return this.hungerDamage;
    }

    public boolean canHarmInCreative()
    {
        return this.isDamageAllowedInCreativeMode;
    }

    public boolean isDamageAbsolute()
    {
        return this.damageIsAbsolute;
    }

    public DamageSource(String damageTypeIn)
    {
        this.damageType = damageTypeIn;
    }

    @Nullable
    public Entity getImmediateSource()
    {
        return this.getTrueSource();
    }

    @Nullable
    public Entity getTrueSource()
    {
        return null;
    }

    public DamageSource setDamageBypassesArmor()
    {
        this.isUnblockable = true;
        this.hungerDamage = 0.0F;
        return this;
    }

    public DamageSource setDamageAllowedInCreativeMode()
    {
        this.isDamageAllowedInCreativeMode = true;
        return this;
    }

    public DamageSource setDamageIsAbsolute()
    {
        this.damageIsAbsolute = true;
        this.hungerDamage = 0.0F;
        return this;
    }

    public DamageSource setFireDamage()
    {
        this.fireDamage = true;
        return this;
    }

    public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn)
    {
        EntityLivingBase entitylivingbase = entityLivingBaseIn.getAttackingEntity();
        String s = "death.attack." + this.damageType;
        String s1 = s + ".player";
        return entitylivingbase != null && I18n.canTranslate(s1) ? new TextComponentTranslation(s1, new Object[] {entityLivingBaseIn.getDisplayName(), entitylivingbase.getDisplayName()}) : new TextComponentTranslation(s, new Object[] {entityLivingBaseIn.getDisplayName()});
    }

    public boolean isFireDamage()
    {
        return this.fireDamage;
    }

    public String getDamageType()
    {
        return this.damageType;
    }

    public DamageSource setDifficultyScaled()
    {
        this.difficultyScaled = true;
        return this;
    }

    public boolean isDifficultyScaled()
    {
        return this.difficultyScaled;
    }

    public boolean isMagicDamage()
    {
        return this.magicDamage;
    }

    public DamageSource setMagicDamage()
    {
        this.magicDamage = true;
        return this;
    }

    public boolean isCreativePlayer()
    {
        Entity entity = this.getTrueSource();
        return entity instanceof EntityPlayer && ((EntityPlayer)entity).capabilities.isCreativeMode;
    }

    @Nullable
    public Vec3d getDamageLocation()
    {
        return null;
    }
}