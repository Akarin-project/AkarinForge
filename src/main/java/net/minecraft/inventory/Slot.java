package net.minecraft.inventory;

import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Slot
{
    private final int slotIndex;
    public final IInventory inventory;
    public int slotNumber;
    public int xPos;
    public int yPos;

    public Slot(IInventory inventoryIn, int index, int xPosition, int yPosition)
    {
        this.inventory = inventoryIn;
        this.slotIndex = index;
        this.xPos = xPosition;
        this.yPos = yPosition;
    }

    public void onSlotChange(ItemStack p_75220_1_, ItemStack p_75220_2_)
    {
        int i = p_75220_2_.getCount() - p_75220_1_.getCount();

        if (i > 0)
        {
            this.onCrafting(p_75220_2_, i);
        }
    }

    protected void onCrafting(ItemStack stack, int amount)
    {
    }

    protected void onSwapCraft(int p_190900_1_)
    {
    }

    protected void onCrafting(ItemStack stack)
    {
    }

    public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack)
    {
        this.onSlotChanged();
        return stack;
    }

    public boolean isItemValid(ItemStack stack)
    {
        return true;
    }

    public ItemStack getStack()
    {
        return this.inventory.getStackInSlot(this.slotIndex);
    }

    public boolean getHasStack()
    {
        return !this.getStack().isEmpty();
    }

    public void putStack(ItemStack stack)
    {
        this.inventory.setInventorySlotContents(this.slotIndex, stack);
        this.onSlotChanged();
    }

    public void onSlotChanged()
    {
        this.inventory.markDirty();
    }

    public int getSlotStackLimit()
    {
        return this.inventory.getInventoryStackLimit();
    }

    public int getItemStackLimit(ItemStack stack)
    {
        return this.getSlotStackLimit();
    }

    @Nullable
    @SideOnly(Side.CLIENT)
    public String getSlotTexture()
    {
        return backgroundName;
    }

    public ItemStack decrStackSize(int amount)
    {
        return this.inventory.decrStackSize(this.slotIndex, amount);
    }

    public boolean isHere(IInventory inv, int slotIn)
    {
        return inv == this.inventory && slotIn == this.slotIndex;
    }

    public boolean canTakeStack(EntityPlayer playerIn)
    {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public boolean isEnabled()
    {
        return true;
    }

    /*========================================= FORGE START =====================================*/
    protected String backgroundName = null;
    protected net.minecraft.util.ResourceLocation backgroundLocation = null;
    protected Object backgroundMap;
    /**
     * Gets the path of the texture file to use for the background image of this slot when drawing the GUI.
     * @return The resource location for the background image
     */
    @SideOnly(Side.CLIENT)
    public net.minecraft.util.ResourceLocation getBackgroundLocation()
    {
        return (backgroundLocation == null ? net.minecraft.client.renderer.texture.TextureMap.LOCATION_BLOCKS_TEXTURE : backgroundLocation);
    }

    /**
     * Sets the texture file to use for the background image of the slot when it's empty.
     * @param texture the resourcelocation for the texture
     */
    @SideOnly(Side.CLIENT)
    public void setBackgroundLocation(net.minecraft.util.ResourceLocation texture)
    {
        this.backgroundLocation = texture;
    }

    /**
     * Sets which icon index to use as the background image of the slot when it's empty.
     * @param name The icon to use, null for none
     */
    public void setBackgroundName(@Nullable String name)
    {
        this.backgroundName = name;
    }

    @Nullable
    @SideOnly(Side.CLIENT)
    public net.minecraft.client.renderer.texture.TextureAtlasSprite getBackgroundSprite()
    {
        String name = getSlotTexture();
        return name == null ? null : getBackgroundMap().getAtlasSprite(name);
    }

    @SideOnly(Side.CLIENT)
    protected net.minecraft.client.renderer.texture.TextureMap getBackgroundMap()
    {
        if (backgroundMap == null) backgroundMap = net.minecraft.client.Minecraft.getMinecraft().getTextureMapBlocks();
        return (net.minecraft.client.renderer.texture.TextureMap)backgroundMap;
    }

    /**
     * Retrieves the index in the inventory for this slot, this value should typically not
     * be used, but can be useful for some occasions.
     *
     * @return Index in associated inventory for this slot.
     */
    public int getSlotIndex()
    {
        return slotIndex;
    }

    /**
     * Checks if the other slot is in the same inventory, by comparing the inventory reference.
     * @param other
     * @return true if the other slot is in the same inventory
     */
    public boolean isSameInventory(Slot other)
    {
        return this.inventory == other.inventory;
    }
    /*========================================= FORGE END =====================================*/
}