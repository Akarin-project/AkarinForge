package org.bukkit.craftbukkit.entity;

import java.util.List;
import net.minecraftforge.common.util.FakePlayer;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;

import io.akarin.forge.AkarinForge;

public class CraftFakePlayer
extends CraftPlayer {
    private Player realPlayer = null;

    public CraftFakePlayer(CraftServer server, FakePlayer entity) {
        super(server, entity);
        this.realPlayer = this.getRealPlayer();
    }

    @Override
    public boolean hasPermission(String name) {
        if (AkarinForge.fakePlayerPermissions.contains(name)) {
            return true;
        }
        Player realPlayer = this.getRealPlayer();
        if (realPlayer == null) {
            return super.hasPermission(name);
        }
        return realPlayer.hasPermission(name);
    }

    @Override
    public boolean isPermissionSet(String name) {
        if (AkarinForge.fakePlayerPermissions.contains(name)) {
            return true;
        }
        Player realPlayer = this.getRealPlayer();
        if (realPlayer == null) {
            return super.isPermissionSet(name);
        }
        return realPlayer.isPermissionSet(name);
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        if (AkarinForge.fakePlayerPermissions.contains(perm.getName())) {
            return true;
        }
        Player realPlayer = this.getRealPlayer();
        if (realPlayer == null) {
            return super.isPermissionSet(perm);
        }
        return realPlayer.isPermissionSet(perm);
    }

    @Override
    public boolean hasPermission(Permission perm) {
        if (AkarinForge.fakePlayerPermissions.contains(perm.getName())) {
            return true;
        }
        Player realPlayer = this.getRealPlayer();
        if (realPlayer == null) {
            return super.hasPermission(perm);
        }
        return realPlayer.hasPermission(perm);
    }

    private Player getRealPlayer() {
        Player getRealPlayer;
        String myName;
        if (this.realPlayer != null && this.realPlayer.isOnline()) {
            return this.realPlayer;
        }
        if (this.realPlayer != null) {
            this.realPlayer = null;
        }
        if ((getRealPlayer = this.server.getPlayerExact(myName = this.getHandle().getName())) instanceof CraftFakePlayer) {
            return null;
        }
        if (getRealPlayer != null) {
            this.realPlayer = getRealPlayer;
        }
        return getRealPlayer;
    }

    @Override
    public void updateInventory() {
    }

    @Override
    public void sendPluginMessage(Plugin source, String channel, byte[] message) {
    }
}

