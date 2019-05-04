/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 */
package org.bukkit.craftbukkit.v1_12_R1.block;

import com.google.common.base.Preconditions;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBlockEntityState;
import org.bukkit.entity.EntityType;

public class CraftCreatureSpawner
extends CraftBlockEntityState<avy>
implements CreatureSpawner {
    public CraftCreatureSpawner(Block block) {
        super(block, avy.class);
    }

    public CraftCreatureSpawner(Material material, avy te2) {
        super(material, te2);
    }

    @Override
    public EntityType getSpawnedType() {
        nf key = ((avy)this.getSnapshot()).a().g();
        return key == null ? EntityType.PIG : EntityType.fromName(key.a());
    }

    @Override
    public void setSpawnedType(EntityType entityType) {
        if (entityType == null || entityType.getName() == null) {
            throw new IllegalArgumentException("Can't spawn EntityType " + (Object)((Object)entityType) + " from mobspawners!");
        }
        ((avy)this.getSnapshot()).a().a(new nf(entityType.getName()));
    }

    @Override
    public String getCreatureTypeName() {
        return ((avy)this.getSnapshot()).a().g().a();
    }

    @Override
    public void setCreatureTypeByName(String creatureType) {
        EntityType type = EntityType.fromName(creatureType);
        if (type == null) {
            return;
        }
        this.setSpawnedType(type);
    }

    @Override
    public int getDelay() {
        return ((avy)this.getSnapshot()).a().a;
    }

    @Override
    public void setDelay(int delay) {
        ((avy)this.getSnapshot()).a().a = delay;
    }

    @Override
    public int getMinSpawnDelay() {
        return ((avy)this.getSnapshot()).a().f;
    }

    @Override
    public void setMinSpawnDelay(int spawnDelay) {
        Preconditions.checkArgument((boolean)(spawnDelay <= this.getMaxSpawnDelay()), (Object)"Minimum Spawn Delay must be less than or equal to Maximum Spawn Delay");
        ((avy)this.getSnapshot()).a().f = spawnDelay;
    }

    @Override
    public int getMaxSpawnDelay() {
        return ((avy)this.getSnapshot()).a().g;
    }

    @Override
    public void setMaxSpawnDelay(int spawnDelay) {
        Preconditions.checkArgument((boolean)(spawnDelay > 0), (Object)"Maximum Spawn Delay must be greater than 0.");
        Preconditions.checkArgument((boolean)(spawnDelay >= this.getMinSpawnDelay()), (Object)"Maximum Spawn Delay must be greater than or equal to Minimum Spawn Delay");
        ((avy)this.getSnapshot()).a().g = spawnDelay;
    }

    @Override
    public int getMaxNearbyEntities() {
        return ((avy)this.getSnapshot()).a().j;
    }

    @Override
    public void setMaxNearbyEntities(int maxNearbyEntities) {
        ((avy)this.getSnapshot()).a().j = maxNearbyEntities;
    }

    @Override
    public int getSpawnCount() {
        return ((avy)this.getSnapshot()).a().h;
    }

    @Override
    public void setSpawnCount(int count) {
        ((avy)this.getSnapshot()).a().h = count;
    }

    @Override
    public int getRequiredPlayerRange() {
        return ((avy)this.getSnapshot()).a().k;
    }

    @Override
    public void setRequiredPlayerRange(int requiredPlayerRange) {
        ((avy)this.getSnapshot()).a().k = requiredPlayerRange;
    }

    @Override
    public int getSpawnRange() {
        return ((avy)this.getSnapshot()).a().l;
    }

    @Override
    public void setSpawnRange(int spawnRange) {
        ((avy)this.getSnapshot()).a().l = spawnRange;
    }
}

