package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CPacketCloseWindow implements Packet<INetHandlerPlayServer>
{
    private int windowId;

    public CPacketCloseWindow()
    {
    }

    @SideOnly(Side.CLIENT)
    public CPacketCloseWindow(int windowIdIn)
    {
        this.windowId = windowIdIn;
    }

    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processCloseWindow(this);
    }

    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.windowId = buf.readByte();
    }

    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeByte(this.windowId);
    }
}