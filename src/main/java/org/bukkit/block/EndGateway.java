/*
 * Akarin Forge
 */
package org.bukkit.block;

import org.bukkit.Location;
import org.bukkit.block.BlockState;

public interface EndGateway
extends BlockState {
    public Location getExitLocation();

    public void setExitLocation(Location var1);

    public boolean isExactTeleport();

    public void setExactTeleport(boolean var1);
}

