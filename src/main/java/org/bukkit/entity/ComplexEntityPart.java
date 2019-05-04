/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.entity.ComplexLivingEntity;
import org.bukkit.entity.Entity;

public interface ComplexEntityPart
extends Entity {
    public ComplexLivingEntity getParent();
}

