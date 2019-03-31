package net.minecraft.network.play.server;

import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SPacketCustomPayload implements Packet<INetHandlerPlayClient>
{
    private String channel;
    private PacketBuffer data;

    public SPacketCustomPayload()
    {
    }

    public SPacketCustomPayload(String channelIn, PacketBuffer bufIn)
    {
        this.channel = channelIn;
        this.data = bufIn;

        if (bufIn.writerIndex() > 1048576)
        {
            throw new IllegalArgumentException("Payload may not be larger than 1048576 bytes");
        }
    }

    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.channel = buf.readString(20);
        int i = buf.readableBytes();

        if (i >= 0 && i <= 1048576)
        {
            this.data = new PacketBuffer(buf.readBytes(i));
        }
        else
        {
            throw new IOException("Payload may not be larger than 1048576 bytes");
        }
    }

    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeString(this.channel);
        synchronized(this.data) { //This may be access multiple times, from multiple threads, lets be safe.
        this.data.markReaderIndex();
        buf.writeBytes((ByteBuf)this.data);
        this.data.resetReaderIndex();
        }
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleCustomPayload(this);
        // Forge: fix network buffer leaks (MC-121884)
        if (this.data != null) this.data.release();
    }

    @SideOnly(Side.CLIENT)
    public String getChannelName()
    {
        return this.channel;
    }

    @SideOnly(Side.CLIENT)
    public PacketBuffer getBufferData()
    {
        return this.data;
    }
}