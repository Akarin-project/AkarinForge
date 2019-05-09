/*
 * Akarin Forge
 */
package org.bukkit.event.enchantment;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class EnchantItemEvent
extends InventoryEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Block table;
    private final ItemStack item;
    private int level;
    private boolean cancelled;
    private final Map<Enchantment, Integer> enchants;
    private final Player enchanter;
    private int button;

    public EnchantItemEvent(Player enchanter, InventoryView view, Block table, ItemStack item, int level, Map<Enchantment, Integer> enchants, int i2) {
        super(view);
        this.enchanter = enchanter;
        this.table = table;
        this.item = item;
        this.level = level;
        this.enchants = new HashMap<Enchantment, Integer>(enchants);
        this.cancelled = false;
        this.button = i2;
    }

    public Player getEnchanter() {
        return this.enchanter;
    }

    public Block getEnchantBlock() {
        return this.table;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public int getExpLevelCost() {
        return this.level;
    }

    public void setExpLevelCost(int level) {
        this.level = level;
    }

    public Map<Enchantment, Integer> getEnchantsToAdd() {
        return this.enchants;
    }

    public int whichButton() {
        return this.button;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

