/*
 * Akarin Forge
 */
package org.bukkit.entity;

import java.util.Set;
import org.bukkit.entity.ComplexEntityPart;
import org.bukkit.entity.LivingEntity;

public interface ComplexLivingEntity
extends LivingEntity {
    public Set<ComplexEntityPart> getParts();
}

