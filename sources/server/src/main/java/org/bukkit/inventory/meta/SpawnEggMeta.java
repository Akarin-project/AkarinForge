/*
 * Akarin Forge
 */
package org.bukkit.inventory.meta;

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.meta.ItemMeta;

public interface SpawnEggMeta
extends ItemMeta {
    public EntityType getSpawnedType();

    public void setSpawnedType(EntityType var1);

    @Override
    public SpawnEggMeta clone();
}

