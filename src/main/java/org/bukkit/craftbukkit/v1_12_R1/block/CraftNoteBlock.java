/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.block;

import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.NoteBlock;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBlockEntityState;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers;

public class CraftNoteBlock
extends CraftBlockEntityState<avz>
implements NoteBlock {
    public CraftNoteBlock(Block block) {
        super(block, avz.class);
    }

    public CraftNoteBlock(Material material, avz te2) {
        super(material, te2);
    }

    @Override
    public Note getNote() {
        return new Note(((avz)this.getSnapshot()).a);
    }

    @Override
    public byte getRawNote() {
        return ((avz)this.getSnapshot()).a;
    }

    @Override
    public void setNote(Note note) {
        ((avz)this.getSnapshot()).a = note.getId();
    }

    @Override
    public void setRawNote(byte note) {
        ((avz)this.getSnapshot()).a = note;
    }

    @Override
    public boolean play() {
        Block block = this.getBlock();
        if (block.getType() == Material.NOTE_BLOCK) {
            avz note = (avz)this.getTileEntityFromWorld();
            CraftWorld world = (CraftWorld)this.getWorld();
            note.a(world.getHandle(), new et(this.getX(), this.getY(), this.getZ()));
            return true;
        }
        return false;
    }

    @Override
    public boolean play(byte instrument, byte note) {
        Block block = this.getBlock();
        if (block.getType() == Material.NOTE_BLOCK) {
            CraftWorld world = (CraftWorld)this.getWorld();
            world.getHandle().c(new et(this.getX(), this.getY(), this.getZ()), CraftMagicNumbers.getBlock(block), instrument, note);
            return true;
        }
        return false;
    }

    @Override
    public boolean play(Instrument instrument, Note note) {
        Block block = this.getBlock();
        if (block.getType() == Material.NOTE_BLOCK) {
            CraftWorld world = (CraftWorld)this.getWorld();
            world.getHandle().c(new et(this.getX(), this.getY(), this.getZ()), CraftMagicNumbers.getBlock(block), instrument.getType(), note.getId());
            return true;
        }
        return false;
    }
}

