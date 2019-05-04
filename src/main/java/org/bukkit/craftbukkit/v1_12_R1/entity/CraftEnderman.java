/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftMonster;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.EntityType;
import org.bukkit.material.MaterialData;

public class CraftEnderman
extends CraftMonster
implements Enderman {
    public CraftEnderman(CraftServer server, acu entity) {
        super(server, entity);
    }

    @Override
    public MaterialData getCarriedMaterial() {
        awt blockData = this.getHandle().dn();
        return blockData == null ? Material.AIR.getNewData(0) : CraftMagicNumbers.getMaterial(blockData.u()).getNewData((byte)blockData.u().e(blockData));
    }

    @Override
    public void setCarriedMaterial(MaterialData data) {
        this.getHandle().b(CraftMagicNumbers.getBlock(data.getItemTypeId()).a(data.getData()));
    }

    @Override
    public acu getHandle() {
        return (acu)this.entity;
    }

    @Override
    public String toString() {
        return "CraftEnderman";
    }

    @Override
    public EntityType getType() {
        return EntityType.ENDERMAN;
    }
}

