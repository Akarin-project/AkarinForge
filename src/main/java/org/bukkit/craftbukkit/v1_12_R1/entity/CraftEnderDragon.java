/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 *  com.google.common.collect.ImmutableSet$Builder
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftComplexLivingEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.ComplexEntityPart;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;

public class CraftEnderDragon
extends CraftComplexLivingEntity
implements EnderDragon {
    public CraftEnderDragon(CraftServer server, abd entity) {
        super(server, entity);
    }

    @Override
    public Set<ComplexEntityPart> getParts() {
        ImmutableSet.Builder builder = ImmutableSet.builder();
        for (abb part : this.getHandle().bv) {
            builder.add((Object)((ComplexEntityPart)((Object)part.getBukkitEntity())));
        }
        return builder.build();
    }

    @Override
    public abd getHandle() {
        return (abd)this.entity;
    }

    @Override
    public String toString() {
        return "CraftEnderDragon";
    }

    @Override
    public EntityType getType() {
        return EntityType.ENDER_DRAGON;
    }

    @Override
    public EnderDragon.Phase getPhase() {
        return EnderDragon.Phase.values()[this.getHandle().V().a(abd.a)];
    }

    @Override
    public void setPhase(EnderDragon.Phase phase) {
        this.getHandle().de().a(CraftEnderDragon.getMinecraftPhase(phase));
    }

    public static EnderDragon.Phase getBukkitPhase(abt phase) {
        return EnderDragon.Phase.values()[phase.b()];
    }

    public static abt getMinecraftPhase(EnderDragon.Phase phase) {
        return abt.a(phase.ordinal());
    }
}

