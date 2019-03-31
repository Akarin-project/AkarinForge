package net.minecraft.network.play.server;

import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SPacketSelectAdvancementsTab implements Packet<INetHandlerPlayClient>
{
    @Nullable
    private ResourceLocation tab;

    public SPacketSelectAdvancementsTab()
    {
    }

    public SPacketSelectAdvancementsTab(@Nullable ResourceLocation p_i47596_1_)
    {
        this.tab = p_i47596_1_;
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleSelectAdvancementsTab(this);
    }

    public void readPacketData(PacketBuffer buf) throws IOException
    {
        if (buf.readBoolean())
        {
            this.tab = buf.readResourceLocation();
        }
    }

    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeBoolean(this.tab != null);

        if (this.tab != null)
        {
            buf.writeResourceLocation(this.tab);
        }
    }

    @Nullable
    @SideOnly(Side.CLIENT)
    public ResourceLocation getTab()
    {
        return this.tab;
    }
}