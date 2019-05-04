/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.block;

import java.util.Objects;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.EndGateway;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBlockEntityState;

public class CraftEndGateway
extends CraftBlockEntityState<awf>
implements EndGateway {
    public CraftEndGateway(Block block) {
        super(block, awf.class);
    }

    public CraftEndGateway(Material material, awf te2) {
        super(material, te2);
    }

    @Override
    public Location getExitLocation() {
        et pos = ((awf)this.getSnapshot()).h;
        return pos == null ? null : new Location(this.isPlaced() ? this.getWorld() : null, pos.p(), pos.q(), pos.r());
    }

    @Override
    public void setExitLocation(Location location) {
        if (location == null) {
            ((awf)this.getSnapshot()).h = null;
        } else {
            if (!Objects.equals(location.getWorld(), this.isPlaced() ? this.getWorld() : null)) {
                throw new IllegalArgumentException("Cannot set exit location to different world");
            }
            ((awf)this.getSnapshot()).h = new et(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        }
    }

    @Override
    public boolean isExactTeleport() {
        return ((awf)this.getSnapshot()).i;
    }

    @Override
    public void setExactTeleport(boolean exact) {
        ((awf)this.getSnapshot()).i = exact;
    }

    @Override
    public void applyTo(awf endGateway) {
        super.applyTo(endGateway);
        if (((awf)this.getSnapshot()).h == null) {
            endGateway.h = null;
        }
    }
}

