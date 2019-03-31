package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SPacketWindowProperty implements Packet<INetHandlerPlayClient>
{
    private int windowId;
    private int property;
    private int value;

    public SPacketWindowProperty()
    {
    }

    public SPacketWindowProperty(int windowIdIn, int propertyIn, int valueIn)
    {
        this.windowId = windowIdIn;
        this.property = propertyIn;
        this.value = valueIn;
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleWindowProperty(this);
    }

    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.windowId = buf.readUnsignedByte();
        this.property = buf.readShort();
        this.value = buf.readShort();
    }

    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeByte(this.windowId);
        buf.writeShort(this.property);
        buf.writeShort(this.value);
    }

    @SideOnly(Side.CLIENT)
    public int getWindowId()
    {
        return this.windowId;
    }

    @SideOnly(Side.CLIENT)
    public int getProperty()
    {
        return this.property;
    }

    @SideOnly(Side.CLIENT)
    public int getValue()
    {
        return this.value;
    }
}