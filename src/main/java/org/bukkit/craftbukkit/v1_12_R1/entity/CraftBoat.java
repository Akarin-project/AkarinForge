/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.TreeSpecies;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftVehicle;
import org.bukkit.entity.Boat;
import org.bukkit.entity.EntityType;

public class CraftBoat
extends CraftVehicle
implements Boat {
    public CraftBoat(CraftServer server, afd entity) {
        super(server, entity);
    }

    @Override
    public TreeSpecies getWoodType() {
        return CraftBoat.getTreeSpecies(this.getHandle().s());
    }

    @Override
    public void setWoodType(TreeSpecies species) {
        this.getHandle().a(CraftBoat.getBoatType(species));
    }

    @Override
    public double getMaxSpeed() {
        return this.getHandle().maxSpeed;
    }

    @Override
    public void setMaxSpeed(double speed) {
        if (speed >= 0.0) {
            this.getHandle().maxSpeed = speed;
        }
    }

    @Override
    public double getOccupiedDeceleration() {
        return this.getHandle().occupiedDeceleration;
    }

    @Override
    public void setOccupiedDeceleration(double speed) {
        if (speed >= 0.0) {
            this.getHandle().occupiedDeceleration = speed;
        }
    }

    @Override
    public double getUnoccupiedDeceleration() {
        return this.getHandle().unoccupiedDeceleration;
    }

    @Override
    public void setUnoccupiedDeceleration(double speed) {
        this.getHandle().unoccupiedDeceleration = speed;
    }

    @Override
    public boolean getWorkOnLand() {
        return this.getHandle().landBoats;
    }

    @Override
    public void setWorkOnLand(boolean workOnLand) {
        this.getHandle().landBoats = workOnLand;
    }

    @Override
    public afd getHandle() {
        return (afd)this.entity;
    }

    @Override
    public String toString() {
        return "CraftBoat";
    }

    @Override
    public EntityType getType() {
        return EntityType.BOAT;
    }

    public static TreeSpecies getTreeSpecies(afd.b boatType) {
        switch (boatType) {
            case b: {
                return TreeSpecies.REDWOOD;
            }
            case c: {
                return TreeSpecies.BIRCH;
            }
            case d: {
                return TreeSpecies.JUNGLE;
            }
            case e: {
                return TreeSpecies.ACACIA;
            }
            case f: {
                return TreeSpecies.DARK_OAK;
            }
        }
        return TreeSpecies.GENERIC;
    }

    public static afd.b getBoatType(TreeSpecies species) {
        switch (species) {
            case REDWOOD: {
                return afd.b.b;
            }
            case BIRCH: {
                return afd.b.c;
            }
            case JUNGLE: {
                return afd.b.d;
            }
            case ACACIA: {
                return afd.b.e;
            }
            case DARK_OAK: {
                return afd.b.f;
            }
        }
        return afd.b.a;
    }

}

