/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.entity.Creature;

public interface Ageable
extends Creature {
    public int getAge();

    public void setAge(int var1);

    public void setAgeLock(boolean var1);

    public boolean getAgeLock();

    public void setBaby();

    public void setAdult();

    public boolean isAdult();

    public boolean canBreed();

    public void setBreed(boolean var1);
}

