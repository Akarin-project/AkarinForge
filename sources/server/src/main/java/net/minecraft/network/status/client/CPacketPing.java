package net.minecraft.network.status.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.status.INetHandlerStatusServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CPacketPing implements Packet<INetHandlerStatusServer>
{
    private long clientTime;

    public CPacketPing()
    {
    }

    @SideOnly(Side.CLIENT)
    public CPacketPing(long clientTimeIn)
    {
        this.clientTime = clientTimeIn;
    }

    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.clientTime = buf.readLong();
    }

    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeLong(this.clientTime);
    }

    public void processPacket(INetHandlerStatusServer handler)
    {
        handler.processPing(this);
    }

    public long getClientTime()
    {
        return this.clientTime;
    }
}