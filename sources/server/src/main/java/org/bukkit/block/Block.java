/*
 * Akarin Forge
 */
package org.bukkit.block;

import java.util.Collection;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.Metadatable;

public interface Block
extends Metadatable {
    @Deprecated
    public byte getData();

    public Block getRelative(int var1, int var2, int var3);

    public Block getRelative(BlockFace var1);

    public Block getRelative(BlockFace var1, int var2);

    public Material getType();

    @Deprecated
    public int getTypeId();

    public byte getLightLevel();

    public byte getLightFromSky();

    public byte getLightFromBlocks();

    public World getWorld();

    public int getX();

    public int getY();

    public int getZ();

    public Location getLocation();

    public Location getLocation(Location var1);

    public Chunk getChunk();

    @Deprecated
    public void setData(byte var1);

    @Deprecated
    public void setData(byte var1, boolean var2);

    public void setType(Material var1);

    public void setType(Material var1, boolean var2);

    @Deprecated
    public boolean setTypeId(int var1);

    @Deprecated
    public boolean setTypeId(int var1, boolean var2);

    @Deprecated
    public boolean setTypeIdAndData(int var1, byte var2, boolean var3);

    public BlockFace getFace(Block var1);

    public BlockState getState();

    public Biome getBiome();

    public void setBiome(Biome var1);

    public boolean isBlockPowered();

    public boolean isBlockIndirectlyPowered();

    public boolean isBlockFacePowered(BlockFace var1);

    public boolean isBlockFaceIndirectlyPowered(BlockFace var1);

    public int getBlockPower(BlockFace var1);

    public int getBlockPower();

    public boolean isEmpty();

    public boolean isLiquid();

    public double getTemperature();

    public double getHumidity();

    public PistonMoveReaction getPistonMoveReaction();

    public boolean breakNaturally();

    public boolean breakNaturally(ItemStack var1);

    public Collection<ItemStack> getDrops();

    public Collection<ItemStack> getDrops(ItemStack var1);
}

