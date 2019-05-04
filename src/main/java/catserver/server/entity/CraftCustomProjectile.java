/*
 * Decompiled with CFR 0_119.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 */
package catserver.server.entity;

import catserver.server.entity.CraftCustomEntity;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;

public class CraftCustomProjectile
extends CraftCustomEntity
implements Projectile {
    private ProjectileSource shooter = null;
    private boolean doesBounce;
    public static final GameProfile dropper = new GameProfile(UUID.nameUUIDFromBytes("[Dropper]".getBytes()), "[Dropper]");

    public CraftCustomProjectile(CraftServer server, vg entity) {
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
        return "CraftCustomProjectile";
    }
}

