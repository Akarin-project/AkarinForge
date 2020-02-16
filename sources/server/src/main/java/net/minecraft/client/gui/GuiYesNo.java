package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiYesNo extends GuiScreen
{
    protected GuiYesNoCallback parentScreen;
    protected String messageLine1;
    private final String messageLine2;
    private final List<String> listLines = Lists.<String>newArrayList();
    protected String confirmButtonText;
    protected String cancelButtonText;
    protected int parentButtonClickedId;
    private int ticksUntilEnable;

    public GuiYesNo(GuiYesNoCallback parentScreenIn, String messageLine1In, String messageLine2In, int parentButtonClickedIdIn)
    {
        this.parentScreen = parentScreenIn;
        this.messageLine1 = messageLine1In;
        this.messageLine2 = messageLine2In;
        this.parentButtonClickedId = parentButtonClickedIdIn;
        this.confirmButtonText = I18n.format("gui.yes");
        this.cancelButtonText = I18n.format("gui.no");
    }

    public GuiYesNo(GuiYesNoCallback parentScreenIn, String messageLine1In, String messageLine2In, String confirmButtonTextIn, String cancelButtonTextIn, int parentButtonClickedIdIn)
    {
        this.parentScreen = parentScreenIn;
        this.messageLine1 = messageLine1In;
        this.messageLine2 = messageLine2In;
        this.confirmButtonText = confirmButtonTextIn;
        this.cancelButtonText = cancelButtonTextIn;
        this.parentButtonClickedId = parentButtonClickedIdIn;
    }

    public void initGui()
    {
        this.buttonList.add(new GuiOptionButton(0, this.width / 2 - 155, this.height / 6 + 96, this.confirmButtonText));
        this.buttonList.add(new GuiOptionButton(1, this.width / 2 - 155 + 160, this.height / 6 + 96, this.cancelButtonText));
        this.listLines.clear();
        this.listLines.addAll(this.fontRenderer.listFormattedStringToWidth(this.messageLine2, this.width - 50));
    }

    protected void actionPerformed(GuiButton button) throws IOException
    {
        this.parentScreen.confirmClicked(button.id == 0, this.parentButtonClickedId);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, this.messageLine1, this.width / 2, 70, 16777215);
        int i = 90;

        for (String s : this.listLines)
        {
            this.drawCenteredString(this.fontRenderer, s, this.width / 2, i, 16777215);
            i += this.fontRenderer.FONT_HEIGHT;
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void setButtonDelay(int ticksUntilEnableIn)
    {
        this.ticksUntilEnable = ticksUntilEnableIn;

        for (GuiButton guibutton : this.buttonList)
        {
            guibutton.enabled = false;
        }
    }

    public void updateScreen()
    {
        super.updateScreen();

        if (--this.ticksUntilEnable == 0)
        {
            for (GuiButton guibutton : this.buttonList)
            {
                guibutton.enabled = true;
            }
        }
    }
}