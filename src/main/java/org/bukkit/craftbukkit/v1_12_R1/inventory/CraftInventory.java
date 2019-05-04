/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.Validate
 */
package org.bukkit.craftbukkit.v1_12_R1.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.apache.commons.lang3.Validate;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryAnvil;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryCustom;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryEnchanting;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_12_R1.inventory.InventoryIterator;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class CraftInventory
implements Inventory {
    protected final tv inventory;

    public CraftInventory(tv inventory) {
        this.inventory = inventory;
    }

    public tv getInventory() {
        return this.inventory;
    }

    @Override
    public int getSize() {
        return this.getInventory().w_();
    }

    @Override
    public String getName() {
        String name = this.inventory.h_();
        return name != null ? name : "MODInv_" + this.inventory.getClass().getSimpleName();
    }

    @Override
    public ItemStack getItem(int index) {
        aip item = this.getInventory().a(index);
        return item.b() ? null : CraftItemStack.asCraftMirror(item);
    }

    protected ItemStack[] asCraftMirror(List<aip> mcItems) {
        int size = mcItems.size();
        ItemStack[] items = new ItemStack[size];
        for (int i2 = 0; i2 < size; ++i2) {
            aip mcItem = mcItems.get(i2);
            items[i2] = mcItem.b() ? null : CraftItemStack.asCraftMirror(mcItem);
        }
        return items;
    }

    @Override
    public ItemStack[] getStorageContents() {
        return this.getContents();
    }

    @Override
    public void setStorageContents(ItemStack[] items) throws IllegalArgumentException {
        this.setContents(items);
    }

    @Override
    public ItemStack[] getContents() {
        List<aip> mcItems = null;
        try {
            mcItems = this.getInventory().getContents();
        }
        catch (AbstractMethodError e2) {
            return new ItemStack[0];
        }
        return this.asCraftMirror(mcItems);
    }

    @Override
    public void setContents(ItemStack[] items) {
        if (this.getSize() < items.length) {
            throw new IllegalArgumentException("Invalid inventory size; expected " + this.getSize() + " or less");
        }
        for (int i2 = 0; i2 < this.getSize(); ++i2) {
            if (i2 >= items.length) {
                this.setItem(i2, null);
                continue;
            }
            this.setItem(i2, items[i2]);
        }
    }

    @Override
    public void setItem(int index, ItemStack item) {
        this.getInventory().a(index, CraftItemStack.asNMSCopy(item));
    }

    @Override
    public boolean contains(int materialId) {
        for (ItemStack item : this.getStorageContents()) {
            if (item == null || item.getTypeId() != materialId) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean contains(Material material) {
        Validate.notNull((Object)((Object)material), (String)"Material cannot be null", (Object[])new Object[0]);
        return this.contains(material.getId());
    }

    @Override
    public boolean contains(ItemStack item) {
        if (item == null) {
            return false;
        }
        for (ItemStack i2 : this.getStorageContents()) {
            if (!item.equals(i2)) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean contains(int materialId, int amount) {
        if (amount <= 0) {
            return true;
        }
        for (ItemStack item : this.getStorageContents()) {
            if (item == null || item.getTypeId() != materialId || (amount -= item.getAmount()) > 0) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean contains(Material material, int amount) {
        Validate.notNull((Object)((Object)material), (String)"Material cannot be null", (Object[])new Object[0]);
        return this.contains(material.getId(), amount);
    }

    @Override
    public boolean contains(ItemStack item, int amount) {
        if (item == null) {
            return false;
        }
        if (amount <= 0) {
            return true;
        }
        for (ItemStack i2 : this.getStorageContents()) {
            if (!item.equals(i2) || --amount > 0) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean containsAtLeast(ItemStack item, int amount) {
        if (item == null) {
            return false;
        }
        if (amount <= 0) {
            return true;
        }
        for (ItemStack i2 : this.getStorageContents()) {
            if (!item.isSimilar(i2) || (amount -= i2.getAmount()) > 0) continue;
            return true;
        }
        return false;
    }

    public HashMap<Integer, ItemStack> all(int materialId) {
        HashMap<Integer, ItemStack> slots = new HashMap<Integer, ItemStack>();
        ItemStack[] inventory = this.getStorageContents();
        for (int i2 = 0; i2 < inventory.length; ++i2) {
            ItemStack item = inventory[i2];
            if (item == null || item.getTypeId() != materialId) continue;
            slots.put(i2, item);
        }
        return slots;
    }

    public HashMap<Integer, ItemStack> all(Material material) {
        Validate.notNull((Object)((Object)material), (String)"Material cannot be null", (Object[])new Object[0]);
        return this.all(material.getId());
    }

    public HashMap<Integer, ItemStack> all(ItemStack item) {
        HashMap<Integer, ItemStack> slots = new HashMap<Integer, ItemStack>();
        if (item != null) {
            ItemStack[] inventory = this.getStorageContents();
            for (int i2 = 0; i2 < inventory.length; ++i2) {
                if (!item.equals(inventory[i2])) continue;
                slots.put(i2, inventory[i2]);
            }
        }
        return slots;
    }

    @Override
    public int first(int materialId) {
        ItemStack[] inventory = this.getStorageContents();
        for (int i2 = 0; i2 < inventory.length; ++i2) {
            ItemStack item = inventory[i2];
            if (item == null || item.getTypeId() != materialId) continue;
            return i2;
        }
        return -1;
    }

    @Override
    public int first(Material material) {
        Validate.notNull((Object)((Object)material), (String)"Material cannot be null", (Object[])new Object[0]);
        return this.first(material.getId());
    }

    @Override
    public int first(ItemStack item) {
        return this.first(item, true);
    }

    private int first(ItemStack item, boolean withAmount) {
        if (item == null) {
            return -1;
        }
        ItemStack[] inventory = this.getStorageContents();
        for (int i2 = 0; i2 < inventory.length; ++i2) {
            if (inventory[i2] == null || !(withAmount ? item.equals(inventory[i2]) : item.isSimilar(inventory[i2]))) continue;
            return i2;
        }
        return -1;
    }

    @Override
    public int firstEmpty() {
        ItemStack[] inventory = this.getStorageContents();
        for (int i2 = 0; i2 < inventory.length; ++i2) {
            if (inventory[i2] != null) continue;
            return i2;
        }
        return -1;
    }

    public int firstPartial(int materialId) {
        ItemStack[] inventory = this.getStorageContents();
        for (int i2 = 0; i2 < inventory.length; ++i2) {
            ItemStack item = inventory[i2];
            if (item == null || item.getTypeId() != materialId || item.getAmount() >= item.getMaxStackSize()) continue;
            return i2;
        }
        return -1;
    }

    public int firstPartial(Material material) {
        Validate.notNull((Object)((Object)material), (String)"Material cannot be null", (Object[])new Object[0]);
        return this.firstPartial(material.getId());
    }

    private int firstPartial(ItemStack item) {
        ItemStack[] inventory = this.getStorageContents();
        CraftItemStack filteredItem = CraftItemStack.asCraftCopy(item);
        if (item == null) {
            return -1;
        }
        for (int i2 = 0; i2 < inventory.length; ++i2) {
            ItemStack cItem = inventory[i2];
            if (cItem == null || cItem.getAmount() >= cItem.getMaxStackSize() || !cItem.isSimilar(filteredItem)) continue;
            return i2;
        }
        return -1;
    }

    @Override
    public /* varargs */ HashMap<Integer, ItemStack> addItem(ItemStack ... items) {
        Validate.noNullElements((Object[])items, (String)"Item cannot be null", (Object[])new Object[0]);
        HashMap<Integer, ItemStack> leftover = new HashMap<Integer, ItemStack>();
        block0 : for (int i2 = 0; i2 < items.length; ++i2) {
            ItemStack item = items[i2];
            do {
                int maxAmount;
                int firstPartial;
                int partialAmount;
                if ((firstPartial = this.firstPartial(item)) == -1) {
                    int firstFree = this.firstEmpty();
                    if (firstFree == -1) {
                        leftover.put(i2, item);
                        continue block0;
                    }
                    if (item.getAmount() > this.getMaxItemStack()) {
                        CraftItemStack stack = CraftItemStack.asCraftCopy(item);
                        stack.setAmount(this.getMaxItemStack());
                        this.setItem(firstFree, stack);
                        item.setAmount(item.getAmount() - this.getMaxItemStack());
                        continue;
                    }
                    this.setItem(firstFree, item);
                    continue block0;
                }
                ItemStack partialItem = this.getItem(firstPartial);
                int amount = item.getAmount();
                if (amount + (partialAmount = partialItem.getAmount()) <= (maxAmount = partialItem.getMaxStackSize())) {
                    partialItem.setAmount(amount + partialAmount);
                    this.setItem(firstPartial, partialItem);
                    continue block0;
                }
                partialItem.setAmount(maxAmount);
                this.setItem(firstPartial, partialItem);
                item.setAmount(amount + partialAmount - maxAmount);
            } while (true);
        }
        return leftover;
    }

    @Override
    public /* varargs */ HashMap<Integer, ItemStack> removeItem(ItemStack ... items) {
        Validate.notNull((Object)items, (String)"Items cannot be null", (Object[])new Object[0]);
        HashMap<Integer, ItemStack> leftover = new HashMap<Integer, ItemStack>();
        block0 : for (int i2 = 0; i2 < items.length; ++i2) {
            ItemStack item = items[i2];
            int toDelete = item.getAmount();
            do {
                int first;
                if ((first = this.first(item, false)) == -1) {
                    item.setAmount(toDelete);
                    leftover.put(i2, item);
                    continue block0;
                }
                ItemStack itemStack = this.getItem(first);
                int amount = itemStack.getAmount();
                if (amount <= toDelete) {
                    toDelete -= amount;
                    this.clear(first);
                    continue;
                }
                itemStack.setAmount(amount - toDelete);
                this.setItem(first, itemStack);
                toDelete = 0;
            } while (toDelete > 0);
        }
        return leftover;
    }

    private int getMaxItemStack() {
        return this.getInventory().z_();
    }

    @Override
    public void remove(int materialId) {
        ItemStack[] items = this.getStorageContents();
        for (int i2 = 0; i2 < items.length; ++i2) {
            if (items[i2] == null || items[i2].getTypeId() != materialId) continue;
            this.clear(i2);
        }
    }

    @Override
    public void remove(Material material) {
        Validate.notNull((Object)((Object)material), (String)"Material cannot be null", (Object[])new Object[0]);
        this.remove(material.getId());
    }

    @Override
    public void remove(ItemStack item) {
        ItemStack[] items = this.getStorageContents();
        for (int i2 = 0; i2 < items.length; ++i2) {
            if (items[i2] == null || !items[i2].equals(item)) continue;
            this.clear(i2);
        }
    }

    @Override
    public void clear(int index) {
        this.setItem(index, null);
    }

    @Override
    public void clear() {
        for (int i2 = 0; i2 < this.getSize(); ++i2) {
            this.clear(i2);
        }
    }

    @Override
    public ListIterator<ItemStack> iterator() {
        return new InventoryIterator(this);
    }

    @Override
    public ListIterator<ItemStack> iterator(int index) {
        if (index < 0) {
            index += this.getSize() + 1;
        }
        return new InventoryIterator(this, index);
    }

    @Override
    public List<HumanEntity> getViewers() {
        try {
            return this.inventory.getViewers();
        }
        catch (AbstractMethodError e2) {
            return new ArrayList<HumanEntity>();
        }
    }

    @Override
    public String getTitle() {
        String name = this.inventory.h_();
        return name != null ? name : "MODInv_" + this.inventory.getClass().getSimpleName();
    }

    @Override
    public InventoryType getType() {
        if (this.inventory instanceof afy) {
            return this.inventory.w_() >= 9 ? InventoryType.WORKBENCH : InventoryType.CRAFTING;
        }
        if (this.inventory instanceof aec) {
            return InventoryType.PLAYER;
        }
        if (this.inventory instanceof avq) {
            return InventoryType.DROPPER;
        }
        if (this.inventory instanceof avp) {
            return InventoryType.DISPENSER;
        }
        if (this.inventory instanceof avu) {
            return InventoryType.FURNACE;
        }
        if (this instanceof CraftInventoryEnchanting) {
            return InventoryType.ENCHANTING;
        }
        if (this.inventory instanceof avk) {
            return InventoryType.BREWING;
        }
        if (this.inventory instanceof CraftInventoryCustom.MinecraftInventory) {
            return ((CraftInventoryCustom.MinecraftInventory)this.inventory).getType();
        }
        if (this.inventory instanceof agm) {
            return InventoryType.ENDER_CHEST;
        }
        if (this.inventory instanceof agj) {
            return InventoryType.MERCHANT;
        }
        if (this.inventory instanceof avh) {
            return InventoryType.BEACON;
        }
        if (this instanceof CraftInventoryAnvil) {
            return InventoryType.ANVIL;
        }
        if (this.inventory instanceof avv) {
            return InventoryType.HOPPER;
        }
        if (this.inventory instanceof awb) {
            return InventoryType.SHULKER_BOX;
        }
        return InventoryType.CHEST;
    }

    @Override
    public InventoryHolder getHolder() {
        try {
            return this.inventory.getOwner();
        }
        catch (AbstractMethodError e2) {
            if (this.inventory instanceof avj) {
                avj tileentity = (avj)((Object)this.inventory);
                BlockState state = tileentity.D().getWorld().getBlockAt(tileentity.w().p(), tileentity.w().q(), tileentity.w().r()).getState();
                return state instanceof InventoryHolder ? (InventoryHolder)((Object)state) : null;
            }
            return null;
        }
    }

    @Override
    public int getMaxStackSize() {
        return this.inventory.z_();
    }

    @Override
    public void setMaxStackSize(int size) {
        this.inventory.setMaxStackSize(size);
    }

    public int hashCode() {
        return this.inventory.hashCode();
    }

    public boolean equals(Object obj) {
        return obj instanceof CraftInventory && ((CraftInventory)obj).inventory.equals(this.inventory);
    }

    @Override
    public Location getLocation() {
        return this.inventory.getLocation();
    }
}

