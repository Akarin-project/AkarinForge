/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftVehicle;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

public class CraftMinecart
extends CraftVehicle
implements Minecart {
    public CraftMinecart(CraftServer server, afe entity) {
        super(server, entity);
    }

    @Override
    public void setDamage(double damage) {
        this.getHandle().a((float)damage);
    }

    @Override
    public double getDamage() {
        return this.getHandle().s();
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
    public boolean isSlowWhenEmpty() {
        return this.getHandle().slowWhenEmpty;
    }

    @Override
    public void setSlowWhenEmpty(boolean slow) {
        this.getHandle().slowWhenEmpty = slow;
    }

    @Override
    public Vector getFlyingVelocityMod() {
        return this.getHandle().getFlyingVelocityMod();
    }

    @Override
    public void setFlyingVelocityMod(Vector flying) {
        this.getHandle().setFlyingVelocityMod(flying);
    }

    @Override
    public Vector getDerailedVelocityMod() {
        return this.getHandle().getDerailedVelocityMod();
    }

    @Override
    public void setDerailedVelocityMod(Vector derailed) {
        this.getHandle().setDerailedVelocityMod(derailed);
    }

    @Override
    public afe getHandle() {
        return (afe)this.entity;
    }

    @Override
    public void setDisplayBlock(MaterialData material) {
        if (material != null) {
            awt block = CraftMagicNumbers.getBlock(material.getItemTypeId()).a(material.getData());
            this.getHandle().b(block);
        } else {
            this.getHandle().b(aox.a.t());
            this.getHandle().a(false);
        }
    }

    @Override
    public MaterialData getDisplayBlock() {
        awt blockData = this.getHandle().w();
        return CraftMagicNumbers.getMaterial(blockData.u()).getNewData((byte)blockData.u().e(blockData));
    }

    @Override
    public void setDisplayBlockOffset(int offset) {
        this.getHandle().f(offset);
    }

    @Override
    public int getDisplayBlockOffset() {
        return this.getHandle().y();
    }

    @Override
    public EntityType getType() {
        return EntityType.MINECART;
    }
}

