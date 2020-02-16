package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.EnumHand;

public class CPacketPlayerTryUseItem implements Packet<INetHandlerPlayServer>
{
    private EnumHand hand;

    public CPacketPlayerTryUseItem()
    {
    }

    public CPacketPlayerTryUseItem(EnumHand handIn)
    {
        this.hand = handIn;
    }

    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.hand = (EnumHand)buf.readEnumValue(EnumHand.class);
    }

    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeEnumValue(this.hand);
    }

    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processTryUseItem(this);
    }

    public EnumHand getHand()
    {
        return this.hand;
    }
}