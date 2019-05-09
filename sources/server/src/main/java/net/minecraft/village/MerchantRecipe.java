package net.minecraft.village;

import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMerchantRecipe;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MerchantRecipe
{
    public ItemStack itemToBuy; // Akarin
    public ItemStack secondItemToBuy; // Akarin
    public ItemStack itemToSell; // Akarin
    public int toolUses; // Akarin
    public int maxTradeUses; // Akarin
    public boolean rewardsExp; // Akarin
    // CraftBukkit start
    private CraftMerchantRecipe bukkitHandle;

    public CraftMerchantRecipe asBukkit() {
        return (bukkitHandle == null) ? bukkitHandle = new CraftMerchantRecipe(this) : bukkitHandle;
    }

    public MerchantRecipe(ItemStack itemstack, ItemStack itemstack1, ItemStack itemstack2, int i, int j, CraftMerchantRecipe bukkit) {
        this(itemstack, itemstack1, itemstack2, i, j);
        this.bukkitHandle = bukkit;
    }
    // CraftBukkit end

    public MerchantRecipe(NBTTagCompound tagCompound)
    {
        this.itemToBuy = ItemStack.EMPTY;
        this.secondItemToBuy = ItemStack.EMPTY;
        this.itemToSell = ItemStack.EMPTY;
        this.readFromTags(tagCompound);
    }

    public MerchantRecipe(ItemStack buy1, ItemStack buy2, ItemStack sell)
    {
        this(buy1, buy2, sell, 0, 7);
    }

    public MerchantRecipe(ItemStack buy1, ItemStack buy2, ItemStack sell, int toolUsesIn, int maxTradeUsesIn)
    {
        this.itemToBuy = ItemStack.EMPTY;
        this.secondItemToBuy = ItemStack.EMPTY;
        this.itemToSell = ItemStack.EMPTY;
        this.itemToBuy = buy1;
        this.secondItemToBuy = buy2;
        this.itemToSell = sell;
        this.toolUses = toolUsesIn;
        this.maxTradeUses = maxTradeUsesIn;
        this.rewardsExp = true;
    }

    public MerchantRecipe(ItemStack buy1, ItemStack sell)
    {
        this(buy1, ItemStack.EMPTY, sell);
    }

    public MerchantRecipe(ItemStack buy1, Item sellItem)
    {
        this(buy1, new ItemStack(sellItem));
    }

    public ItemStack getItemToBuy()
    {
        return this.itemToBuy;
    }

    public ItemStack getSecondItemToBuy()
    {
        return this.secondItemToBuy;
    }

    public boolean hasSecondItemToBuy()
    {
        return !this.secondItemToBuy.isEmpty();
    }

    public ItemStack getItemToSell()
    {
        return this.itemToSell;
    }

    public int getToolUses()
    {
        return this.toolUses;
    }

    public int getMaxTradeUses()
    {
        return this.maxTradeUses;
    }

    public void incrementToolUses()
    {
        ++this.toolUses;
    }

    public void increaseMaxTradeUses(int increment)
    {
        this.maxTradeUses += increment;
    }

    public boolean isRecipeDisabled()
    {
        return this.toolUses >= this.maxTradeUses;
    }

    @SideOnly(Side.CLIENT)
    public void compensateToolUses()
    {
        this.toolUses = this.maxTradeUses;
    }

    public boolean getRewardsExp()
    {
        return this.rewardsExp;
    }

    public void readFromTags(NBTTagCompound tagCompound)
    {
        NBTTagCompound nbttagcompound = tagCompound.getCompoundTag("buy");
        this.itemToBuy = new ItemStack(nbttagcompound);
        NBTTagCompound nbttagcompound1 = tagCompound.getCompoundTag("sell");
        this.itemToSell = new ItemStack(nbttagcompound1);

        if (tagCompound.hasKey("buyB", 10))
        {
            this.secondItemToBuy = new ItemStack(tagCompound.getCompoundTag("buyB"));
        }

        if (tagCompound.hasKey("uses", 99))
        {
            this.toolUses = tagCompound.getInteger("uses");
        }

        if (tagCompound.hasKey("maxUses", 99))
        {
            this.maxTradeUses = tagCompound.getInteger("maxUses");
        }
        else
        {
            this.maxTradeUses = 7;
        }

        if (tagCompound.hasKey("rewardExp", 1))
        {
            this.rewardsExp = tagCompound.getBoolean("rewardExp");
        }
        else
        {
            this.rewardsExp = true;
        }
    }

    public NBTTagCompound writeToTags()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setTag("buy", this.itemToBuy.writeToNBT(new NBTTagCompound()));
        nbttagcompound.setTag("sell", this.itemToSell.writeToNBT(new NBTTagCompound()));

        if (!this.secondItemToBuy.isEmpty())
        {
            nbttagcompound.setTag("buyB", this.secondItemToBuy.writeToNBT(new NBTTagCompound()));
        }

        nbttagcompound.setInteger("uses", this.toolUses);
        nbttagcompound.setInteger("maxUses", this.maxTradeUses);
        nbttagcompound.setBoolean("rewardExp", this.rewardsExp);
        return nbttagcompound;
    }
}