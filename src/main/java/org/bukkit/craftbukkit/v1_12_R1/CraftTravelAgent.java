/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1;

import org.bukkit.Location;
import org.bukkit.TravelAgent;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;

public class CraftTravelAgent
extends anc
implements TravelAgent {
    public static TravelAgent DEFAULT = null;
    private int searchRadius = 128;
    private int creationRadius = 16;
    private boolean canCreatePortal = true;

    public CraftTravelAgent(oo worldserver) {
        super(worldserver);
        if (DEFAULT == null && worldserver.dimension == 0) {
            DEFAULT = this;
        }
    }

    @Override
    public Location findOrCreate(Location target) {
        oo worldServer = ((CraftWorld)target.getWorld()).getHandle();
        Location found = this.findPortal(target);
        if (found == null) {
            found = this.getCanCreatePortal() && this.createPortal(target) ? this.findPortal(target) : target;
        }
        return found;
    }

    @Override
    public Location findPortal(Location location) {
        anc pta = ((CraftWorld)location.getWorld()).getHandle().x();
        et found = pta.findPortal(location.getX(), location.getY(), location.getZ(), this.getSearchRadius());
        return found != null ? new Location(location.getWorld(), found.p(), found.q(), found.r(), location.getYaw(), location.getPitch()) : null;
    }

    @Override
    public boolean createPortal(Location location) {
        anc pta = ((CraftWorld)location.getWorld()).getHandle().x();
        return pta.createPortal(location.getX(), location.getY(), location.getZ(), this.getCreationRadius());
    }

    @Override
    public TravelAgent setSearchRadius(int radius) {
        this.searchRadius = radius;
        return this;
    }

    @Override
    public int getSearchRadius() {
        return this.searchRadius;
    }

    @Override
    public TravelAgent setCreationRadius(int radius) {
        this.creationRadius = radius < 2 ? 0 : radius;
        return this;
    }

    @Override
    public int getCreationRadius() {
        return this.creationRadius;
    }

    @Override
    public boolean getCanCreatePortal() {
        return this.canCreatePortal;
    }

    @Override
    public void setCanCreatePortal(boolean create) {
        this.canCreatePortal = create;
    }
}

