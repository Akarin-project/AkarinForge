/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  com.google.common.base.Predicates
 *  com.google.common.collect.Iterables
 */
package org.bukkit.craftbukkit.v1_12_R1.inventory;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftChatMessage;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class InventoryWrapper
implements tv {
    private final Inventory inventory;
    private final List<HumanEntity> viewers = new ArrayList<HumanEntity>();

    public InventoryWrapper(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public int w_() {
        return this.inventory.getSize();
    }

    @Override
    public aip a(int i2) {
        return CraftItemStack.asNMSCopy(this.inventory.getItem(i2));
    }

    @Override
    public aip a(int i2, int j2) {
        aip result;
        aip stack = this.a(i2);
        if (stack.b()) {
            return stack;
        }
        if (stack.E() <= j2) {
            this.a(i2, aip.a);
            result = stack;
        } else {
            result = CraftItemStack.copyNMSStack(stack, j2);
            stack.g(j2);
        }
        this.y_();
        return result;
    }

    @Override
    public aip c_(int i2) {
        aip result;
        aip stack = this.a(i2);
        if (stack.b()) {
            return stack;
        }
        if (stack.E() <= 1) {
            this.a(i2, aip.a);
            result = stack;
        } else {
            result = CraftItemStack.copyNMSStack(stack, 1);
            stack.g(1);
        }
        return result;
    }

    @Override
    public void a(int i2, aip itemstack) {
        this.inventory.setItem(i2, CraftItemStack.asBukkitCopy(itemstack));
    }

    @Override
    public int z_() {
        return this.inventory.getMaxStackSize();
    }

    @Override
    public void y_() {
    }

    @Override
    public boolean a(aed entityhuman) {
        return true;
    }

    @Override
    public void b(aed entityhuman) {
    }

    @Override
    public void c(aed entityhuman) {
    }

    @Override
    public boolean b(int i2, aip itemstack) {
        return true;
    }

    @Override
    public int c(int i2) {
        return 0;
    }

    @Override
    public void b(int i2, int j2) {
    }

    @Override
    public int h() {
        return 0;
    }

    @Override
    public void m() {
        this.inventory.clear();
    }

    @Override
    public List<aip> getContents() {
        int size = this.w_();
        ArrayList<aip> items = new ArrayList<aip>(size);
        for (int i2 = 0; i2 < size; ++i2) {
            items.set(i2, this.a(i2));
        }
        return items;
    }

    @Override
    public InventoryHolder getOwner() {
        return this.inventory.getHolder();
    }

    @Override
    public void setMaxStackSize(int size) {
        this.inventory.setMaxStackSize(size);
    }

    @Override
    public String h_() {
        return this.inventory.getName();
    }

    @Override
    public boolean n_() {
        return this.h_() != null;
    }

    @Override
    public hh i_() {
        return CraftChatMessage.fromString(this.h_())[0];
    }

    @Override
    public Location getLocation() {
        return this.inventory.getLocation();
    }

    @Override
    public boolean x_() {
        return Iterables.any((Iterable)this.inventory, (Predicate)Predicates.notNull());
    }

    @Override
    public void onOpen(CraftHumanEntity who) {
        this.viewers.add(who);
    }

    @Override
    public void onClose(CraftHumanEntity who) {
        this.viewers.remove(who);
    }

    @Override
    public List<HumanEntity> getViewers() {
        return this.viewers;
    }
}

