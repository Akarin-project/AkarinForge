package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SPacketPlayerListHeaderFooter implements Packet<INetHandlerPlayClient>
{
    private ITextComponent header;
    private ITextComponent footer;

    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.header = buf.readTextComponent();
        this.footer = buf.readTextComponent();
    }

    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeTextComponent(this.header);
        buf.writeTextComponent(this.footer);
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handlePlayerListHeaderFooter(this);
    }

    @SideOnly(Side.CLIENT)
    public ITextComponent getHeader()
    {
        return this.header;
    }

    @SideOnly(Side.CLIENT)
    public ITextComponent getFooter()
    {
        return this.footer;
    }
}