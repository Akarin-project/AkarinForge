/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.meta.FireworkMeta;

public interface Firework
extends Entity {
    public FireworkMeta getFireworkMeta();

    public void setFireworkMeta(FireworkMeta var1);

    public void detonate();
}

