/*
 * Akarin Forge
 */
package org.bukkit.event.player;

import java.net.InetAddress;
import java.util.UUID;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerPreLoginEvent;

public class AsyncPlayerPreLoginEvent
extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Result result = Result.ALLOWED;
    private String message = "";
    private final String name;
    private final InetAddress ipAddress;
    private final UUID uniqueId;

    @Deprecated
    public AsyncPlayerPreLoginEvent(String name, InetAddress ipAddress) {
        this(name, ipAddress, null);
    }

    public AsyncPlayerPreLoginEvent(String name, InetAddress ipAddress, UUID uniqueId) {
        super(true);
        this.name = name;
        this.ipAddress = ipAddress;
        this.uniqueId = uniqueId;
    }

    public Result getLoginResult() {
        return this.result;
    }

    @Deprecated
    public PlayerPreLoginEvent.Result getResult() {
        return this.result == null ? null : this.result.old();
    }

    public void setLoginResult(Result result) {
        this.result = result;
    }

    @Deprecated
    public void setResult(PlayerPreLoginEvent.Result result) {
        this.result = result == null ? null : Result.valueOf(result.name());
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

    @Deprecated
    public void disallow(PlayerPreLoginEvent.Result result, String message) {
        this.result = result == null ? null : Result.valueOf(result.name());
        this.message = message;
    }

    public String getName() {
        return this.name;
    }

    public InetAddress getAddress() {
        return this.ipAddress;
    }

    public UUID getUniqueId() {
        return this.uniqueId;
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

        @Deprecated
        private PlayerPreLoginEvent.Result old() {
            return PlayerPreLoginEvent.Result.valueOf(this.name());
        }
    }

}

