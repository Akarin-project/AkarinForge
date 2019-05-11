package net.minecraft.inventory;

import org.bukkit.Location;
import org.bukkit.inventory.InventoryHolder;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityEnderChest;

public class InventoryEnderChest extends InventoryBasic
{
    private TileEntityEnderChest associatedChest;

    public InventoryEnderChest()
    {
        super("container.enderchest", false, 27);
    }
    // Akarin start
    private EntityPlayer owner;

    public InventoryHolder getBukkitOwner() {
        return this.owner == null ? null : owner.getBukkitEntity();
    }

    @Override
    public Location getLocation() {
        return new Location(this.owner.getEntityWorld().getWorld(), this.owner.getPosition().getX(), this.owner.getPosition().getY(), this.owner.getPosition().getZ());
    }
    
    public InventoryEnderChest(EntityPlayer owner)
    {
        this();
        this.owner = owner;
    }
    // Akarin end

    public void setChestTileEntity(TileEntityEnderChest chestTileEntity)
    {
        this.associatedChest = chestTileEntity;
    }

    public void loadInventoryFromNBT(NBTTagList p_70486_1_)
    {
        for (int i = 0; i < this.getSizeInventory(); ++i)
        {
            this.setInventorySlotContents(i, ItemStack.EMPTY);
        }

        for (int k = 0; k < p_70486_1_.tagCount(); ++k)
        {
            NBTTagCompound nbttagcompound = p_70486_1_.getCompoundTagAt(k);
            int j = nbttagcompound.getByte("Slot") & 255;

            if (j >= 0 && j < this.getSizeInventory())
            {
                this.setInventorySlotContents(j, new ItemStack(nbttagcompound));
            }
        }
    }

    public NBTTagList saveInventoryToNBT()
    {
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.getSizeInventory(); ++i)
        {
            ItemStack itemstack = this.getStackInSlot(i);

            if (!itemstack.isEmpty())
            {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                itemstack.writeToNBT(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
            }
        }

        return nbttaglist;
    }

    public boolean isUsableByPlayer(EntityPlayer player)
    {
        return this.associatedChest != null && !this.associatedChest.canBeUsed(player) ? false : super.isUsableByPlayer(player);
    }

    public void openInventory(EntityPlayer player)
    {
        if (this.associatedChest != null)
        {
            this.associatedChest.openChest();
        }

        super.openInventory(player);
    }

    public void closeInventory(EntityPlayer player)
    {
        if (this.associatedChest != null)
        {
            this.associatedChest.closeChest();
        }

        super.closeInventory(player);
        this.associatedChest = null;
    }
}