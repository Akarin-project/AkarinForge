package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketResourcePackStatus implements Packet<INetHandlerPlayServer>
{
    private CPacketResourcePackStatus.Action action;

    public CPacketResourcePackStatus()
    {
    }

    public CPacketResourcePackStatus(CPacketResourcePackStatus.Action p_i47156_1_)
    {
        this.action = p_i47156_1_;
    }

    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.action = (CPacketResourcePackStatus.Action)buf.readEnumValue(CPacketResourcePackStatus.Action.class);
    }

    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeEnumValue(this.action);
    }

    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.handleResourcePackStatus(this);
    }

    public static enum Action
    {
        SUCCESSFULLY_LOADED,
        DECLINED,
        FAILED_DOWNLOAD,
        ACCEPTED;
    }
}