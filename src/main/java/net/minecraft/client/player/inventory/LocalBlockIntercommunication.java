package net.minecraft.client.player.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LocalBlockIntercommunication implements IInteractionObject
{
    private final String guiID;
    private final ITextComponent displayName;

    public LocalBlockIntercommunication(String guiIdIn, ITextComponent displayNameIn)
    {
        this.guiID = guiIdIn;
        this.displayName = displayNameIn;
    }

    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        throw new UnsupportedOperationException();
    }

    public String getName()
    {
        return this.displayName.getUnformattedText();
    }

    public boolean hasCustomName()
    {
        return true;
    }

    public String getGuiID()
    {
        return this.guiID;
    }

    public ITextComponent getDisplayName()
    {
        return this.displayName;
    }
}