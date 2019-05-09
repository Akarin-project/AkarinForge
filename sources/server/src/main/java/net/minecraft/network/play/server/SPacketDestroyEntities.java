package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SPacketDestroyEntities implements Packet<INetHandlerPlayClient>
{
    private int[] entityIDs;

    public SPacketDestroyEntities()
    {
    }

    public SPacketDestroyEntities(int... entityIdsIn)
    {
        this.entityIDs = entityIdsIn;
    }

    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.entityIDs = new int[buf.readVarInt()];

        for (int i = 0; i < this.entityIDs.length; ++i)
        {
            this.entityIDs[i] = buf.readVarInt();
        }
    }

    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarInt(this.entityIDs.length);

        for (int i : this.entityIDs)
        {
            buf.writeVarInt(i);
        }
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleDestroyEntities(this);
    }

    @SideOnly(Side.CLIENT)
    public int[] getEntityIDs()
    {
        return this.entityIDs;
    }
}