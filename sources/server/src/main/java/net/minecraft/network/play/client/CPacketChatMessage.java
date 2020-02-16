package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketChatMessage implements Packet<INetHandlerPlayServer>
{
    private String message;

    public CPacketChatMessage()
    {
    }

    public CPacketChatMessage(String messageIn)
    {
        if (messageIn.length() > 256)
        {
            messageIn = messageIn.substring(0, 256);
        }

        this.message = messageIn;
    }

    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.message = buf.readString(256);
    }

    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeString(this.message);
    }

    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processChatMessage(this);
    }

    public String getMessage()
    {
        return this.message;
    }
}