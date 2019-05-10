/*
 * Akarin Forge
 */
package org.bukkit.event.hanging;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Hanging;
import org.bukkit.event.hanging.HangingBreakEvent;

public class HangingBreakByEntityEvent
extends HangingBreakEvent {
    private final Entity remover;

    public HangingBreakByEntityEvent(Hanging hanging, Entity remover) {
        this(hanging, remover, HangingBreakEvent.RemoveCause.ENTITY);
    }

    public HangingBreakByEntityEvent(Hanging hanging, Entity remover, HangingBreakEvent.RemoveCause cause) {
        super(hanging, cause);
        this.remover = remover;
    }

    public Entity getRemover() {
        return this.remover;
    }
}

