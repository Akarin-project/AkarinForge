/*
 * Akarin Forge
 */
package org.bukkit;

import org.bukkit.Location;

public interface TravelAgent {
    public TravelAgent setSearchRadius(int var1);

    public int getSearchRadius();

    public TravelAgent setCreationRadius(int var1);

    public int getCreationRadius();

    public boolean getCanCreatePortal();

    public void setCanCreatePortal(boolean var1);

    public Location findOrCreate(Location var1);

    public Location findPortal(Location var1);

    public boolean createPortal(Location var1);
}

