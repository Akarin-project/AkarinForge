/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.Validate
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import java.util.UUID;
import org.apache.commons.lang3.Validate;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftAnimals;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryAbstractHorse;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Horse;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.AbstractHorseInventory;
import org.bukkit.inventory.Inventory;

public abstract class CraftAbstractHorse
extends CraftAnimals
implements AbstractHorse {
    public CraftAbstractHorse(CraftServer server, aao entity) {
        super(server, entity);
    }

    @Override
    public aao getHandle() {
        return (aao)this.entity;
    }

    @Override
    public void setVariant(Horse.Variant variant) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public int getDomestication() {
        return this.getHandle().dB();
    }

    @Override
    public void setDomestication(int value) {
        Validate.isTrue((boolean)(value >= 0), (String)"Domestication cannot be less than zero", (Object[])new Object[0]);
        Validate.isTrue((boolean)(value <= this.getMaxDomestication()), (String)"Domestication cannot be greater than the max domestication", (Object[])new Object[0]);
        this.getHandle().m(value);
    }

    @Override
    public int getMaxDomestication() {
        return this.getHandle().dH();
    }

    @Override
    public void setMaxDomestication(int value) {
        Validate.isTrue((boolean)(value > 0), (String)"Max domestication cannot be zero or less", (Object[])new Object[0]);
        this.getHandle().maxDomestication = value;
    }

    @Override
    public double getJumpStrength() {
        return this.getHandle().dE();
    }

    @Override
    public void setJumpStrength(double strength) {
        Validate.isTrue((boolean)(strength >= 0.0), (String)"Jump strength cannot be less than zero", (Object[])new Object[0]);
        this.getHandle().a(aaq.bx).a(strength);
    }

    @Override
    public boolean isTamed() {
        return this.getHandle().du();
    }

    @Override
    public void setTamed(boolean tamed) {
        this.getHandle().r(tamed);
    }

    @Override
    public AnimalTamer getOwner() {
        if (this.getOwnerUUID() == null) {
            return null;
        }
        return this.getServer().getOfflinePlayer(this.getOwnerUUID());
    }

    @Override
    public void setOwner(AnimalTamer owner) {
        if (owner != null) {
            this.setTamed(true);
            this.getHandle().setGoalTarget(null, null, false);
            this.setOwnerUUID(owner.getUniqueId());
        } else {
            this.setTamed(false);
            this.setOwnerUUID(null);
        }
    }

    public UUID getOwnerUUID() {
        return this.getHandle().dv();
    }

    public void setOwnerUUID(UUID uuid) {
        this.getHandle().b(uuid);
    }

    @Override
    public AbstractHorseInventory getInventory() {
        return new CraftInventoryAbstractHorse(this.getHandle().bC);
    }
}

