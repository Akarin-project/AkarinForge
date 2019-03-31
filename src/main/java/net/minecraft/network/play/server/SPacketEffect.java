package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SPacketEffect implements Packet<INetHandlerPlayClient>
{
    private int soundType;
    private BlockPos soundPos;
    private int soundData;
    private boolean serverWide;

    public SPacketEffect()
    {
    }

    public SPacketEffect(int soundTypeIn, BlockPos soundPosIn, int soundDataIn, boolean serverWideIn)
    {
        this.soundType = soundTypeIn;
        this.soundPos = soundPosIn;
        this.soundData = soundDataIn;
        this.serverWide = serverWideIn;
    }

    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.soundType = buf.readInt();
        this.soundPos = buf.readBlockPos();
        this.soundData = buf.readInt();
        this.serverWide = buf.readBoolean();
    }

    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeInt(this.soundType);
        buf.writeBlockPos(this.soundPos);
        buf.writeInt(this.soundData);
        buf.writeBoolean(this.serverWide);
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleEffect(this);
    }

    @SideOnly(Side.CLIENT)
    public boolean isSoundServerwide()
    {
        return this.serverWide;
    }

    @SideOnly(Side.CLIENT)
    public int getSoundType()
    {
        return this.soundType;
    }

    @SideOnly(Side.CLIENT)
    public int getSoundData()
    {
        return this.soundData;
    }

    @SideOnly(Side.CLIENT)
    public BlockPos getSoundPos()
    {
        return this.soundPos;
    }
}