/*
 * Akarin Forge
 */
package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.inventory.MerchantRecipe;

public class VillagerReplenishTradeEvent
extends EntityEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private MerchantRecipe recipe;
    private int bonus;

    public VillagerReplenishTradeEvent(Villager what, MerchantRecipe recipe, int bonus) {
        super(what);
        this.recipe = recipe;
        this.bonus = bonus;
    }

    public MerchantRecipe getRecipe() {
        return this.recipe;
    }

    public void setRecipe(MerchantRecipe recipe) {
        this.recipe = recipe;
    }

    public int getBonus() {
        return this.bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
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
    public Villager getEntity() {
        return (Villager)super.getEntity();
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

