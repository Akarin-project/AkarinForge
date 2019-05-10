/*
 * Akarin Forge
 */
package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntitySpawnEvent;

public class CreatureSpawnEvent
extends EntitySpawnEvent {
    private final SpawnReason spawnReason;

    public CreatureSpawnEvent(LivingEntity spawnee, SpawnReason spawnReason) {
        super(spawnee);
        this.spawnReason = spawnReason;
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity)this.entity;
    }

    public SpawnReason getSpawnReason() {
        return this.spawnReason;
    }

    public static enum SpawnReason {
        NATURAL,
        JOCKEY,
        CHUNK_GEN,
        SPAWNER,
        EGG,
        SPAWNER_EGG,
        LIGHTNING,
        BUILD_SNOWMAN,
        BUILD_IRONGOLEM,
        BUILD_WITHER,
        VILLAGE_DEFENSE,
        VILLAGE_INVASION,
        BREEDING,
        SLIME_SPLIT,
        REINFORCEMENTS,
        NETHER_PORTAL,
        DISPENSE_EGG,
        INFECTION,
        CURED,
        OCELOT_BABY,
        SILVERFISH_BLOCK,
        MOUNT,
        TRAP,
        ENDER_PEARL,
        SHOULDER_ENTITY,
        CUSTOM,
        DEFAULT;
        

        private SpawnReason() {
        }
    }

}

