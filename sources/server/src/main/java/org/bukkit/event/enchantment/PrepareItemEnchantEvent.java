/*
 * Akarin Forge
 */
package org.bukkit.event.enchantment;

import org.bukkit.block.Block;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class PrepareItemEnchantEvent
extends InventoryEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Block table;
    private final ItemStack item;
    private final EnchantmentOffer[] offers;
    private final int bonus;
    private boolean cancelled;
    private final Player enchanter;

    public PrepareItemEnchantEvent(Player enchanter, InventoryView view, Block table, ItemStack item, EnchantmentOffer[] offers, int bonus) {
        super(view);
        this.enchanter = enchanter;
        this.table = table;
        this.item = item;
        this.offers = offers;
        this.bonus = bonus;
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

    public int[] getExpLevelCostsOffered() {
        int[] levelOffers = new int[this.offers.length];
        for (int i2 = 0; i2 < this.offers.length; ++i2) {
            levelOffers[i2] = this.offers[i2] != null ? this.offers[i2].getCost() : 0;
        }
        return levelOffers;
    }

    public EnchantmentOffer[] getOffers() {
        return this.offers;
    }

    public int getEnchantmentBonus() {
        return this.bonus;
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

