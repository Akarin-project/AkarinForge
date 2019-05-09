/*
 * Akarin Forge
 */
package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityTargetEvent;

public class EntityTargetLivingEntityEvent
extends EntityTargetEvent {
    public EntityTargetLivingEntityEvent(Entity entity, LivingEntity target, EntityTargetEvent.TargetReason reason) {
        super(entity, target, reason);
    }

    @Override
    public LivingEntity getTarget() {
        return (LivingEntity)super.getTarget();
    }

    @Override
    public void setTarget(Entity target) {
        if (target == null || target instanceof LivingEntity) {
            super.setTarget(target);
        }
    }
}

