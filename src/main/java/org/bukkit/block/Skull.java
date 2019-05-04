/*
 * Akarin Forge
 */
package org.bukkit.block;

import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;

public interface Skull
extends BlockState {
    public boolean hasOwner();

    @Deprecated
    public String getOwner();

    @Deprecated
    public boolean setOwner(String var1);

    public OfflinePlayer getOwningPlayer();

    public void setOwningPlayer(OfflinePlayer var1);

    public BlockFace getRotation();

    public void setRotation(BlockFace var1);

    public SkullType getSkullType();

    public void setSkullType(SkullType var1);
}

