/*
 * Akarin Forge
 */
package org.bukkit.event.entity;

import java.util.List;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

public class AreaEffectCloudApplyEvent
extends EntityEvent {
    private static final HandlerList handlers = new HandlerList();
    private final List<LivingEntity> affectedEntities;

    public AreaEffectCloudApplyEvent(AreaEffectCloud entity, List<LivingEntity> affectedEntities) {
        super(entity);
        this.affectedEntities = affectedEntities;
    }

    @Override
    public AreaEffectCloud getEntity() {
        return (AreaEffectCloud)this.entity;
    }

    public List<LivingEntity> getAffectedEntities() {
        return this.affectedEntities;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

