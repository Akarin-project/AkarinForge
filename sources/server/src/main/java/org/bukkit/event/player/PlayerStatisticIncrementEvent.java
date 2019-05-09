/*
 * Akarin Forge
 */
package org.bukkit.event.player;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerStatisticIncrementEvent
extends PlayerEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    protected final Statistic statistic;
    private final int initialValue;
    private final int newValue;
    private boolean isCancelled = false;
    private final EntityType entityType;
    private final Material material;

    public PlayerStatisticIncrementEvent(Player player, Statistic statistic, int initialValue, int newValue) {
        super(player);
        this.statistic = statistic;
        this.initialValue = initialValue;
        this.newValue = newValue;
        this.entityType = null;
        this.material = null;
    }

    public PlayerStatisticIncrementEvent(Player player, Statistic statistic, int initialValue, int newValue, EntityType entityType) {
        super(player);
        this.statistic = statistic;
        this.initialValue = initialValue;
        this.newValue = newValue;
        this.entityType = entityType;
        this.material = null;
    }

    public PlayerStatisticIncrementEvent(Player player, Statistic statistic, int initialValue, int newValue, Material material) {
        super(player);
        this.statistic = statistic;
        this.initialValue = initialValue;
        this.newValue = newValue;
        this.entityType = null;
        this.material = material;
    }

    public Statistic getStatistic() {
        return this.statistic;
    }

    public int getPreviousValue() {
        return this.initialValue;
    }

    public int getNewValue() {
        return this.newValue;
    }

    public EntityType getEntityType() {
        return this.entityType;
    }

    public Material getMaterial() {
        return this.material;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

