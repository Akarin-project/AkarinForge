package net.minecraft.inventory;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class InventoryBasic implements IInventory
{
    private String inventoryTitle;
    private final int slotsCount;
    private final NonNullList<ItemStack> inventoryContents;
    private List<IInventoryChangedListener> changeListeners;
    private boolean hasCustomName;

    public InventoryBasic(String title, boolean customName, int slotCount)
    {
        this.inventoryTitle = title;
        this.hasCustomName = customName;
        this.slotsCount = slotCount;
        this.inventoryContents = NonNullList.<ItemStack>withSize(slotCount, ItemStack.EMPTY);
    }

    @SideOnly(Side.CLIENT)
    public InventoryBasic(ITextComponent title, int slotCount)
    {
        this(title.getUnformattedText(), true, slotCount);
    }

    public void addInventoryChangeListener(IInventoryChangedListener listener)
    {
        if (this.changeListeners == null)
        {
            this.changeListeners = Lists.<IInventoryChangedListener>newArrayList();
        }

        this.changeListeners.add(listener);
    }

    public void removeInventoryChangeListener(IInventoryChangedListener listener)
    {
        this.changeListeners.remove(listener);
    }

    public ItemStack getStackInSlot(int index)
    {
        return index >= 0 && index < this.inventoryContents.size() ? (ItemStack)this.inventoryContents.get(index) : ItemStack.EMPTY;
    }

    public ItemStack decrStackSize(int index, int count)
    {
        ItemStack itemstack = ItemStackHelper.getAndSplit(this.inventoryContents, index, count);

        if (!itemstack.isEmpty())
        {
            this.markDirty();
        }

        return itemstack;
    }

    public ItemStack addItem(ItemStack stack)
    {
        ItemStack itemstack = stack.copy();

        for (int i = 0; i < this.slotsCount; ++i)
        {
            ItemStack itemstack1 = this.getStackInSlot(i);

            if (itemstack1.isEmpty())
            {
                this.setInventorySlotContents(i, itemstack);
                this.markDirty();
                return ItemStack.EMPTY;
            }

            if (ItemStack.areItemsEqual(itemstack1, itemstack))
            {
                int j = Math.min(this.getInventoryStackLimit(), itemstack1.getMaxStackSize());
                int k = Math.min(itemstack.getCount(), j - itemstack1.getCount());

                if (k > 0)
                {
                    itemstack1.grow(k);
                    itemstack.shrink(k);

                    if (itemstack.isEmpty())
                    {
                        this.markDirty();
                        return ItemStack.EMPTY;
                    }
                }
            }
        }

        if (itemstack.getCount() != stack.getCount())
        {
            this.markDirty();
        }

        return itemstack;
    }

    public ItemStack removeStackFromSlot(int index)
    {
        ItemStack itemstack = this.inventoryContents.get(index);

        if (itemstack.isEmpty())
        {
            return ItemStack.EMPTY;
        }
        else
        {
            this.inventoryContents.set(index, ItemStack.EMPTY);
            return itemstack;
        }
    }

    public void setInventorySlotContents(int index, ItemStack stack)
    {
        this.inventoryContents.set(index, stack);

        if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }

        this.markDirty();
    }

    public int getSizeInventory()
    {
        return this.slotsCount;
    }

    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.inventoryContents)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    public String getName()
    {
        return this.inventoryTitle;
    }

    public boolean hasCustomName()
    {
        return this.hasCustomName;
    }

    public void setCustomName(String inventoryTitleIn)
    {
        this.hasCustomName = true;
        this.inventoryTitle = inventoryTitleIn;
    }

    public ITextComponent getDisplayName()
    {
        return (ITextComponent)(this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]));
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public void markDirty()
    {
        if (this.changeListeners != null)
        {
            for (int i = 0; i < this.changeListeners.size(); ++i)
            {
                ((IInventoryChangedListener)this.changeListeners.get(i)).onInventoryChanged(this);
            }
        }
    }

    public boolean isUsableByPlayer(EntityPlayer player)
    {
        return true;
    }

    public void openInventory(EntityPlayer player)
    {
    }

    public void closeInventory(EntityPlayer player)
    {
    }

    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return true;
    }

    public int getField(int id)
    {
        return 0;
    }

    public void setField(int id, int value)
    {
    }

    public int getFieldCount()
    {
        return 0;
    }

    public void clear()
    {
        this.inventoryContents.clear();
    }
}