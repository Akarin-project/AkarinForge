package net.minecraft.world.chunk;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockStatePaletteRegistry implements IBlockStatePalette
{
    public int idFor(IBlockState state)
    {
        int i = Block.BLOCK_STATE_IDS.get(state);
        return i == -1 ? 0 : i;
    }

    public IBlockState getBlockState(int indexKey)
    {
        IBlockState iblockstate = Block.BLOCK_STATE_IDS.getByValue(indexKey);
        return iblockstate == null ? Blocks.AIR.getDefaultState() : iblockstate;
    }

    @SideOnly(Side.CLIENT)
    public void read(PacketBuffer buf)
    {
        buf.readVarInt();
    }

    public void write(PacketBuffer buf)
    {
        buf.writeVarInt(0);
    }

    public int getSerializedSize()
    {
        return PacketBuffer.getVarIntSize(0);
    }
}