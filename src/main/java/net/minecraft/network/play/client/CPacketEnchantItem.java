package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CPacketEnchantItem implements Packet<INetHandlerPlayServer>
{
    private int windowId;
    private int button;

    public CPacketEnchantItem()
    {
    }

    @SideOnly(Side.CLIENT)
    public CPacketEnchantItem(int windowIdIn, int buttonIn)
    {
        this.windowId = windowIdIn;
        this.button = buttonIn;
    }

    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processEnchantItem(this);
    }

    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.windowId = buf.readByte();
        this.button = buf.readByte();
    }

    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeByte(this.windowId);
        buf.writeByte(this.button);
    }

    public int getWindowId()
    {
        return this.windowId;
    }

    public int getButton()
    {
        return this.button;
    }
}