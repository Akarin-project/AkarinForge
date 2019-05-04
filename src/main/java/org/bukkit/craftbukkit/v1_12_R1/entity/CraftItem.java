/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class CraftItem
extends CraftEntity
implements Item {
    private final acl item;

    public CraftItem(CraftServer server, vg entity, acl item) {
        super(server, entity);
        this.item = item;
    }

    public CraftItem(CraftServer server, acl entity) {
        this(server, entity, entity);
    }

    @Override
    public ItemStack getItemStack() {
        return CraftItemStack.asCraftMirror(this.item.k());
    }

    @Override
    public void setItemStack(ItemStack stack) {
        this.item.a(CraftItemStack.asNMSCopy(stack));
    }

    @Override
    public int getPickupDelay() {
        return this.item.e;
    }

    @Override
    public void setPickupDelay(int delay) {
        this.item.e = Math.min(delay, 32767);
    }

    @Override
    public String toString() {
        return "CraftItem";
    }

    @Override
    public EntityType getType() {
        return EntityType.DROPPED_ITEM;
    }
}

