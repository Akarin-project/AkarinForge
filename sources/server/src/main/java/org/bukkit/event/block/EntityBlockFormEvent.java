/*
 * Akarin Forge
 */
package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.event.block.BlockFormEvent;

public class EntityBlockFormEvent
extends BlockFormEvent {
    private final Entity entity;

    public EntityBlockFormEvent(Entity entity, Block block, BlockState blockstate) {
        super(block, blockstate);
        this.entity = entity;
    }

    public Entity getEntity() {
        return this.entity;
    }
}

