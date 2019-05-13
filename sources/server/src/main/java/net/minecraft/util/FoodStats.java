/*
 * Akarin reference
 */
package net.minecraft.util;

import java.lang.reflect.Field;

import org.bukkit.craftbukkit.v1_12_R1.event.CraftEventFactory;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateHealth;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FoodStats
{
    public int foodLevel = 20;
    public float foodSaturationLevel = 5.0F;
    public float foodExhaustionLevel;
    private int foodTimer;
    private int prevFoodLevel = 20;
    // Akarin start
    private EntityPlayer entityhuman;
    
    public FoodStats() {}
    
    public FoodStats(EntityPlayer entityhuman) {
        org.apache.commons.lang.Validate.notNull(entityhuman);
        this.entityhuman = entityhuman;
        
        try {
            Field appaleCore = this.getClass().getField("entityplayer");
            appaleCore.set(this, entityhuman);
        }
        catch (IllegalAccessException | NoSuchFieldException appaleCore) {
            // empty catch block
        }
    }
    // Akarin end

    public void addStats(int foodLevelIn, float foodSaturationModifier)
    {
        this.foodLevel = Math.min(foodLevelIn + this.foodLevel, 20);
        this.foodSaturationLevel = Math.min(this.foodSaturationLevel + (float)foodLevelIn * foodSaturationModifier * 2.0F, (float)this.foodLevel);
    }

    public void addStats(ItemFood foodItem, ItemStack stack)
    {
        // Akarin start
        if (entityhuman == null) {
            this.addStats(foodItem.getHealAmount(stack), foodItem.getSaturationModifier(stack));
        } else {
            int oldFoodLevel = foodLevel;

            org.bukkit.event.entity.FoodLevelChangeEvent event = org.bukkit.craftbukkit.v1_12_R1.event.CraftEventFactory.callFoodLevelChangeEvent(entityhuman, foodItem.getHealAmount(stack) + oldFoodLevel);

            if (!event.isCancelled()) {
                this.addStats(event.getFoodLevel() - oldFoodLevel, foodItem.getSaturationModifier(stack));
            }

            ((EntityPlayerMP) entityhuman).getBukkitEntity().sendHealthUpdate();
        }
        // Akarin end
    }

    public void onUpdate(EntityPlayer player)
    {
        EnumDifficulty enumdifficulty = player.world.getDifficulty();
        this.prevFoodLevel = this.foodLevel;

        if (this.foodExhaustionLevel > 4.0F)
        {
            this.foodExhaustionLevel -= 4.0F;

            if (this.foodSaturationLevel > 0.0F)
            {
                this.foodSaturationLevel = Math.max(this.foodSaturationLevel - 1.0F, 0.0F);
            }
            else if (enumdifficulty != EnumDifficulty.PEACEFUL)
            {
                // Akarin start
                org.bukkit.event.entity.FoodLevelChangeEvent event = org.bukkit.craftbukkit.v1_12_R1.event.CraftEventFactory.callFoodLevelChangeEvent(entityhuman, Math.max(this.foodLevel - 1, 0));
                if (!event.isCancelled()) {
                    this.foodLevel = event.getFoodLevel();
                }
                ((EntityPlayerMP) entityhuman).connection.sendPacket(new SPacketUpdateHealth(((EntityPlayerMP) entityhuman).getBukkitEntity().getScaledHealth(), this.foodLevel, this.foodSaturationLevel));
                // Akarin end
            }
        }

        boolean flag = player.world.getGameRules().getBoolean("naturalRegeneration");

        if (flag && this.foodSaturationLevel > 0.0F && player.shouldHeal() && this.foodLevel >= 20)
        {
            ++this.foodTimer;

            if (this.foodTimer >= 10)
            {
                float f = Math.min(this.foodSaturationLevel, 6.0F);
                entityhuman.heal(f / 6.0F, org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason.SATIATED, true); // Akarin
                this.addExhaustion(f);
                this.foodTimer = 0;
            }
        }
        else if (flag && this.foodLevel >= 18 && player.shouldHeal())
        {
            ++this.foodTimer;

            if (this.foodTimer >= 80)
            {
                entityhuman.heal(1.0F, org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason.SATIATED); // CraftBukkit - added RegainReason
                this.addExhaustion(20); // Spigot - Change to use configurable value
                this.foodTimer = 0;
            }
        }
        else if (this.foodLevel <= 0)
        {
            ++this.foodTimer;

            if (this.foodTimer >= 80)
            {
                if (player.getHealth() > 10.0F || enumdifficulty == EnumDifficulty.HARD || player.getHealth() > 1.0F && enumdifficulty == EnumDifficulty.NORMAL)
                {
                    player.attackEntityFrom(DamageSource.STARVE, 1.0F);
                }

                this.foodTimer = 0;
            }
        }
        else
        {
            this.foodTimer = 0;
        }
    }

    public void readNBT(NBTTagCompound compound)
    {
        if (compound.hasKey("foodLevel", 99))
        {
            this.foodLevel = compound.getInteger("foodLevel");
            this.foodTimer = compound.getInteger("foodTickTimer");
            this.foodSaturationLevel = compound.getFloat("foodSaturationLevel");
            this.foodExhaustionLevel = compound.getFloat("foodExhaustionLevel");
        }
    }

    public void writeNBT(NBTTagCompound compound)
    {
        compound.setInteger("foodLevel", this.foodLevel);
        compound.setInteger("foodTickTimer", this.foodTimer);
        compound.setFloat("foodSaturationLevel", this.foodSaturationLevel);
        compound.setFloat("foodExhaustionLevel", this.foodExhaustionLevel);
    }

    public int getFoodLevel()
    {
        return this.foodLevel;
    }

    public boolean needFood()
    {
        return this.foodLevel < 20;
    }

    public void addExhaustion(float exhaustion)
    {
        this.foodExhaustionLevel = Math.min(this.foodExhaustionLevel + exhaustion, 40.0F);
    }

    public float getSaturationLevel()
    {
        return this.foodSaturationLevel;
    }

    public void setFoodLevel(int foodLevelIn)
    {
        this.foodLevel = foodLevelIn;
    }

    @SideOnly(Side.CLIENT)
    public void setFoodSaturationLevel(float foodSaturationLevelIn)
    {
        this.foodSaturationLevel = foodSaturationLevelIn;
    }
}