/*
 * Akarin Forge
 */
package org.bukkit.event.player;

import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerItemMendEvent
extends PlayerEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final ItemStack item;
    private final ExperienceOrb experienceOrb;
    private int repairAmount;
    private boolean cancelled;

    public PlayerItemMendEvent(Player who, ItemStack item, ExperienceOrb experienceOrb, int repairAmount) {
        super(who);
        this.item = item;
        this.experienceOrb = experienceOrb;
        this.repairAmount = repairAmount;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public ExperienceOrb getExperienceOrb() {
        return this.experienceOrb;
    }

    public int getRepairAmount() {
        return this.repairAmount;
    }

    public void setRepairAmount(int amount) {
        this.repairAmount = amount;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

