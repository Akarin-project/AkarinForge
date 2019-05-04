/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.entity.LivingEntity;

public interface Slime
extends LivingEntity {
    public int getSize();

    public void setSize(int var1);

    public void setTarget(LivingEntity var1);

    public LivingEntity getTarget();
}

