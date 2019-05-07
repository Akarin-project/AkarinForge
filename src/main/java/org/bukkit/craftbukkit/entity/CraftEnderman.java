package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.monster.EntityEnderman;

import net.minecraft.block.state.IBlockState;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.EntityType;
import org.bukkit.material.MaterialData;

public class CraftEnderman extends CraftMonster implements Enderman {
    public CraftEnderman(CraftServer server, EntityEnderman entity) {
        super(server, entity);
    }

    @Override public boolean teleportRandomly() { return getHandle().teleportRandomly(); } // Paper
    public MaterialData getCarriedMaterial() {
        IBlockState blockData = getHandle().getHeldBlockState();
        return (blockData == null) ? Material.AIR.getNewData((byte) 0) : CraftMagicNumbers.getMaterial(blockData.getBlock()).getNewData((byte) blockData.getBlock().getMetaFromState(blockData));
    }

    public void setCarriedMaterial(MaterialData data) {
        getHandle().setHeldBlockState(CraftMagicNumbers.getBlock(data.getItemTypeId()).getStateFromMeta(data.getData()));
    }

    @Override
    public EntityEnderman getHandle() {
        return (EntityEnderman) entity;
    }

    @Override
    public String toString() {
        return "CraftEnderman";
    }

    public EntityType getType() {
        return EntityType.ENDERMAN;
    }
}
