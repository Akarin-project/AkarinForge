/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import java.util.UUID;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftAnimals;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.entity.EntityTargetEvent;

public class CraftTameableAnimal
extends CraftAnimals
implements Tameable,
Creature {
    public CraftTameableAnimal(CraftServer server, wb entity) {
        super(server, entity);
    }

    @Override
    public wb getHandle() {
        return (wb)super.getHandle();
    }

    public UUID getOwnerUUID() {
        try {
            return this.getHandle().b();
        }
        catch (IllegalArgumentException ex2) {
            return null;
        }
    }

    public void setOwnerUUID(UUID uuid) {
        this.getHandle().b(uuid);
    }

    @Override
    public AnimalTamer getOwner() {
        if (this.getOwnerUUID() == null) {
            return null;
        }
        OfflinePlayer owner = this.getServer().getPlayer(this.getOwnerUUID());
        if (owner == null) {
            owner = this.getServer().getOfflinePlayer(this.getOwnerUUID());
        }
        return owner;
    }

    @Override
    public boolean isTamed() {
        return this.getHandle().dl();
    }

    @Override
    public void setOwner(AnimalTamer tamer) {
        if (tamer != null) {
            this.setTamed(true);
            this.getHandle().setGoalTarget(null, null, false);
            this.setOwnerUUID(tamer.getUniqueId());
        } else {
            this.setTamed(false);
            this.setOwnerUUID(null);
        }
    }

    @Override
    public void setTamed(boolean tame) {
        this.getHandle().q(tame);
        if (!tame) {
            this.setOwnerUUID(null);
        }
    }

    public boolean isSitting() {
        return this.getHandle().dn();
    }

    public void setSitting(boolean sitting) {
        this.getHandle().r(sitting);
        this.getHandle().dp().a(sitting);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{owner=" + this.getOwner() + ",tamed=" + this.isTamed() + "}";
    }
}

