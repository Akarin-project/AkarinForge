package net.minecraft.entity;

import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public abstract class EntityAgeable extends EntityCreature
{
    private static final DataParameter<Boolean> BABY = EntityDataManager.<Boolean>createKey(EntityAgeable.class, DataSerializers.BOOLEAN);
    protected int growingAge;
    protected int forcedAge;
    protected int forcedAgeTimer;
    private float ageWidth = -1.0F;
    private float ageHeight;

    public EntityAgeable(World worldIn)
    {
        super(worldIn);
    }

    @Nullable
    public abstract EntityAgeable createChild(EntityAgeable ageable);

    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);

        if (itemstack.getItem() == Items.SPAWN_EGG)
        {
            if (!this.world.isRemote)
            {
                Class <? extends Entity > oclass = EntityList.getClass(ItemMonsterPlacer.getNamedIdFrom(itemstack));

                if (oclass != null && this.getClass() == oclass)
                {
                    EntityAgeable entityageable = this.createChild(this);

                    if (entityageable != null)
                    {
                        entityageable.setGrowingAge(-24000);
                        entityageable.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
                        this.world.spawnEntity(entityageable);

                        if (itemstack.hasDisplayName())
                        {
                            entityageable.setCustomNameTag(itemstack.getDisplayName());
                        }

                        if (!player.capabilities.isCreativeMode)
                        {
                            itemstack.shrink(1);
                        }
                    }
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    protected boolean holdingSpawnEggOfClass(ItemStack stack, Class <? extends Entity > entityClass)
    {
        if (stack.getItem() != Items.SPAWN_EGG)
        {
            return false;
        }
        else
        {
            Class <? extends Entity > oclass = EntityList.getClass(ItemMonsterPlacer.getNamedIdFrom(stack));
            return oclass != null && entityClass == oclass;
        }
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(BABY, Boolean.valueOf(false));
    }

    public int getGrowingAge()
    {
        if (this.world.isRemote)
        {
            return ((Boolean)this.dataManager.get(BABY)).booleanValue() ? -1 : 1;
        }
        else
        {
            return this.growingAge;
        }
    }

    public void ageUp(int growthSeconds, boolean updateForcedAge)
    {
        int i = this.getGrowingAge();
        int j = i;
        i = i + growthSeconds * 20;

        if (i > 0)
        {
            i = 0;

            if (j < 0)
            {
                this.onGrowingAdult();
            }
        }

        int k = i - j;
        this.setGrowingAge(i);

        if (updateForcedAge)
        {
            this.forcedAge += k;

            if (this.forcedAgeTimer == 0)
            {
                this.forcedAgeTimer = 40;
            }
        }

        if (this.getGrowingAge() == 0)
        {
            this.setGrowingAge(this.forcedAge);
        }
    }

    public void addGrowth(int growth)
    {
        this.ageUp(growth, false);
    }

    public void setGrowingAge(int age)
    {
        this.dataManager.set(BABY, Boolean.valueOf(age < 0));
        this.growingAge = age;
        this.setScaleForAge(this.isChild());
    }

    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("Age", this.getGrowingAge());
        compound.setInteger("ForcedAge", this.forcedAge);
    }

    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.setGrowingAge(compound.getInteger("Age"));
        this.forcedAge = compound.getInteger("ForcedAge");
    }

    public void notifyDataManagerChange(DataParameter<?> key)
    {
        if (BABY.equals(key))
        {
            this.setScaleForAge(this.isChild());
        }

        super.notifyDataManagerChange(key);
    }

    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (this.world.isRemote)
        {
            if (this.forcedAgeTimer > 0)
            {
                if (this.forcedAgeTimer % 4 == 0)
                {
                    this.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + 0.5D + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, 0.0D, 0.0D, 0.0D);
                }

                --this.forcedAgeTimer;
            }
        }
        else
        {
            int i = this.getGrowingAge();

            if (i < 0)
            {
                ++i;
                this.setGrowingAge(i);

                if (i == 0)
                {
                    this.onGrowingAdult();
                }
            }
            else if (i > 0)
            {
                --i;
                this.setGrowingAge(i);
            }
        }
    }

    protected void onGrowingAdult()
    {
    }

    public boolean isChild()
    {
        return this.getGrowingAge() < 0;
    }

    public void setScaleForAge(boolean child)
    {
        this.setScale(child ? 0.5F : 1.0F);
    }

    protected final void setSize(float width, float height)
    {
        boolean flag = this.ageWidth > 0.0F;
        this.ageWidth = width;
        this.ageHeight = height;

        if (!flag)
        {
            this.setScale(1.0F);
        }
    }

    protected final void setScale(float scale)
    {
        super.setSize(this.ageWidth * scale, this.ageHeight * scale);
    }
}