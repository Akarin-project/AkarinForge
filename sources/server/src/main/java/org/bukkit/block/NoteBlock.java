/*
 * Akarin Forge
 */
package org.bukkit.block;

import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.block.BlockState;

public interface NoteBlock
extends BlockState {
    public Note getNote();

    @Deprecated
    public byte getRawNote();

    public void setNote(Note var1);

    @Deprecated
    public void setRawNote(byte var1);

    public boolean play();

    @Deprecated
    public boolean play(byte var1, byte var2);

    public boolean play(Instrument var1, Note var2);
}

