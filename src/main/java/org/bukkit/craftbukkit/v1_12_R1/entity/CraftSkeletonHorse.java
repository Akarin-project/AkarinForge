/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftAbstractHorse;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.SkeletonHorse;

public class CraftSkeletonHorse
extends CraftAbstractHorse
implements SkeletonHorse {
    public CraftSkeletonHorse(CraftServer server, aau entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftSkeletonHorse";
    }

    @Override
    public EntityType getType() {
        return EntityType.SKELETON_HORSE;
    }

    @Override
    public Horse.Variant getVariant() {
        return Horse.Variant.SKELETON_HORSE;
    }
}

