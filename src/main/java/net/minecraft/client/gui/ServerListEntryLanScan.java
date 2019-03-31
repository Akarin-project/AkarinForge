package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ServerListEntryLanScan implements GuiListExtended.IGuiListEntry
{
    private final Minecraft mc = Minecraft.getMinecraft();

    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks)
    {
        int i = y + slotHeight / 2 - this.mc.fontRenderer.FONT_HEIGHT / 2;
        this.mc.fontRenderer.drawString(I18n.format("lanServer.scanning"), this.mc.currentScreen.width / 2 - this.mc.fontRenderer.getStringWidth(I18n.format("lanServer.scanning")) / 2, i, 16777215);
        String s;

        switch ((int)(Minecraft.getSystemTime() / 300L % 4L))
        {
            case 0:
            default:
                s = "O o o";
                break;
            case 1:
            case 3:
                s = "o O o";
                break;
            case 2:
                s = "o o O";
        }

        this.mc.fontRenderer.drawString(s, this.mc.currentScreen.width / 2 - this.mc.fontRenderer.getStringWidth(s) / 2, i + this.mc.fontRenderer.FONT_HEIGHT, 8421504);
    }

    public void updatePosition(int slotIndex, int x, int y, float partialTicks)
    {
    }

    public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY)
    {
        return false;
    }

    public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY)
    {
    }
}