/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.entity.Monster;
import org.bukkit.entity.Villager;

public interface Zombie
extends Monster {
    public boolean isBaby();

    public void setBaby(boolean var1);

    @Deprecated
    public boolean isVillager();

    @Deprecated
    public void setVillager(boolean var1);

    @Deprecated
    public void setVillagerProfession(Villager.Profession var1);

    @Deprecated
    public Villager.Profession getVillagerProfession();
}

