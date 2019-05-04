/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.entity.Villager;
import org.bukkit.entity.Zombie;

public interface ZombieVillager
extends Zombie {
    @Override
    public void setVillagerProfession(Villager.Profession var1);

    @Override
    public Villager.Profession getVillagerProfession();
}

