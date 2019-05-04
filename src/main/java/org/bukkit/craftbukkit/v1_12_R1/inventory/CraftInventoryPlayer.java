/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  org.apache.commons.lang3.Validate
 */
package org.bukkit.craftbukkit.v1_12_R1.inventory;

import com.google.common.base.Preconditions;
import java.util.List;
import org.apache.commons.lang3.Validate;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.material.MaterialData;

public class CraftInventoryPlayer
extends CraftInventory
implements PlayerInventory,
EntityEquipment {
    public CraftInventoryPlayer(aec inventory) {
        super(inventory);
    }

    @Override
    public aec getInventory() {
        return (aec)this.inventory;
    }

    @Override
    public ItemStack[] getStorageContents() {
        return this.asCraftMirror(this.getInventory().a);
    }

    @Override
    public ItemStack getItemInMainHand() {
        return CraftItemStack.asCraftMirror(this.getInventory().i());
    }

    @Override
    public void setItemInMainHand(ItemStack item) {
        this.setItem(this.getHeldItemSlot(), item);
    }

    @Override
    public ItemStack getItemInOffHand() {
        return CraftItemStack.asCraftMirror(this.getInventory().c.get(0));
    }

    @Override
    public void setItemInOffHand(ItemStack item) {
        ItemStack[] extra = this.getExtraContents();
        extra[0] = item;
        this.setExtraContents(extra);
    }

    @Override
    public ItemStack getItemInHand() {
        return this.getItemInMainHand();
    }

    @Override
    public void setItemInHand(ItemStack stack) {
        this.setItemInMainHand(stack);
    }

    @Override
    public void setItem(int index, ItemStack item) {
        super.setItem(index, item);
        if (this.getHolder() == null) {
            return;
        }
        oq player = ((CraftPlayer)this.getHolder()).getHandle();
        if (player.a == null) {
            return;
        }
        if (index < aec.j()) {
            index += 36;
        } else if (index > 39) {
            index += 5;
        } else if (index > 35) {
            index = 8 - (index - 36);
        }
        player.a.a(new iu(player.bx.d, index, CraftItemStack.asNMSCopy(item)));
    }

    @Override
    public int getHeldItemSlot() {
        return this.getInventory().d;
    }

    @Override
    public void setHeldItemSlot(int slot) {
        Validate.isTrue((boolean)(slot >= 0 && slot < aec.j()), (String)"Slot is not between 0 and 8 inclusive", (Object[])new Object[0]);
        this.getInventory().d = slot;
        ((CraftPlayer)this.getHolder()).getHandle().a.a(new kb(slot));
    }

    @Override
    public ItemStack getHelmet() {
        return this.getItem(this.getSize() - 2);
    }

    @Override
    public ItemStack getChestplate() {
        return this.getItem(this.getSize() - 3);
    }

    @Override
    public ItemStack getLeggings() {
        return this.getItem(this.getSize() - 4);
    }

    @Override
    public ItemStack getBoots() {
        return this.getItem(this.getSize() - 5);
    }

    @Override
    public void setHelmet(ItemStack helmet) {
        this.setItem(this.getSize() - 2, helmet);
    }

    @Override
    public void setChestplate(ItemStack chestplate) {
        this.setItem(this.getSize() - 3, chestplate);
    }

    @Override
    public void setLeggings(ItemStack leggings) {
        this.setItem(this.getSize() - 4, leggings);
    }

    @Override
    public void setBoots(ItemStack boots) {
        this.setItem(this.getSize() - 5, boots);
    }

    @Override
    public ItemStack[] getArmorContents() {
        return this.asCraftMirror(this.getInventory().b);
    }

    private void setSlots(ItemStack[] items, int baseSlot, int length) {
        if (items == null) {
            items = new ItemStack[length];
        }
        Preconditions.checkArgument((boolean)(items.length <= length), (String)"items.length must be < %s", (int)length);
        for (int i2 = 0; i2 < length; ++i2) {
            if (i2 >= items.length) {
                this.setItem(baseSlot + i2, null);
                continue;
            }
            this.setItem(baseSlot + i2, items[i2]);
        }
    }

    @Override
    public void setStorageContents(ItemStack[] items) throws IllegalArgumentException {
        this.setSlots(items, 0, this.getInventory().a.size());
    }

    @Override
    public void setArmorContents(ItemStack[] items) {
        this.setSlots(items, this.getInventory().a.size(), this.getInventory().b.size());
    }

    @Override
    public ItemStack[] getExtraContents() {
        return this.asCraftMirror(this.getInventory().c);
    }

    @Override
    public void setExtraContents(ItemStack[] items) {
        this.setSlots(items, this.getInventory().a.size() + this.getInventory().b.size(), this.getInventory().c.size());
    }

    @Override
    public int clear(int id2, int data) {
        int count = 0;
        ItemStack[] items = this.getContents();
        for (int i2 = 0; i2 < items.length; ++i2) {
            ItemStack item = items[i2];
            if (item == null || id2 > -1 && item.getTypeId() != id2 || data > -1 && item.getData().getData() != data) continue;
            count += item.getAmount();
            this.setItem(i2, null);
        }
        return count;
    }

    @Override
    public HumanEntity getHolder() {
        return (HumanEntity)this.inventory.getOwner();
    }

    @Override
    public float getItemInHandDropChance() {
        return this.getItemInMainHandDropChance();
    }

    @Override
    public void setItemInHandDropChance(float chance) {
        this.setItemInMainHandDropChance(chance);
    }

    @Override
    public float getItemInMainHandDropChance() {
        return 1.0f;
    }

    @Override
    public void setItemInMainHandDropChance(float chance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getItemInOffHandDropChance() {
        return 1.0f;
    }

    @Override
    public void setItemInOffHandDropChance(float chance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getHelmetDropChance() {
        return 1.0f;
    }

    @Override
    public void setHelmetDropChance(float chance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getChestplateDropChance() {
        return 1.0f;
    }

    @Override
    public void setChestplateDropChance(float chance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getLeggingsDropChance() {
        return 1.0f;
    }

    @Override
    public void setLeggingsDropChance(float chance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getBootsDropChance() {
        return 1.0f;
    }

    @Override
    public void setBootsDropChance(float chance) {
        throw new UnsupportedOperationException();
    }
}

