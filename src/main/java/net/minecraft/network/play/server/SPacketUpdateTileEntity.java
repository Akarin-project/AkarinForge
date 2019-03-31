package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SPacketUpdateTileEntity implements Packet<INetHandlerPlayClient>
{
    private BlockPos blockPos;
    private int tileEntityType;
    private NBTTagCompound nbt;

    public SPacketUpdateTileEntity()
    {
    }

    public SPacketUpdateTileEntity(BlockPos blockPosIn, int tileEntityTypeIn, NBTTagCompound compoundIn)
    {
        this.blockPos = blockPosIn;
        this.tileEntityType = tileEntityTypeIn;
        this.nbt = compoundIn;
    }

    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.blockPos = buf.readBlockPos();
        this.tileEntityType = buf.readUnsignedByte();
        this.nbt = buf.readCompoundTag();
    }

    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeBlockPos(this.blockPos);
        buf.writeByte((byte)this.tileEntityType);
        buf.writeCompoundTag(this.nbt);
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleUpdateTileEntity(this);
    }

    @SideOnly(Side.CLIENT)
    public BlockPos getPos()
    {
        return this.blockPos;
    }

    @SideOnly(Side.CLIENT)
    public int getTileEntityType()
    {
        return this.tileEntityType;
    }

    @SideOnly(Side.CLIENT)
    public NBTTagCompound getNbtCompound()
    {
        return this.nbt;
    }
}