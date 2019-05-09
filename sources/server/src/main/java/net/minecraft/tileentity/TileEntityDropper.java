package net.minecraft.tileentity;

import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;

public class TileEntityDropper extends TileEntityDispenser
{
    public static void registerFixesDropper(DataFixer fixer)
    {
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityDropper.class, new String[] {"Items"}));
    }

    public String getName()
    {
        return this.hasCustomName() ? this.customName : "container.dropper";
    }

    public String getGuiID()
    {
        return "minecraft:dropper";
    }
}