/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.Validate
 */
package org.bukkit.event.player;

import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

@Deprecated
public class PlayerChatEvent
extends PlayerEvent
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    private String message;
    private String format;
    private final Set<Player> recipients;

    public PlayerChatEvent(Player player, String message) {
        super(player);
        this.message = message;
        this.format = "<%1$s> %2$s";
        this.recipients = new HashSet<Player>(player.getServer().getOnlinePlayers());
    }

    public PlayerChatEvent(Player player, String message, String format, Set<Player> recipients) {
        super(player);
        this.message = message;
        this.format = format;
        this.recipients = recipients;
    }

    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPlayer(Player player) {
        Validate.notNull((Object)player, (String)"Player cannot be null");
        this.player = player;
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        try {
            String.format(format, this.player, this.message);
        }
        catch (RuntimeException ex2) {
            ex2.fillInStackTrace();
            throw ex2;
        }
        this.format = format;
    }

    public Set<Player> getRecipients() {
        return this.recipients;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

