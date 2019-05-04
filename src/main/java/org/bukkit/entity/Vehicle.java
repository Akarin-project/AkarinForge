/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public interface Vehicle
extends Entity {
    @Override
    public Vector getVelocity();

    @Override
    public void setVelocity(Vector var1);
}

