package org.bukkit.craftbukkit.v1_12_R1.entity;

import net.minecraft.entity.passive.EntityDonkey;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.Donkey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse.Variant;

public class CraftDonkey extends CraftChestedHorse implements Donkey {

    public CraftDonkey(CraftServer server, EntityDonkey entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftDonkey";
    }

    @Override
    public EntityType getType() {
        return EntityType.DONKEY;
    }

    @Override
    public Variant getVariant() {
        return Variant.DONKEY;
    }
}
