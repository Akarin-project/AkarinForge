package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CPacketPlaceRecipe implements Packet<INetHandlerPlayServer>
{
    private int field_194320_a;
    private IRecipe field_194321_b;
    private boolean field_194322_c;

    public CPacketPlaceRecipe()
    {
    }

    @SideOnly(Side.CLIENT)
    public CPacketPlaceRecipe(int p_i47614_1_, IRecipe p_i47614_2_, boolean p_i47614_3_)
    {
        this.field_194320_a = p_i47614_1_;
        this.field_194321_b = p_i47614_2_;
        this.field_194322_c = p_i47614_3_;
    }

    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.field_194320_a = buf.readByte();
        this.field_194321_b = CraftingManager.getRecipeById(buf.readVarInt());
        this.field_194322_c = buf.readBoolean();
    }

    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeByte(this.field_194320_a);
        buf.writeVarInt(CraftingManager.getIDForRecipe(this.field_194321_b));
        buf.writeBoolean(this.field_194322_c);
    }

    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.func_194308_a(this);
    }

    public int func_194318_a()
    {
        return this.field_194320_a;
    }

    public IRecipe func_194317_b()
    {
        return this.field_194321_b;
    }

    public boolean func_194319_c()
    {
        return this.field_194322_c;
    }
}