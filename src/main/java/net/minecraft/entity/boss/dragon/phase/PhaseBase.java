package net.minecraft.entity.boss.dragon.phase;

import javax.annotation.Nullable;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public abstract class PhaseBase implements IPhase
{
    protected final EntityDragon dragon;

    public PhaseBase(EntityDragon dragonIn)
    {
        this.dragon = dragonIn;
    }

    public boolean getIsStationary()
    {
        return false;
    }

    public void doClientRenderEffects()
    {
    }

    public void doLocalUpdate()
    {
    }

    public void onCrystalDestroyed(EntityEnderCrystal crystal, BlockPos pos, DamageSource dmgSrc, @Nullable EntityPlayer plyr)
    {
    }

    public void initPhase()
    {
    }

    public void removeAreaEffect()
    {
    }

    public float getMaxRiseOrFall()
    {
        return 0.6F;
    }

    @Nullable
    public Vec3d getTargetLocation()
    {
        return null;
    }

    public float getAdjustedDamage(MultiPartEntityPart pt, DamageSource src, float damage)
    {
        return damage;
    }

    public float getYawFactor()
    {
        float f = MathHelper.sqrt(this.dragon.motionX * this.dragon.motionX + this.dragon.motionZ * this.dragon.motionZ) + 1.0F;
        float f1 = Math.min(f, 40.0F);
        return 0.7F / f1 / f;
    }
}