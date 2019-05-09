package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class GuiScreenServerList extends GuiScreen
{
    private final GuiScreen lastScreen;
    private final ServerData serverData;
    private GuiTextField ipEdit;

    public GuiScreenServerList(GuiScreen lastScreenIn, ServerData serverDataIn)
    {
        this.lastScreen = lastScreenIn;
        this.serverData = serverDataIn;
    }

    public void updateScreen()
    {
        this.ipEdit.updateCursorCounter();
    }

    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, I18n.format("selectServer.select")));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, I18n.format("gui.cancel")));
        this.ipEdit = new GuiTextField(2, this.fontRenderer, this.width / 2 - 100, 116, 200, 20);
        this.ipEdit.setMaxStringLength(128);
        this.ipEdit.setFocused(true);
        this.ipEdit.setText(this.mc.gameSettings.lastServer);
        (this.buttonList.get(0)).enabled = !this.ipEdit.getText().isEmpty() && this.ipEdit.getText().split(":").length > 0;
    }

    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
        this.mc.gameSettings.lastServer = this.ipEdit.getText();
        this.mc.gameSettings.saveOptions();
    }

    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.enabled)
        {
            if (button.id == 1)
            {
                this.lastScreen.confirmClicked(false, 0);
            }
            else if (button.id == 0)
            {
                this.serverData.serverIP = this.ipEdit.getText();
                this.lastScreen.confirmClicked(true, 0);
            }
        }
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (this.ipEdit.textboxKeyTyped(typedChar, keyCode))
        {
            (this.buttonList.get(0)).enabled = !this.ipEdit.getText().isEmpty() && this.ipEdit.getText().split(":").length > 0;
        }
        else if (keyCode == 28 || keyCode == 156)
        {
            this.actionPerformed(this.buttonList.get(0));
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.ipEdit.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, I18n.format("selectServer.direct"), this.width / 2, 20, 16777215);
        this.drawString(this.fontRenderer, I18n.format("addServer.enterIp"), this.width / 2 - 100, 100, 10526880);
        this.ipEdit.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}