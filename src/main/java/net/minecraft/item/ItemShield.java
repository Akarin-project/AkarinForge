package net.minecraft.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.BlockDispenser;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemShield extends Item
{
    public ItemShield()
    {
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.COMBAT);
        this.setMaxDamage(336);
        this.addPropertyOverride(new ResourceLocation("blocking"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {
                return entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, ItemArmor.DISPENSER_BEHAVIOR);
    }

    public String getItemStackDisplayName(ItemStack stack)
    {
        if (stack.getSubCompound("BlockEntityTag") != null)
        {
            EnumDyeColor enumdyecolor = TileEntityBanner.getColor(stack);
            return I18n.translateToLocal("item.shield." + enumdyecolor.getUnlocalizedName() + ".name");
        }
        else
        {
            return I18n.translateToLocal("item.shield.name");
        }
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        ItemBanner.appendHoverTextFromTileEntityTag(stack, tooltip);
    }

    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BLOCK;
    }

    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 72000;
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }

    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
        return repair.getItem() == Item.getItemFromBlock(Blocks.PLANKS) ? true : super.getIsRepairable(toRepair, repair);
    }
}