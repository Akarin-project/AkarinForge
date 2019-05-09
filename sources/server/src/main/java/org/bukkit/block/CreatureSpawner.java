/*
 * Akarin Forge
 */
package org.bukkit.block;

import org.bukkit.block.BlockState;
import org.bukkit.entity.EntityType;

public interface CreatureSpawner
extends BlockState {
    public EntityType getSpawnedType();

    public void setSpawnedType(EntityType var1);

    @Deprecated
    public void setCreatureTypeByName(String var1);

    @Deprecated
    public String getCreatureTypeName();

    public int getDelay();

    public void setDelay(int var1);

    public int getMinSpawnDelay();

    public void setMinSpawnDelay(int var1);

    public int getMaxSpawnDelay();

    public void setMaxSpawnDelay(int var1);

    public int getSpawnCount();

    public void setSpawnCount(int var1);

    public int getMaxNearbyEntities();

    public void setMaxNearbyEntities(int var1);

    public int getRequiredPlayerRange();

    public void setRequiredPlayerRange(int var1);

    public int getSpawnRange();

    public void setSpawnRange(int var1);
}

