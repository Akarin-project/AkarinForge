/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.entity.LivingEntity;

public interface Creature
extends LivingEntity {
    public void setTarget(LivingEntity var1);

    public LivingEntity getTarget();
}

