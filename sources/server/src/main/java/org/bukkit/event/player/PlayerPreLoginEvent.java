/*
 * Akarin Forge
 */
package org.bukkit.event.player;

import java.net.InetAddress;
import java.util.UUID;
import org.bukkit.Warning;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Deprecated
public class PlayerPreLoginEvent
extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Result result = Result.ALLOWED;
    private String message = "";
    private final String name;
    private final InetAddress ipAddress;
    private final UUID uniqueId;

    @Deprecated
    public PlayerPreLoginEvent(String name, InetAddress ipAddress) {
        this(name, ipAddress, null);
    }

    public PlayerPreLoginEvent(String name, InetAddress ipAddress, UUID uniqueId) {
        this.name = name;
        this.ipAddress = ipAddress;
        this.uniqueId = uniqueId;
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

    public void allow() {
        this.result = Result.ALLOWED;
        this.message = "";
    }

    public void disallow(Result result, String message) {
        this.result = result;
        this.message = message;
    }

    public String getName() {
        return this.name;
    }

    public InetAddress getAddress() {
        return this.ipAddress;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public UUID getUniqueId() {
        return this.uniqueId;
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

