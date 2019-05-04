/*
 * Decompiled with CFR 0_119.
 */
package catserver.api.bukkit.event;

import net.minecraftforge.fml.common.eventhandler.Event;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

public class ForgeEvent
extends org.bukkit.event.Event {
    private static final HandlerList handlers = new HandlerList();
    private final Event forgeEvent;

    public ForgeEvent(Event forgeEvent) {
        super(!Bukkit.getServer().isPrimaryThread());
        this.forgeEvent = forgeEvent;
    }

    public Event getForgeEvent() {
        return this.forgeEvent;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

