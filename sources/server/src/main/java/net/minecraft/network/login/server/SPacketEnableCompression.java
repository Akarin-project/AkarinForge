package net.minecraft.network.login.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SPacketEnableCompression implements Packet<INetHandlerLoginClient>
{
    private int compressionThreshold;

    public SPacketEnableCompression()
    {
    }

    public SPacketEnableCompression(int thresholdIn)
    {
        this.compressionThreshold = thresholdIn;
    }

    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.compressionThreshold = buf.readVarInt();
    }

    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarInt(this.compressionThreshold);
    }

    public void processPacket(INetHandlerLoginClient handler)
    {
        handler.handleEnableCompression(this);
    }

    @SideOnly(Side.CLIENT)
    public int getCompressionThreshold()
    {
        return this.compressionThreshold;
    }
}