package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SPacketUnloadChunk implements Packet<INetHandlerPlayClient>
{
    private int x;
    private int z;

    public SPacketUnloadChunk()
    {
    }

    public SPacketUnloadChunk(int xIn, int zIn)
    {
        this.x = xIn;
        this.z = zIn;
    }

    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.x = buf.readInt();
        this.z = buf.readInt();
    }

    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeInt(this.x);
        buf.writeInt(this.z);
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.processChunkUnload(this);
    }

    @SideOnly(Side.CLIENT)
    public int getX()
    {
        return this.x;
    }

    @SideOnly(Side.CLIENT)
    public int getZ()
    {
        return this.z;
    }
}