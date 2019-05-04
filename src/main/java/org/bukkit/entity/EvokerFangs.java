/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public interface EvokerFangs
extends Entity {
    public LivingEntity getOwner();

    public void setOwner(LivingEntity var1);
}

