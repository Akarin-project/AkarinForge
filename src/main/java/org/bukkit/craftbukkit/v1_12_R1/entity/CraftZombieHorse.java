/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftAbstractHorse;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.ZombieHorse;

public class CraftZombieHorse
extends CraftAbstractHorse
implements ZombieHorse {
    public CraftZombieHorse(CraftServer server, aaw entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftZombieHorse";
    }

    @Override
    public EntityType getType() {
        return EntityType.ZOMBIE_HORSE;
    }

    @Override
    public Horse.Variant getVariant() {
        return Horse.Variant.UNDEAD_HORSE;
    }
}

