package io.akarin.forge.server.layers.entity;

import com.mojang.authlib.GameProfile;

import io.akarin.forge.server.layers.entity.CraftModdedEntity;
import net.minecraft.entity.Entity;

import java.util.UUID;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;

public class CraftModdedProjectile extends CraftModdedEntity implements Projectile {
    public static final GameProfile dropper = new GameProfile(UUID.nameUUIDFromBytes("[Dropper]".getBytes()), "[Dropper]");
	
    private ProjectileSource shooter = null;
    private boolean doesBounce;

    public CraftModdedProjectile(CraftServer server, Entity entity) {
        super(server, entity);
    }

    @Override
    public ProjectileSource getShooter() {
        return this.shooter;
    }

    @Override
    public void setShooter(ProjectileSource shooter) {
        this.shooter = shooter;
    }

    @Override
    public boolean doesBounce() {
        return this.doesBounce;
    }

    @Override
    public void setBounce(boolean doesBounce) {
        this.doesBounce = doesBounce;
    }

    @Override
    public String toString() {
        return "CraftModdedProjectile";
    }
}

