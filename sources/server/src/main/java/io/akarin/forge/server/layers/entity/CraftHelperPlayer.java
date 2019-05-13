package io.akarin.forge.server.layers.entity;

import java.util.List;
import net.minecraftforge.common.util.FakePlayer;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;

public class CraftHelperPlayer
extends CraftPlayer {
    private Player realPlayer = null;

    public CraftHelperPlayer(CraftServer server, FakePlayer entity) {
        super(server, entity);
        this.realPlayer = this.getRealPlayer();
    }

    @Override
    public boolean hasPermission(String name) {
        if (true) {
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
        if (true) {
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
        if (true) {
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
        if (true) {
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
        if ((getRealPlayer = this.server.getPlayerExact(myName = this.getHandle().getName())) instanceof CraftHelperPlayer) {
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

