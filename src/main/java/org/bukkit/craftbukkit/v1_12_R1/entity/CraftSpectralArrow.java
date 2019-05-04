/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftArrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.SpectralArrow;

public class CraftSpectralArrow
extends CraftArrow
implements SpectralArrow {
    public CraftSpectralArrow(CraftServer server, aeu entity) {
        super(server, entity);
    }

    @Override
    public aeu getHandle() {
        return (aeu)this.entity;
    }

    @Override
    public String toString() {
        return "CraftSpectralArrow";
    }

    @Override
    public EntityType getType() {
        return EntityType.SPECTRAL_ARROW;
    }

    @Override
    public int getGlowingTicks() {
        return this.getHandle().f;
    }

    @Override
    public void setGlowingTicks(int duration) {
        this.getHandle().f = duration;
    }
}

