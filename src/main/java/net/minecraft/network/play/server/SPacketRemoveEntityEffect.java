package net.minecraft.network.play.server;

import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.potion.Potion;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SPacketRemoveEntityEffect implements Packet<INetHandlerPlayClient>
{
    private int entityId;
    private Potion effectId;

    public SPacketRemoveEntityEffect()
    {
    }

    public SPacketRemoveEntityEffect(int entityIdIn, Potion potionIn)
    {
        this.entityId = entityIdIn;
        this.effectId = potionIn;
    }

    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.entityId = buf.readVarInt();
        this.effectId = Potion.getPotionById(buf.readUnsignedByte());
    }

    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarInt(this.entityId);
        buf.writeByte(Potion.getIdFromPotion(this.effectId));
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleRemoveEntityEffect(this);
    }

    @Nullable
    @SideOnly(Side.CLIENT)
    public Entity getEntity(World worldIn)
    {
        return worldIn.getEntityByID(this.entityId);
    }

    @Nullable
    @SideOnly(Side.CLIENT)
    public Potion getPotion()
    {
        return this.effectId;
    }
}