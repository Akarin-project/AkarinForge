package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiErrorScreen extends GuiScreen
{
    private final String title;
    private final String message;

    public GuiErrorScreen(String titleIn, String messageIn)
    {
        this.title = titleIn;
        this.message = messageIn;
    }

    public void initGui()
    {
        super.initGui();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, 140, I18n.format("gui.cancel")));
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawGradientRect(0, 0, this.width, this.height, -12574688, -11530224);
        this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, 90, 16777215);
        this.drawCenteredString(this.fontRenderer, this.message, this.width / 2, 110, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
    }

    protected void actionPerformed(GuiButton button) throws IOException
    {
        this.mc.displayGuiScreen((GuiScreen)null);
    }
}