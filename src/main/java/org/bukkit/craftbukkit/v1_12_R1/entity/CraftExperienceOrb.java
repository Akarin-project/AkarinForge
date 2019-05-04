/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;

public class CraftExperienceOrb
extends CraftEntity
implements ExperienceOrb {
    public CraftExperienceOrb(CraftServer server, vm entity) {
        super(server, entity);
    }

    @Override
    public int getExperience() {
        return this.getHandle().e;
    }

    @Override
    public void setExperience(int value) {
        this.getHandle().e = value;
    }

    @Override
    public vm getHandle() {
        return (vm)this.entity;
    }

    @Override
    public String toString() {
        return "CraftExperienceOrb";
    }

    @Override
    public EntityType getType() {
        return EntityType.EXPERIENCE_ORB;
    }
}

