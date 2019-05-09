package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SPacketEntityEquipment implements Packet<INetHandlerPlayClient>
{
    private int entityID;
    private EntityEquipmentSlot equipmentSlot;
    private ItemStack itemStack = ItemStack.EMPTY;

    public SPacketEntityEquipment()
    {
    }

    public SPacketEntityEquipment(int entityIdIn, EntityEquipmentSlot equipmentSlotIn, ItemStack itemStackIn)
    {
        this.entityID = entityIdIn;
        this.equipmentSlot = equipmentSlotIn;
        this.itemStack = itemStackIn.copy();
    }

    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.entityID = buf.readVarInt();
        this.equipmentSlot = (EntityEquipmentSlot)buf.readEnumValue(EntityEquipmentSlot.class);
        this.itemStack = buf.readItemStack();
    }

    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarInt(this.entityID);
        buf.writeEnumValue(this.equipmentSlot);
        buf.writeItemStack(this.itemStack);
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleEntityEquipment(this);
    }

    @SideOnly(Side.CLIENT)
    public ItemStack getItemStack()
    {
        return this.itemStack;
    }

    @SideOnly(Side.CLIENT)
    public int getEntityID()
    {
        return this.entityID;
    }

    @SideOnly(Side.CLIENT)
    public EntityEquipmentSlot getEquipmentSlot()
    {
        return this.equipmentSlot;
    }
}