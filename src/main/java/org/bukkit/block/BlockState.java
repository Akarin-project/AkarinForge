/*
 * Akarin Forge
 */
package org.bukkit.block;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.Metadatable;

public interface BlockState
extends Metadatable {
    public Block getBlock();

    public MaterialData getData();

    public Material getType();

    @Deprecated
    public int getTypeId();

    public byte getLightLevel();

    public World getWorld();

    public int getX();

    public int getY();

    public int getZ();

    public Location getLocation();

    public Location getLocation(Location var1);

    public Chunk getChunk();

    public void setData(MaterialData var1);

    public void setType(Material var1);

    @Deprecated
    public boolean setTypeId(int var1);

    public boolean update();

    public boolean update(boolean var1);

    public boolean update(boolean var1, boolean var2);

    @Deprecated
    public byte getRawData();

    @Deprecated
    public void setRawData(byte var1);

    public boolean isPlaced();
}

