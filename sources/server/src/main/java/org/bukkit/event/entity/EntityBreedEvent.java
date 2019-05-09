/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.Validate
 */
package org.bukkit.event.entity;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.inventory.ItemStack;

public class EntityBreedEvent
extends EntityEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final LivingEntity mother;
    private final LivingEntity father;
    private final LivingEntity breeder;
    private final ItemStack bredWith;
    private int experience;
    private boolean cancel;

    public EntityBreedEvent(LivingEntity child, LivingEntity mother, LivingEntity father, LivingEntity breeder, ItemStack bredWith, int experience) {
        super(child);
        Validate.notNull((Object)child, (String)"Cannot have null child");
        Validate.notNull((Object)mother, (String)"Cannot have null mother");
        Validate.notNull((Object)father, (String)"Cannot have null father");
        this.mother = mother;
        this.father = father;
        this.breeder = breeder;
        this.bredWith = bredWith;
        this.setExperience(experience);
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity)this.entity;
    }

    public LivingEntity getMother() {
        return this.mother;
    }

    public LivingEntity getFather() {
        return this.father;
    }

    public LivingEntity getBreeder() {
        return this.breeder;
    }

    public ItemStack getBredWith() {
        return this.bredWith;
    }

    public int getExperience() {
        return this.experience;
    }

    public void setExperience(int experience) {
        Validate.isTrue((boolean)(experience >= 0), (String)"Experience cannot be negative");
        this.experience = experience;
    }

    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

