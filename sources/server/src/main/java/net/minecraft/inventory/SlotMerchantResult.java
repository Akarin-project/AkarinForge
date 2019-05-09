package net.minecraft.inventory;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.village.MerchantRecipe;

public class SlotMerchantResult extends Slot
{
    private final InventoryMerchant merchantInventory;
    private final EntityPlayer player;
    private int removeCount;
    private final IMerchant merchant;

    public SlotMerchantResult(EntityPlayer player, IMerchant merchant, InventoryMerchant merchantInventory, int slotIndex, int xPosition, int yPosition)
    {
        super(merchantInventory, slotIndex, xPosition, yPosition);
        this.player = player;
        this.merchant = merchant;
        this.merchantInventory = merchantInventory;
    }

    public boolean isItemValid(ItemStack stack)
    {
        return false;
    }

    public ItemStack decrStackSize(int amount)
    {
        if (this.getHasStack())
        {
            this.removeCount += Math.min(amount, this.getStack().getCount());
        }

        return super.decrStackSize(amount);
    }

    protected void onCrafting(ItemStack stack, int amount)
    {
        this.removeCount += amount;
        this.onCrafting(stack);
    }

    protected void onCrafting(ItemStack stack)
    {
        stack.onCrafting(this.player.world, this.player, this.removeCount);
        this.removeCount = 0;
    }

    public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack)
    {
        this.onCrafting(stack);
        MerchantRecipe merchantrecipe = this.merchantInventory.getCurrentRecipe();

        if (merchantrecipe != null)
        {
            ItemStack itemstack = this.merchantInventory.getStackInSlot(0);
            ItemStack itemstack1 = this.merchantInventory.getStackInSlot(1);

            if (this.doTrade(merchantrecipe, itemstack, itemstack1) || this.doTrade(merchantrecipe, itemstack1, itemstack))
            {
                this.merchant.useRecipe(merchantrecipe);
                thePlayer.addStat(StatList.TRADED_WITH_VILLAGER);
                this.merchantInventory.setInventorySlotContents(0, itemstack);
                this.merchantInventory.setInventorySlotContents(1, itemstack1);
            }
        }

        return stack;
    }

    private boolean doTrade(MerchantRecipe trade, ItemStack firstItem, ItemStack secondItem)
    {
        ItemStack itemstack = trade.getItemToBuy();
        ItemStack itemstack1 = trade.getSecondItemToBuy();

        if (firstItem.getItem() == itemstack.getItem() && firstItem.getCount() >= itemstack.getCount())
        {
            if (!itemstack1.isEmpty() && !secondItem.isEmpty() && itemstack1.getItem() == secondItem.getItem() && secondItem.getCount() >= itemstack1.getCount())
            {
                firstItem.shrink(itemstack.getCount());
                secondItem.shrink(itemstack1.getCount());
                return true;
            }

            if (itemstack1.isEmpty() && secondItem.isEmpty())
            {
                firstItem.shrink(itemstack.getCount());
                return true;
            }
        }

        return false;
    }
}