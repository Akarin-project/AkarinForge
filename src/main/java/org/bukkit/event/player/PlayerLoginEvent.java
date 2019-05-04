/*
 * Akarin Forge
 */
package org.bukkit.event.player;

import java.net.InetAddress;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerLoginEvent
extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final InetAddress address;
    private final String hostname;
    private Result result = Result.ALLOWED;
    private String message = "";
    private final InetAddress realAddress;

    public PlayerLoginEvent(Player player, String hostname, InetAddress address, InetAddress realAddress) {
        super(player);
        this.hostname = hostname;
        this.address = address;
        this.realAddress = realAddress;
    }

    public PlayerLoginEvent(Player player, String hostname, InetAddress address) {
        this(player, hostname, address, address);
    }

    public PlayerLoginEvent(Player player, String hostname, InetAddress address, Result result, String message, InetAddress realAddress) {
        this(player, hostname, address, realAddress);
        this.result = result;
        this.message = message;
    }

    public InetAddress getRealAddress() {
        return this.realAddress;
    }

    public Result getResult() {
        return this.result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getKickMessage() {
        return this.message;
    }

    public void setKickMessage(String message) {
        this.message = message;
    }

    public String getHostname() {
        return this.hostname;
    }

    public void allow() {
        this.result = Result.ALLOWED;
        this.message = "";
    }

    public void disallow(Result result, String message) {
        this.result = result;
        this.message = message;
    }

    public InetAddress getAddress() {
        return this.address;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public static enum Result {
        ALLOWED,
        KICK_FULL,
        KICK_BANNED,
        KICK_WHITELIST,
        KICK_OTHER;
        

        private Result() {
        }
    }

}

