/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.inventory;

import java.util.List;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.PlayerInventory;

public class CraftContainer
extends afr {
    private final InventoryView view;
    private InventoryType cachedType;
    private String cachedTitle;
    private afr delegate;
    private final int cachedSize;

    public CraftContainer(InventoryView view, aed player, int id2) {
        this.view = view;
        this.d = id2;
        tv top = ((CraftInventory)view.getTopInventory()).getInventory();
        aec bottom = (aec)((CraftInventory)view.getBottomInventory()).getInventory();
        this.cachedType = view.getType();
        this.cachedTitle = view.getTitle();
        this.cachedSize = this.getSize();
        this.setupSlots(top, bottom, player);
    }

    public CraftContainer(Inventory inventory, final aed player, int id2) {
        this(new InventoryView(){

            @Override
            public Inventory getTopInventory() {
                return Inventory.this;
            }

            @Override
            public Inventory getBottomInventory() {
                return this.getPlayer().getInventory();
            }

            @Override
            public HumanEntity getPlayer() {
                return player.getBukkitEntity();
            }

            @Override
            public InventoryType getType() {
                return Inventory.this.getType();
            }
        }, player, id2);
    }

    @Override
    public InventoryView getBukkitView() {
        return this.view;
    }

    private int getSize() {
        return this.view.getTopInventory().getSize();
    }

    @Override
    public boolean c(aed entityhuman) {
        if (this.cachedType == this.view.getType() && this.cachedSize == this.getSize() && this.cachedTitle.equals(this.view.getTitle())) {
            return true;
        }
        boolean typeChanged = this.cachedType != this.view.getType();
        this.cachedType = this.view.getType();
        this.cachedTitle = this.view.getTitle();
        if (this.view.getPlayer() instanceof CraftPlayer) {
            CraftPlayer player = (CraftPlayer)this.view.getPlayer();
            String type = CraftContainer.getNotchInventoryType(this.cachedType);
            tv top = ((CraftInventory)this.view.getTopInventory()).getInventory();
            aec bottom = (aec)((CraftInventory)this.view.getBottomInventory()).getInventory();
            this.b.clear();
            this.c.clear();
            if (typeChanged) {
                this.setupSlots(top, bottom, player.getHandle());
            }
            int size = this.getSize();
            player.getHandle().a.a(new ir(this.d, type, new ho(this.cachedTitle), size));
            player.updateInventory();
        }
        return true;
    }

    public static String getNotchInventoryType(InventoryType type) {
        switch (type) {
            case WORKBENCH: {
                return "minecraft:crafting_table";
            }
            case FURNACE: {
                return "minecraft:furnace";
            }
            case DISPENSER: {
                return "minecraft:dispenser";
            }
            case ENCHANTING: {
                return "minecraft:enchanting_table";
            }
            case BREWING: {
                return "minecraft:brewing_stand";
            }
            case BEACON: {
                return "minecraft:beacon";
            }
            case ANVIL: {
                return "minecraft:anvil";
            }
            case HOPPER: {
                return "minecraft:hopper";
            }
            case DROPPER: {
                return "minecraft:dropper";
            }
            case SHULKER_BOX: {
                return "minecraft:shulker_box";
            }
        }
        return "minecraft:chest";
    }

    private void setupSlots(tv top, aec bottom, aed entityhuman) {
        switch (this.cachedType) {
            case CREATIVE: {
                break;
            }
            case PLAYER: 
            case CHEST: {
                this.delegate = new afv(bottom, top, entityhuman);
                break;
            }
            case DISPENSER: 
            case DROPPER: {
                this.delegate = new aga(bottom, top);
                break;
            }
            case FURNACE: {
                this.delegate = new agd(bottom, top);
                break;
            }
            case WORKBENCH: 
            case CRAFTING: {
                this.setupWorkbench(top, bottom);
                break;
            }
            case ENCHANTING: {
                this.delegate = new agb(bottom, entityhuman.l, entityhuman.c());
                break;
            }
            case BREWING: {
                this.delegate = new afu(bottom, top);
                break;
            }
            case HOPPER: {
                this.delegate = new agf(bottom, top, entityhuman);
                break;
            }
            case ANVIL: {
                this.delegate = new afs(bottom, entityhuman.l, entityhuman.c(), entityhuman);
                break;
            }
            case BEACON: {
                this.delegate = new aft(bottom, top);
                break;
            }
            case SHULKER_BOX: {
                this.delegate = new agp(bottom, top, entityhuman);
            }
        }
        if (this.delegate != null) {
            this.b = this.delegate.b;
            this.c = this.delegate.c;
        }
    }

    private void setupWorkbench(tv top, tv bottom) {
        int row;
        int col;
        this.a(new agr(top, 0, 124, 35));
        for (row = 0; row < 3; ++row) {
            for (col = 0; col < 3; ++col) {
                this.a(new agr(top, 1 + col + row * 3, 30 + col * 18, 17 + row * 18));
            }
        }
        for (row = 0; row < 3; ++row) {
            for (col = 0; col < 9; ++col) {
                this.a(new agr(bottom, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        for (col = 0; col < 9; ++col) {
            this.a(new agr(bottom, col, 8 + col * 18, 142));
        }
    }

    @Override
    public aip b(aed entityhuman, int i2) {
        return this.delegate != null ? this.delegate.b(entityhuman, i2) : super.b(entityhuman, i2);
    }

    @Override
    public boolean a(aed entity) {
        return true;
    }

}

