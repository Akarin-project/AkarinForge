/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.entity.Zombie;

public interface PigZombie
extends Zombie {
    public int getAnger();

    public void setAnger(int var1);

    public void setAngry(boolean var1);

    public boolean isAngry();
}

