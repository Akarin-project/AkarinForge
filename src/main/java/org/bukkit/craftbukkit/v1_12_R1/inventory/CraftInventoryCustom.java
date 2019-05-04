/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.Validate
 */
package org.bukkit.craftbukkit.v1_12_R1.inventory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.Validate;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;

public class CraftInventoryCustom
extends CraftInventory {
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

    public CraftInventoryCustom(InventoryHolder owner, fi<aip> items) {
        super(new MinecraftInventory(owner, items));
    }

    static class MinecraftInventory
    implements tv {
        private final fi<aip> items;
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
            Validate.notNull((Object)title, (String)"Title cannot be null", (Object[])new Object[0]);
            this.items = fi.a(size, aip.a);
            this.title = title;
            this.viewers = new ArrayList<HumanEntity>();
            this.owner = owner;
            this.type = InventoryType.CHEST;
        }

        public MinecraftInventory(InventoryHolder owner, fi<aip> items) {
            this.items = items;
            this.title = "Chest";
            this.viewers = new ArrayList<HumanEntity>();
            this.owner = owner;
            this.type = InventoryType.CHEST;
        }

        @Override
        public int w_() {
            return this.items.size();
        }

        @Override
        public aip a(int i2) {
            return this.items.get(i2);
        }

        @Override
        public aip a(int i2, int j2) {
            aip result;
            aip stack = this.a(i2);
            if (stack == aip.a) {
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
            if (stack == aip.a) {
                return stack;
            }
            if (stack.E() <= 1) {
                this.a(i2, null);
                result = stack;
            } else {
                result = CraftItemStack.copyNMSStack(stack, 1);
                stack.g(1);
            }
            return result;
        }

        @Override
        public void a(int i2, aip itemstack) {
            this.items.set(i2, itemstack);
            if (itemstack != aip.a && this.z_() > 0 && itemstack.E() > this.z_()) {
                itemstack.e(this.z_());
            }
        }

        @Override
        public int z_() {
            return this.maxStack;
        }

        @Override
        public void setMaxStackSize(int size) {
            this.maxStack = size;
        }

        @Override
        public void y_() {
        }

        @Override
        public boolean a(aed entityhuman) {
            return true;
        }

        @Override
        public List<aip> getContents() {
            return this.items;
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

        public InventoryType getType() {
            return this.type;
        }

        @Override
        public InventoryHolder getOwner() {
            return this.owner;
        }

        @Override
        public boolean b(int i2, aip itemstack) {
            return true;
        }

        @Override
        public void b(aed entityHuman) {
        }

        @Override
        public void c(aed entityHuman) {
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
            this.items.clear();
        }

        @Override
        public String h_() {
            return this.title;
        }

        @Override
        public boolean n_() {
            return this.title != null;
        }

        @Override
        public hh i_() {
            return new ho(this.title);
        }

        @Override
        public Location getLocation() {
            return null;
        }

        @Override
        public boolean x_() {
            aip itemstack;
            Iterator<aip> iterator = this.items.iterator();
            do {
                if (iterator.hasNext()) continue;
                return true;
            } while ((itemstack = iterator.next()).b());
            return false;
        }
    }

}

