package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CPacketClickWindow implements Packet<INetHandlerPlayServer>
{
    private int windowId;
    private int slotId;
    private int packedClickData;
    private short actionNumber;
    private ItemStack clickedItem = ItemStack.EMPTY;
    private ClickType mode;

    public CPacketClickWindow()
    {
    }

    @SideOnly(Side.CLIENT)
    public CPacketClickWindow(int windowIdIn, int slotIdIn, int usedButtonIn, ClickType modeIn, ItemStack clickedItemIn, short actionNumberIn)
    {
        this.windowId = windowIdIn;
        this.slotId = slotIdIn;
        this.packedClickData = usedButtonIn;
        this.clickedItem = clickedItemIn.copy();
        this.actionNumber = actionNumberIn;
        this.mode = modeIn;
    }

    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processClickWindow(this);
    }

    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.windowId = buf.readByte();
        this.slotId = buf.readShort();
        this.packedClickData = buf.readByte();
        this.actionNumber = buf.readShort();
        this.mode = (ClickType)buf.readEnumValue(ClickType.class);
        this.clickedItem = buf.readItemStack();
    }

    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeByte(this.windowId);
        buf.writeShort(this.slotId);
        buf.writeByte(this.packedClickData);
        buf.writeShort(this.actionNumber);
        buf.writeEnumValue(this.mode);
        net.minecraftforge.common.util.PacketUtil.writeItemStackFromClientToServer(buf, this.clickedItem);
    }

    public int getWindowId()
    {
        return this.windowId;
    }

    public int getSlotId()
    {
        return this.slotId;
    }

    public int getUsedButton()
    {
        return this.packedClickData;
    }

    public short getActionNumber()
    {
        return this.actionNumber;
    }

    public ItemStack getClickedItem()
    {
        return this.clickedItem;
    }

    public ClickType getClickType()
    {
        return this.mode;
    }
}