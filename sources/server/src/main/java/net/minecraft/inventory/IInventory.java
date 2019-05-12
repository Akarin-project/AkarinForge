package net.minecraft.inventory;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.InventoryHolder;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IWorldNameable;

public interface IInventory extends IWorldNameable
{
    int getSizeInventory();

    boolean isEmpty();

    ItemStack getStackInSlot(int index);

    ItemStack decrStackSize(int index, int count);

    ItemStack removeStackFromSlot(int index);

    void setInventorySlotContents(int index, ItemStack stack);

    int getInventoryStackLimit();

    void markDirty();

    boolean isUsableByPlayer(EntityPlayer player);

    void openInventory(EntityPlayer player);

    void closeInventory(EntityPlayer player);

    boolean isItemValidForSlot(int index, ItemStack stack);

    int getField(int id);

    void setField(int id, int value);

    int getFieldCount();

    void clear();
    // Akarin start
    public List<ItemStack> getContents();

    public void onOpen(CraftHumanEntity var1);

    public void onClose(CraftHumanEntity var1);

    public List<HumanEntity> getViewers();

    public InventoryHolder getOwner();

    public void setMaxStackSize(int var1);

    public Location getLocation();
    // Akarin end
}