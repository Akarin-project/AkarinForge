/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.entity.ComplexEntityPart;
import org.bukkit.entity.ComplexLivingEntity;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EnderDragon;

public interface EnderDragonPart
extends ComplexEntityPart,
Damageable {
    @Override
    public EnderDragon getParent();
}

