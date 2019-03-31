package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SPacketTabComplete implements Packet<INetHandlerPlayClient>
{
    private String[] matches;

    public SPacketTabComplete()
    {
    }

    public SPacketTabComplete(String[] matchesIn)
    {
        this.matches = matchesIn;
    }

    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.matches = new String[buf.readVarInt()];

        for (int i = 0; i < this.matches.length; ++i)
        {
            this.matches[i] = buf.readString(32767);
        }
    }

    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarInt(this.matches.length);

        for (String s : this.matches)
        {
            buf.writeString(s);
        }
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleTabComplete(this);
    }

    @SideOnly(Side.CLIENT)
    public String[] getMatches()
    {
        return this.matches;
    }
}