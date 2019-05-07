package io.akarin.forge.inventory;

import javax.annotation.Nullable;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
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

import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryCustom;
import org.bukkit.craftbukkit.inventory.CraftInventoryPlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.PlayerInventory;

public class AkarinCustomInventory
implements InventoryHolder {
    private final IInventory inventory;
    private final CraftInventory container;

    public AkarinCustomInventory(IInventory inventory) {
        this.container = new CraftInventory(inventory);
        this.inventory = inventory;
    }

    public AkarinCustomInventory(ItemStackHandler handler) {
        this.container = new CraftInventoryCustom((InventoryHolder)this, handler.getStacksList());
        this.inventory = this.container.getInventory();
    }

    public AkarinCustomInventory(InventoryPlayer playerInventory) {
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
            return new AkarinCustomInventory((ItemStackHandler)handler);
        }
        if (handler instanceof SlotItemHandler) {
            return new AkarinCustomInventory(((SlotItemHandler)handler).d);
        }
        if (handler instanceof InvWrapper) {
            return new AkarinCustomInventory(((InvWrapper)handler).getInv());
        }
        if (handler instanceof SidedInvWrapper) {
            return new AkarinCustomInventory((IInventory)ReflectionHelper.getPrivateValue(SidedInvWrapper.class, (SidedInvWrapper)handler, "inv"));
        }
        if (handler instanceof PlayerInvWrapper) {
            return new AkarinCustomInventory(AkarinCustomInventory.getPlayerInv((PlayerInvWrapper)handler));
        }
        return null;
    }

    @Nullable
    public static Inventory getInventoryFromForge(IItemHandler handler) {
        InventoryHolder holder = AkarinCustomInventory.getHolderFromForge(handler);
        return holder != null ? holder.getInventory() : null;
    }

    public static InventoryPlayer getPlayerInv(PlayerInvWrapper handler) {
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

