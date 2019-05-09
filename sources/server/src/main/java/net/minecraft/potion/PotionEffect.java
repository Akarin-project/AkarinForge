package net.minecraft.potion;

import com.google.common.collect.ComparisonChain;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PotionEffect implements Comparable<PotionEffect>
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final Potion potion;
    private int duration;
    private int amplifier;
    private boolean isSplashPotion;
    private boolean isAmbient;
    @SideOnly(Side.CLIENT)
    private boolean isPotionDurationMax;
    private boolean showParticles;
    /** List of ItemStack that can cure the potion effect **/
    private java.util.List<net.minecraft.item.ItemStack> curativeItems;

    public PotionEffect(Potion potionIn)
    {
        this(potionIn, 0, 0);
    }

    public PotionEffect(Potion potionIn, int durationIn)
    {
        this(potionIn, durationIn, 0);
    }

    public PotionEffect(Potion potionIn, int durationIn, int amplifierIn)
    {
        this(potionIn, durationIn, amplifierIn, false, true);
    }

    public PotionEffect(Potion potionIn, int durationIn, int amplifierIn, boolean ambientIn, boolean showParticlesIn)
    {
        this.potion = potionIn;
        this.duration = durationIn;
        this.amplifier = amplifierIn;
        this.isAmbient = ambientIn;
        this.showParticles = showParticlesIn;
    }

    public PotionEffect(PotionEffect other)
    {
        this.potion = other.potion;
        this.duration = other.duration;
        this.amplifier = other.amplifier;
        this.isAmbient = other.isAmbient;
        this.showParticles = other.showParticles;
        this.curativeItems = other.curativeItems == null ? null : new java.util.ArrayList<net.minecraft.item.ItemStack>(other.curativeItems);
    }

    public void combine(PotionEffect other)
    {
        if (this.potion != other.potion)
        {
            LOGGER.warn("This method should only be called for matching effects!");
        }

        if (other.amplifier > this.amplifier)
        {
            this.amplifier = other.amplifier;
            this.duration = other.duration;
        }
        else if (other.amplifier == this.amplifier && this.duration < other.duration)
        {
            this.duration = other.duration;
        }
        else if (!other.isAmbient && this.isAmbient)
        {
            this.isAmbient = other.isAmbient;
        }

        this.showParticles = other.showParticles;
    }

    public Potion getPotion()
    {
        return this.getPotionRaw() == null ? null : this.getPotionRaw().delegate.get();
    }

    private Potion getPotionRaw()
    {
        return this.potion;
    }

    public int getDuration()
    {
        return this.duration;
    }

    public int getAmplifier()
    {
        return this.amplifier;
    }

    public boolean getIsAmbient()
    {
        return this.isAmbient;
    }

    public boolean doesShowParticles()
    {
        return this.showParticles;
    }

    public boolean onUpdate(EntityLivingBase entityIn)
    {
        if (this.duration > 0)
        {
            if (this.potion.isReady(this.duration, this.amplifier))
            {
                this.performEffect(entityIn);
            }

            this.deincrementDuration();
        }

        return this.duration > 0;
    }

    private int deincrementDuration()
    {
        return --this.duration;
    }

    public void performEffect(EntityLivingBase entityIn)
    {
        if (this.duration > 0)
        {
            this.potion.performEffect(entityIn, this.amplifier);
        }
    }

    public String getEffectName()
    {
        return this.potion.getName();
    }

    public String toString()
    {
        String s;

        if (this.amplifier > 0)
        {
            s = this.getEffectName() + " x " + (this.amplifier + 1) + ", Duration: " + this.duration;
        }
        else
        {
            s = this.getEffectName() + ", Duration: " + this.duration;
        }

        if (this.isSplashPotion)
        {
            s = s + ", Splash: true";
        }

        if (!this.showParticles)
        {
            s = s + ", Particles: false";
        }

        return s;
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (!(p_equals_1_ instanceof PotionEffect))
        {
            return false;
        }
        else
        {
            PotionEffect potioneffect = (PotionEffect)p_equals_1_;
            return this.duration == potioneffect.duration && this.amplifier == potioneffect.amplifier && this.isSplashPotion == potioneffect.isSplashPotion && this.isAmbient == potioneffect.isAmbient && this.potion.equals(potioneffect.potion);
        }
    }

    public int hashCode()
    {
        int i = this.potion.hashCode();
        i = 31 * i + this.duration;
        i = 31 * i + this.amplifier;
        i = 31 * i + (this.isSplashPotion ? 1 : 0);
        i = 31 * i + (this.isAmbient ? 1 : 0);
        return i;
    }

    public NBTTagCompound writeCustomPotionEffectToNBT(NBTTagCompound nbt)
    {
        nbt.setByte("Id", (byte)Potion.getIdFromPotion(this.getPotion()));
        nbt.setByte("Amplifier", (byte)this.getAmplifier());
        nbt.setInteger("Duration", this.getDuration());
        nbt.setBoolean("Ambient", this.getIsAmbient());
        nbt.setBoolean("ShowParticles", this.doesShowParticles());
        writeCurativeItems(nbt);
        return nbt;
    }

    public static PotionEffect readCustomPotionEffectFromNBT(NBTTagCompound nbt)
    {
        int i = nbt.getByte("Id") & 0xFF;
        Potion potion = Potion.getPotionById(i);

        if (potion == null)
        {
            return null;
        }
        else
        {
            int j = nbt.getByte("Amplifier");
            int k = nbt.getInteger("Duration");
            boolean flag = nbt.getBoolean("Ambient");
            boolean flag1 = true;

            if (nbt.hasKey("ShowParticles", 1))
            {
                flag1 = nbt.getBoolean("ShowParticles");
            }

            return readCurativeItems(new PotionEffect(potion, k, j < 0 ? 0 : j, flag, flag1), nbt);
        }
    }

    @SideOnly(Side.CLIENT)
    public void setPotionDurationMax(boolean maxDuration)
    {
        this.isPotionDurationMax = maxDuration;
    }

    public int compareTo(PotionEffect p_compareTo_1_)
    {
        int i = 32147;
        return (this.getDuration() <= 32147 || p_compareTo_1_.getDuration() <= 32147) && (!this.getIsAmbient() || !p_compareTo_1_.getIsAmbient()) ? ComparisonChain.start().compare(Boolean.valueOf(this.getIsAmbient()), Boolean.valueOf(p_compareTo_1_.getIsAmbient())).compare(this.getDuration(), p_compareTo_1_.getDuration()).compare(this.getPotion().getGuiSortColor(this), p_compareTo_1_.getPotion().getGuiSortColor(p_compareTo_1_)).result() : ComparisonChain.start().compare(Boolean.valueOf(this.getIsAmbient()), Boolean.valueOf(p_compareTo_1_.getIsAmbient())).compare(this.getPotion().getGuiSortColor(this), p_compareTo_1_.getPotion().getGuiSortColor(p_compareTo_1_)).result();
    }

    @SideOnly(Side.CLIENT)
    public boolean getIsPotionDurationMax()
    {
        return this.isPotionDurationMax;
    }

    /* ======================================== FORGE START =====================================*/
    /***
     * Returns a list of curative items for the potion effect
     * By default, this list is initialized using {@link Potion#getCurativeItems}
     *
     * @return The list (ItemStack) of curative items for the potion effect
     */
    public java.util.List<net.minecraft.item.ItemStack> getCurativeItems()
    {
        if (this.curativeItems == null) //Lazy load this so that we don't create a circular dep on Items.
        {
            this.curativeItems = getPotion().getCurativeItems();
        }
        return this.curativeItems;
    }

    /***
     * Checks the given ItemStack to see if it is in the list of curative items for the potion effect
     * @param stack The ItemStack being checked against the list of curative items for this PotionEffect
     * @return true if the given ItemStack is in the list of curative items for this PotionEffect, false otherwise
     */
    public boolean isCurativeItem(net.minecraft.item.ItemStack stack)
    {
        for (net.minecraft.item.ItemStack curativeItem : this.getCurativeItems())
        {
            if (curativeItem.isItemEqual(stack))
            {
                return true;
            }
        }

        return false;
    }

    /***
     * Sets the list of curative items for this potion effect, overwriting any already present
     * @param curativeItems The list of ItemStacks being set to the potion effect
     */
    public void setCurativeItems(java.util.List<net.minecraft.item.ItemStack> curativeItems)
    {
        this.curativeItems = curativeItems;
    }

    /***
     * Adds the given stack to the list of curative items for this PotionEffect
     * @param stack The ItemStack being added to the curative item list
     */
    public void addCurativeItem(net.minecraft.item.ItemStack stack)
    {
        if (!this.isCurativeItem(stack))
        {
            this.getCurativeItems().add(stack);
        }
    }

    private void writeCurativeItems(NBTTagCompound nbt)
    {
        net.minecraft.nbt.NBTTagList list = new net.minecraft.nbt.NBTTagList();
        for (net.minecraft.item.ItemStack stack : getCurativeItems())
        {
            list.appendTag(stack.writeToNBT(new NBTTagCompound()));
        }
        nbt.setTag("CurativeItems", list);
    }

    private static PotionEffect readCurativeItems(PotionEffect effect, NBTTagCompound nbt)
    {
        if (nbt.hasKey("CurativeItems", net.minecraftforge.common.util.Constants.NBT.TAG_LIST))
        {
            java.util.List<net.minecraft.item.ItemStack> items = new java.util.ArrayList<net.minecraft.item.ItemStack>();
            net.minecraft.nbt.NBTTagList list = nbt.getTagList("CurativeItems", net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < list.tagCount(); i++)
            {
                items.add(new net.minecraft.item.ItemStack(list.getCompoundTagAt(i)));
            }
            effect.setCurativeItems(items);
        }

        return effect;
    }
}