package net.minecraft.entity;

public interface IRangedAttackMob
{
    void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor);

    void setSwingingArms(boolean swingingArms);
}