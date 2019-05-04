/*
 * Akarin Forge
 */
package org.bukkit.event.entity;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class EntityBreakDoorEvent
extends EntityChangeBlockEvent {
    public EntityBreakDoorEvent(LivingEntity entity, Block targetBlock) {
        super(entity, targetBlock, Material.AIR, 0);
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity)this.entity;
    }
}

