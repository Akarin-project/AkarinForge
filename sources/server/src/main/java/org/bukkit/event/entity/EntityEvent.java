/*
 * Akarin Forge
 */
package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;

public abstract class EntityEvent
extends Event {
    protected Entity entity;

    public EntityEvent(Entity what) {
        this.entity = what;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public EntityType getEntityType() {
        return this.entity.getType();
    }
}

