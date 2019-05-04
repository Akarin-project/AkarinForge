/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.inventory;

import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class CraftInventoryView
extends InventoryView {
    private final afr container;
    private final CraftHumanEntity player;
    private final CraftInventory viewing;

    public CraftInventoryView(HumanEntity player, Inventory viewing, afr container) {
        this.player = (CraftHumanEntity)player;
        this.viewing = (CraftInventory)viewing;
        this.container = container;
    }

    @Override
    public Inventory getTopInventory() {
        return this.viewing;
    }

    @Override
    public Inventory getBottomInventory() {
        return this.player.getInventory();
    }

    @Override
    public HumanEntity getPlayer() {
        return this.player;
    }

    @Override
    public InventoryType getType() {
        InventoryType type = this.viewing.getType();
        if (type == InventoryType.CRAFTING && this.player.getGameMode() == GameMode.CREATIVE) {
            return InventoryType.CREATIVE;
        }
        return type;
    }

    @Override
    public void setItem(int slot, ItemStack item) {
        aip stack = CraftItemStack.asNMSCopy(item);
        if (slot != -999) {
            this.container.a(slot).d(stack);
        } else {
            this.player.getHandle().a(stack, false);
        }
    }

    @Override
    public ItemStack getItem(int slot) {
        if (slot == -999) {
            return null;
        }
        return CraftItemStack.asCraftMirror(this.container.a(slot).d());
    }

    public boolean isInTop(int rawSlot) {
        return rawSlot < this.viewing.getSize();
    }

    public afr getHandle() {
        return this.container;
    }

    public static InventoryType.SlotType getSlotType(InventoryView inventory, int slot) {
        InventoryType.SlotType type = InventoryType.SlotType.CONTAINER;
        if (slot >= 0 && slot < inventory.getTopInventory().getSize()) {
            switch (inventory.getType()) {
                case FURNACE: {
                    if (slot == 2) {
                        type = InventoryType.SlotType.RESULT;
                        break;
                    }
                    if (slot == 1) {
                        type = InventoryType.SlotType.FUEL;
                        break;
                    }
                    type = InventoryType.SlotType.CRAFTING;
                    break;
                }
                case BREWING: {
                    if (slot == 3) {
                        type = InventoryType.SlotType.FUEL;
                        break;
                    }
                    type = InventoryType.SlotType.CRAFTING;
                    break;
                }
                case ENCHANTING: {
                    type = InventoryType.SlotType.CRAFTING;
                    break;
                }
                case WORKBENCH: 
                case CRAFTING: {
                    if (slot == 0) {
                        type = InventoryType.SlotType.RESULT;
                        break;
                    }
                    type = InventoryType.SlotType.CRAFTING;
                    break;
                }
                case MERCHANT: {
                    if (slot == 2) {
                        type = InventoryType.SlotType.RESULT;
                        break;
                    }
                    type = InventoryType.SlotType.CRAFTING;
                    break;
                }
                case BEACON: {
                    type = InventoryType.SlotType.CRAFTING;
                    break;
                }
                case ANVIL: {
                    if (slot == 2) {
                        type = InventoryType.SlotType.RESULT;
                        break;
                    }
                    type = InventoryType.SlotType.CRAFTING;
                    break;
                }
                default: {
                    break;
                }
            }
        } else if (slot == -999 || slot == -1) {
            type = InventoryType.SlotType.OUTSIDE;
        } else if (inventory.getType() == InventoryType.CRAFTING) {
            if (slot < 9) {
                type = InventoryType.SlotType.ARMOR;
            } else if (slot > 35) {
                type = InventoryType.SlotType.QUICKBAR;
            }
        } else if (slot >= inventory.countSlots() - 14) {
            type = InventoryType.SlotType.QUICKBAR;
        }
        return type;
    }

}

