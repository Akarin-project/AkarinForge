/*
 * Akarin Forge
 */
package org.bukkit.event.block;

import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;

public class NotePlayEvent
extends BlockEvent
implements Cancellable {
    private static HandlerList handlers = new HandlerList();
    private Instrument instrument;
    private Note note;
    private boolean cancelled = false;

    public NotePlayEvent(Block block, Instrument instrument, Note note) {
        super(block);
        this.instrument = instrument;
        this.note = note;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public Instrument getInstrument() {
        return this.instrument;
    }

    public Note getNote() {
        return this.note;
    }

    public void setInstrument(Instrument instrument) {
        if (instrument != null) {
            this.instrument = instrument;
        }
    }

    public void setNote(Note note) {
        if (note != null) {
            this.note = note;
        }
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

