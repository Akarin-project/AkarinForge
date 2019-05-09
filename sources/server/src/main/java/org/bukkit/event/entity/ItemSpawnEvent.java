/*
 * Akarin Forge
 */
package org.bukkit.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.entity.EntitySpawnEvent;

public class ItemSpawnEvent
extends EntitySpawnEvent {
    public ItemSpawnEvent(Item spawnee) {
        super(spawnee);
    }

    @Deprecated
    public ItemSpawnEvent(Item spawnee, Location loc) {
        this(spawnee);
    }

    @Override
    public Item getEntity() {
        return (Item)this.entity;
    }
}

