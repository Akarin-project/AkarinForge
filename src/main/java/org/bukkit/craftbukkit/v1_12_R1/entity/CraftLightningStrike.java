/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LightningStrike;

public class CraftLightningStrike
extends CraftEntity
implements LightningStrike {
    private final LightningStrike.Spigot spigot;

    public CraftLightningStrike(CraftServer server, aci entity) {
        super(server, entity);
        this.spigot = new LightningStrike.Spigot(){

            @Override
            public boolean isSilent() {
                return CraftLightningStrike.this.getHandle().isSilent;
            }
        };
    }

    @Override
    public boolean isEffect() {
        return ((aci)super.getHandle()).isEffect;
    }

    @Override
    public aci getHandle() {
        return (aci)this.entity;
    }

    @Override
    public String toString() {
        return "CraftLightningStrike";
    }

    @Override
    public EntityType getType() {
        return EntityType.LIGHTNING;
    }

    @Override
    public LightningStrike.Spigot org_bukkit_entity_LightningStrike$Spigot_spigot() {
        return this.spigot;
    }

}

