package org.bukkit.craftbukkit.inventory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.text.TextComponentString;

import net.minecraft.util.text.ITextComponent;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class CraftInventoryCustom extends CraftInventory {
    public CraftInventoryCustom(InventoryHolder owner, InventoryType type) {
        super(new MinecraftInventory(owner, type));
    }

    public CraftInventoryCustom(InventoryHolder owner, InventoryType type, String title) {
        super(new MinecraftInventory(owner, type, title));
    }

    public CraftInventoryCustom(InventoryHolder owner, int size) {
        super(new MinecraftInventory(owner, size));
    }

    public CraftInventoryCustom(InventoryHolder owner, int size, String title) {
        super(new MinecraftInventory(owner, size, title));
    }
    
    // Akarin start
    public CraftInventoryCustom(InventoryHolder owner, NonNullList<ItemStack> items) {
        super(new MinecraftInventory(owner, items));
    }
    // Akarin end

    static class MinecraftInventory implements IInventory {
        private final NonNullList<ItemStack> items;
        private int maxStack = 64;
        private final List<HumanEntity> viewers;
        private final String title;
        private InventoryType type;
        private final InventoryHolder owner;

        public MinecraftInventory(InventoryHolder owner, InventoryType type) {
            this(owner, type.getDefaultSize(), type.getDefaultTitle());
            this.type = type;
        }

        public MinecraftInventory(InventoryHolder owner, InventoryType type, String title) {
            this(owner, type.getDefaultSize(), title);
            this.type = type;
        }

        public MinecraftInventory(InventoryHolder owner, int size) {
            this(owner, size, "Chest");
        }

        public MinecraftInventory(InventoryHolder owner, int size, String title) {
            Validate.notNull(title, "Title cannot be null");
            this.items = NonNullList.withSize(size, ItemStack.EMPTY);
            this.title = title;
            this.viewers = new ArrayList<HumanEntity>();
            this.owner = owner;
            this.type = InventoryType.CHEST;
        }
        
        // Akarin start
        public MinecraftInventory(InventoryHolder owner, NonNullList<ItemStack> items) {
            this.items = items;
            this.title = "Chest";
            this.viewers = new ArrayList<HumanEntity>();
            this.owner = owner;
            this.type = InventoryType.CHEST;
        }
        // Akarin end

        public int getSizeInventory() {
            return items.size();
        }

        public ItemStack getStackInSlot(int i) {
            return items.get(i);
        }

        public ItemStack decrStackSize(int i, int j) {
            ItemStack stack = this.getStackInSlot(i);
            ItemStack result;
            if (stack == ItemStack.EMPTY) return stack;
            if (stack.getCount() <= j) {
                this.setInventorySlotContents(i, ItemStack.EMPTY);
                result = stack;
            } else {
                result = CraftItemStack.copyNMSStack(stack, j);
                stack.shrink(j);
            }
            this.markDirty();
            return result;
        }

        public ItemStack removeStackFromSlot(int i) {
            ItemStack stack = this.getStackInSlot(i);
            ItemStack result;
            if (stack == ItemStack.EMPTY) return stack;
            if (stack.getCount() <= 1) {
                this.setInventorySlotContents(i, null);
                result = stack;
            } else {
                result = CraftItemStack.copyNMSStack(stack, 1);
                stack.shrink(1);
            }
            return result;
        }

        public void setInventorySlotContents(int i, ItemStack itemstack) {
            items.set(i, itemstack);
            if (itemstack != ItemStack.EMPTY && this.getInventoryStackLimit() > 0 && itemstack.getCount() > this.getInventoryStackLimit()) {
                itemstack.setCount(this.getInventoryStackLimit());
            }
        }

        public int getInventoryStackLimit() {
            return maxStack;
        }

        public void setMaxStackSize(int size) {
            maxStack = size;
        }

        public void markDirty() {}

        public boolean isUsableByPlayer(EntityPlayer entityhuman) {
            return true;
        }

        public List<ItemStack> getContents() {
            return items;
        }

        public void onOpen(CraftHumanEntity who) {
            viewers.add(who);
        }

        public void onClose(CraftHumanEntity who) {
            viewers.remove(who);
        }

        public List<HumanEntity> getViewers() {
            return viewers;
        }

        public InventoryType getType() {
            return type;
        }

        public InventoryHolder getOwner() {
            return owner;
        }

        public boolean isItemValidForSlot(int i, ItemStack itemstack) {
            return true;
        }

        @Override
        public void openInventory(EntityPlayer entityHuman) {

        }

        @Override
        public void closeInventory(EntityPlayer entityHuman) {

        }

        @Override
        public int getField(int i) {
            return 0;
        }

        @Override
        public void setField(int i, int j) {
        }

        @Override
        public int getFieldCount() {
            return 0;
        }

        @Override
        public void clear() {
            items.clear();
        }

        @Override
        public String getName() {
            return title;
        }

        @Override
        public boolean hasCustomName() {
            return title != null;
        }

        @Override
        public ITextComponent getDisplayName() {
            return new TextComponentString(title);
        }

        @Override
        public Location getLocation() {
            return null;
        }

        @Override
        public boolean isEmpty() {
            Iterator iterator = this.items.iterator();

            ItemStack itemstack;

            do {
                if (!iterator.hasNext()) {
                    return true;
                }

                itemstack = (ItemStack) iterator.next();
            } while (itemstack.isEmpty());

            return false;
        }
    }
}
