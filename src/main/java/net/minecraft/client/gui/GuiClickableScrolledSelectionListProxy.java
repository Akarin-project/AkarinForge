package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.realms.RealmsClickableScrolledSelectionList;
import net.minecraft.realms.Tezzelator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

@SideOnly(Side.CLIENT)
public class GuiClickableScrolledSelectionListProxy extends GuiSlot
{
    private final RealmsClickableScrolledSelectionList proxy;

    public GuiClickableScrolledSelectionListProxy(RealmsClickableScrolledSelectionList selectionList, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn)
    {
        super(Minecraft.getMinecraft(), widthIn, heightIn, topIn, bottomIn, slotHeightIn);
        this.proxy = selectionList;
    }

    protected int getSize()
    {
        return this.proxy.getItemCount();
    }

    protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY)
    {
        this.proxy.selectItem(slotIndex, isDoubleClick, mouseX, mouseY);
    }

    protected boolean isSelected(int slotIndex)
    {
        return this.proxy.isSelectedItem(slotIndex);
    }

    protected void drawBackground()
    {
        this.proxy.renderBackground();
    }

    protected void drawSlot(int slotIndex, int xPos, int yPos, int heightIn, int mouseXIn, int mouseYIn, float partialTicks)
    {
        this.proxy.renderItem(slotIndex, xPos, yPos, heightIn, mouseXIn, mouseYIn);
    }

    public int width()
    {
        return this.width;
    }

    public int mouseY()
    {
        return this.mouseY;
    }

    public int mouseX()
    {
        return this.mouseX;
    }

    protected int getContentHeight()
    {
        return this.proxy.getMaxPosition();
    }

    protected int getScrollBarX()
    {
        return this.proxy.getScrollbarPosition();
    }

    public void handleMouseInput()
    {
        super.handleMouseInput();

        if (this.scrollMultiplier > 0.0F && Mouse.getEventButtonState())
        {
            this.proxy.customMouseEvent(this.top, this.bottom, this.headerPadding, this.amountScrolled, this.slotHeight);
        }
    }

    public void renderSelected(int p_178043_1_, int p_178043_2_, int p_178043_3_, Tezzelator p_178043_4_)
    {
        this.proxy.renderSelected(p_178043_1_, p_178043_2_, p_178043_3_, p_178043_4_);
    }

    protected void drawSelectionBox(int insideLeft, int insideTop, int mouseXIn, int mouseYIn, float partialTicks)
    {
        int i = this.getSize();

        for (int j = 0; j < i; ++j)
        {
            int k = insideTop + j * this.slotHeight + this.headerPadding;
            int l = this.slotHeight - 4;

            if (k > this.bottom || k + l < this.top)
            {
                this.updateItemPos(j, insideLeft, k, partialTicks);
            }

            if (this.showSelectionBox && this.isSelected(j))
            {
                this.renderSelected(this.width, k, l, Tezzelator.instance);
            }

            this.drawSlot(j, insideLeft, k, l, mouseXIn, mouseYIn, partialTicks);
        }
    }
}