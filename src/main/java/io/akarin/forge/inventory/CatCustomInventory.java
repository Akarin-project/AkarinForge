/*
 * Decompiled with CFR 0_119.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package io.akarin.forge.inventory;

import javax.annotation.Nullable;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.PlayerArmorInvWrapper;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryCustom;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryPlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class CatCustomInventory
implements InventoryHolder {
    private final tv inventory;
    private final CraftInventory container;

    public CatCustomInventory(tv inventory) {
        this.container = new CraftInventory(inventory);
        this.inventory = inventory;
    }

    public CatCustomInventory(ItemStackHandler handler) {
        this.container = new CraftInventoryCustom((InventoryHolder)this, handler.getStacksList());
        this.inventory = this.container.getInventory();
    }

    public CatCustomInventory(aec playerInventory) {
        this.container = new CraftInventoryPlayer(playerInventory);
        this.inventory = playerInventory;
    }

    @Override
    public Inventory getInventory() {
        return this.container;
    }

    @Nullable
    public static InventoryHolder getHolderFromForge(IItemHandler handler) {
        if (handler == null) {
            return null;
        }
        if (handler instanceof ItemStackHandler) {
            return new CatCustomInventory((ItemStackHandler)handler);
        }
        if (handler instanceof SlotItemHandler) {
            return new CatCustomInventory(((SlotItemHandler)handler).d);
        }
        if (handler instanceof InvWrapper) {
            return new CatCustomInventory(((InvWrapper)handler).getInv());
        }
        if (handler instanceof SidedInvWrapper) {
            return new CatCustomInventory((tv)ReflectionHelper.getPrivateValue(SidedInvWrapper.class, (SidedInvWrapper)handler, "inv"));
        }
        if (handler instanceof PlayerInvWrapper) {
            return new CatCustomInventory(CatCustomInventory.getPlayerInv((PlayerInvWrapper)handler));
        }
        return null;
    }

    @Nullable
    public static Inventory getInventoryFromForge(IItemHandler handler) {
        InventoryHolder holder = CatCustomInventory.getHolderFromForge(handler);
        return holder != null ? holder.getInventory() : null;
    }

    public static aec getPlayerInv(PlayerInvWrapper handler) {
        IItemHandlerModifiable[] itemHandlers;
        for (IItemHandlerModifiable itemHandler : itemHandlers = (IItemHandlerModifiable[])ReflectionHelper.getPrivateValue(CombinedInvWrapper.class, handler, "itemHandler")) {
            if (itemHandler instanceof PlayerMainInvWrapper) {
                return ((PlayerMainInvWrapper)itemHandler).getInventoryPlayer();
            }
            if (!(itemHandler instanceof PlayerArmorInvWrapper)) continue;
            return ((PlayerArmorInvWrapper)itemHandler).getInventoryPlayer();
        }
        return null;
    }
}

