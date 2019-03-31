package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CPacketCreativeInventoryAction implements Packet<INetHandlerPlayServer>
{
    private int slotId;
    private ItemStack stack = ItemStack.EMPTY;

    public CPacketCreativeInventoryAction()
    {
    }

    @SideOnly(Side.CLIENT)
    public CPacketCreativeInventoryAction(int slotIdIn, ItemStack stackIn)
    {
        this.slotId = slotIdIn;
        this.stack = stackIn.copy();
    }

    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processCreativeInventoryAction(this);
    }

    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.slotId = buf.readShort();
        this.stack = buf.readItemStack();
    }

    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeShort(this.slotId);
        net.minecraftforge.common.util.PacketUtil.writeItemStackFromClientToServer(buf, this.stack);
    }

    public int getSlotId()
    {
        return this.slotId;
    }

    public ItemStack getStack()
    {
        return this.stack;
    }
}