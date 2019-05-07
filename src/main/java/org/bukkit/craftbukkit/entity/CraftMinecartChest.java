package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.item.EntityMinecartChest;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.inventory.Inventory;

import com.destroystokyo.paper.loottable.CraftLootableEntityInventory;

@SuppressWarnings("deprecation")
public class CraftMinecartChest extends CraftMinecart implements StorageMinecart, CraftLootableEntityInventory { // Paper
    private final CraftInventory inventory;

    public CraftMinecartChest(CraftServer server, EntityMinecartChest entity) {
        super(server, entity);
        inventory = new CraftInventory(entity);
    }

    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public String toString() {
        return "CraftMinecartChest{" + "inventory=" + inventory + '}';
    }

    public EntityType getType() {
        return EntityType.MINECART_CHEST;
    }
}
