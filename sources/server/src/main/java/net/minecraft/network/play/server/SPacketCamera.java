package net.minecraft.network.play.server;

import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SPacketCamera implements Packet<INetHandlerPlayClient>
{
    public int entityId;

    public SPacketCamera()
    {
    }

    public SPacketCamera(Entity entityIn)
    {
        this.entityId = entityIn.getEntityId();
    }

    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.entityId = buf.readVarInt();
    }

    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarInt(this.entityId);
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleCamera(this);
    }

    @Nullable
    @SideOnly(Side.CLIENT)
    public Entity getEntity(World worldIn)
    {
        return worldIn.getEntityByID(this.entityId);
    }
}