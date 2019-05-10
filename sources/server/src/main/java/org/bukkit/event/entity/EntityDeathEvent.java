/*
 * Akarin Forge
 */
package org.bukkit.event.entity;

import java.util.List;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.inventory.ItemStack;

public class EntityDeathEvent
extends EntityEvent {
    private static final HandlerList handlers = new HandlerList();
    private final List<ItemStack> drops;
    private int dropExp = 0;

    public EntityDeathEvent(LivingEntity entity, List<ItemStack> drops) {
        this(entity, drops, 0);
    }

    public EntityDeathEvent(LivingEntity what, List<ItemStack> drops, int droppedExp) {
        super(what);
        this.drops = drops;
        this.dropExp = droppedExp;
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity)this.entity;
    }

    public int getDroppedExp() {
        return this.dropExp;
    }

    public void setDroppedExp(int exp) {
        this.dropExp = exp;
    }

    public List<ItemStack> getDrops() {
        return this.drops;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

