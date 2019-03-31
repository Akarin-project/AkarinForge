package net.minecraft.tileentity;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IWorldNameable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityBanner extends TileEntity implements IWorldNameable
{
    private String name;
    private EnumDyeColor baseColor = EnumDyeColor.BLACK;
    private NBTTagList patterns;
    private boolean patternDataSet;
    private List<BannerPattern> patternList;
    private List<EnumDyeColor> colorList;
    private String patternResourceLocation;

    public void setItemValues(ItemStack stack, boolean p_175112_2_)
    {
        this.patterns = null;
        NBTTagCompound nbttagcompound = stack.getSubCompound("BlockEntityTag");

        if (nbttagcompound != null && nbttagcompound.hasKey("Patterns", 9))
        {
            this.patterns = nbttagcompound.getTagList("Patterns", 10).copy();
        }

        this.baseColor = p_175112_2_ ? getColor(stack) : ItemBanner.getBaseColor(stack);
        this.patternList = null;
        this.colorList = null;
        this.patternResourceLocation = "";
        this.patternDataSet = true;
        this.name = stack.hasDisplayName() ? stack.getDisplayName() : null;
    }

    public String getName()
    {
        return this.hasCustomName() ? this.name : "banner";
    }

    public boolean hasCustomName()
    {
        return this.name != null && !this.name.isEmpty();
    }

    public ITextComponent getDisplayName()
    {
        return (ITextComponent)(this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]));
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("Base", this.baseColor.getDyeDamage());

        if (this.patterns != null)
        {
            compound.setTag("Patterns", this.patterns);
        }

        if (this.hasCustomName())
        {
            compound.setString("CustomName", this.name);
        }

        return compound;
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        if (compound.hasKey("CustomName", 8))
        {
            this.name = compound.getString("CustomName");
        }

        this.baseColor = EnumDyeColor.byDyeDamage(compound.getInteger("Base"));
        this.patterns = compound.getTagList("Patterns", 10);
        this.patternList = null;
        this.colorList = null;
        this.patternResourceLocation = null;
        this.patternDataSet = true;
    }

    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(this.pos, 6, this.getUpdateTag());
    }

    public NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }

    public static int getPatterns(ItemStack stack)
    {
        NBTTagCompound nbttagcompound = stack.getSubCompound("BlockEntityTag");
        return nbttagcompound != null && nbttagcompound.hasKey("Patterns") ? nbttagcompound.getTagList("Patterns", 10).tagCount() : 0;
    }

    @SideOnly(Side.CLIENT)
    public List<BannerPattern> getPatternList()
    {
        this.initializeBannerData();
        return this.patternList;
    }

    @SideOnly(Side.CLIENT)
    public List<EnumDyeColor> getColorList()
    {
        this.initializeBannerData();
        return this.colorList;
    }

    @SideOnly(Side.CLIENT)
    public String getPatternResourceLocation()
    {
        this.initializeBannerData();
        return this.patternResourceLocation;
    }

    @SideOnly(Side.CLIENT)
    private void initializeBannerData()
    {
        if (this.patternList == null || this.colorList == null || this.patternResourceLocation == null)
        {
            if (!this.patternDataSet)
            {
                this.patternResourceLocation = "";
            }
            else
            {
                this.patternList = Lists.<BannerPattern>newArrayList();
                this.colorList = Lists.<EnumDyeColor>newArrayList();
                this.patternList.add(BannerPattern.BASE);
                this.colorList.add(this.baseColor);
                this.patternResourceLocation = "b" + this.baseColor.getDyeDamage();

                if (this.patterns != null)
                {
                    for (int i = 0; i < this.patterns.tagCount(); ++i)
                    {
                        NBTTagCompound nbttagcompound = this.patterns.getCompoundTagAt(i);
                        BannerPattern bannerpattern = BannerPattern.byHash(nbttagcompound.getString("Pattern"));

                        if (bannerpattern != null)
                        {
                            this.patternList.add(bannerpattern);
                            int j = nbttagcompound.getInteger("Color");
                            this.colorList.add(EnumDyeColor.byDyeDamage(j));
                            this.patternResourceLocation = this.patternResourceLocation + bannerpattern.getHashname() + j;
                        }
                    }
                }
            }
        }
    }

    public static void removeBannerData(ItemStack stack)
    {
        NBTTagCompound nbttagcompound = stack.getSubCompound("BlockEntityTag");

        if (nbttagcompound != null && nbttagcompound.hasKey("Patterns", 9))
        {
            NBTTagList nbttaglist = nbttagcompound.getTagList("Patterns", 10);

            if (!nbttaglist.hasNoTags())
            {
                nbttaglist.removeTag(nbttaglist.tagCount() - 1);

                if (nbttaglist.hasNoTags())
                {
                    stack.getTagCompound().removeTag("BlockEntityTag");

                    if (stack.getTagCompound().hasNoTags())
                    {
                        stack.setTagCompound((NBTTagCompound)null);
                    }
                }
            }
        }
    }

    public ItemStack getItem()
    {
        ItemStack itemstack = ItemBanner.makeBanner(this.baseColor, this.patterns);

        if (this.hasCustomName())
        {
            itemstack.setStackDisplayName(this.getName());
        }

        return itemstack;
    }

    public static EnumDyeColor getColor(ItemStack p_190616_0_)
    {
        NBTTagCompound nbttagcompound = p_190616_0_.getSubCompound("BlockEntityTag");
        return nbttagcompound != null && nbttagcompound.hasKey("Base") ? EnumDyeColor.byDyeDamage(nbttagcompound.getInteger("Base")) : EnumDyeColor.BLACK;
    }
}