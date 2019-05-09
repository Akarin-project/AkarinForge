package net.minecraft.client.gui;

import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiGameOver extends GuiScreen
{
    private int enableButtonsTimer;
    private final ITextComponent causeOfDeath;

    public GuiGameOver(@Nullable ITextComponent causeOfDeathIn)
    {
        this.causeOfDeath = causeOfDeathIn;
    }

    public void initGui()
    {
        this.buttonList.clear();
        this.enableButtonsTimer = 0;

        if (this.mc.world.getWorldInfo().isHardcoreModeEnabled())
        {
            this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 72, I18n.format("deathScreen.spectate")));
            this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, I18n.format("deathScreen." + (this.mc.isIntegratedServerRunning() ? "deleteWorld" : "leaveServer"))));
        }
        else
        {
            this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 72, I18n.format("deathScreen.respawn")));
            this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, I18n.format("deathScreen.titleScreen")));

            if (this.mc.getSession() == null)
            {
                (this.buttonList.get(1)).enabled = false;
            }
        }

        for (GuiButton guibutton : this.buttonList)
        {
            guibutton.enabled = false;
        }
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
    }

    protected void actionPerformed(GuiButton button) throws IOException
    {
        switch (button.id)
        {
            case 0:
                this.mc.player.respawnPlayer();
                this.mc.displayGuiScreen((GuiScreen)null);
                break;
            case 1:

                if (this.mc.world.getWorldInfo().isHardcoreModeEnabled())
                {
                    this.mc.displayGuiScreen(new GuiMainMenu());
                }
                else
                {
                    GuiYesNo guiyesno = new GuiYesNo(this, I18n.format("deathScreen.quit.confirm"), "", I18n.format("deathScreen.titleScreen"), I18n.format("deathScreen.respawn"), 0);
                    this.mc.displayGuiScreen(guiyesno);
                    guiyesno.setButtonDelay(20);
                }
        }
    }

    public void confirmClicked(boolean result, int id)
    {
        if (result)
        {
            if (this.mc.world != null)
            {
                this.mc.world.sendQuittingDisconnectingPacket();
            }

            this.mc.loadWorld((WorldClient)null);
            this.mc.displayGuiScreen(new GuiMainMenu());
        }
        else
        {
            this.mc.player.respawnPlayer();
            this.mc.displayGuiScreen((GuiScreen)null);
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        boolean flag = this.mc.world.getWorldInfo().isHardcoreModeEnabled();
        this.drawGradientRect(0, 0, this.width, this.height, 1615855616, -1602211792);
        GlStateManager.pushMatrix();
        GlStateManager.scale(2.0F, 2.0F, 2.0F);
        this.drawCenteredString(this.fontRenderer, I18n.format(flag ? "deathScreen.title.hardcore" : "deathScreen.title"), this.width / 2 / 2, 30, 16777215);
        GlStateManager.popMatrix();

        if (this.causeOfDeath != null)
        {
            this.drawCenteredString(this.fontRenderer, this.causeOfDeath.getFormattedText(), this.width / 2, 85, 16777215);
        }

        this.drawCenteredString(this.fontRenderer, I18n.format("deathScreen.score") + ": " + TextFormatting.YELLOW + this.mc.player.getScore(), this.width / 2, 100, 16777215);

        if (this.causeOfDeath != null && mouseY > 85 && mouseY < 85 + this.fontRenderer.FONT_HEIGHT)
        {
            ITextComponent itextcomponent = this.getClickedComponentAt(mouseX);

            if (itextcomponent != null && itextcomponent.getStyle().getHoverEvent() != null)
            {
                this.handleComponentHover(itextcomponent, mouseX, mouseY);
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Nullable
    public ITextComponent getClickedComponentAt(int p_184870_1_)
    {
        if (this.causeOfDeath == null)
        {
            return null;
        }
        else
        {
            int i = this.mc.fontRenderer.getStringWidth(this.causeOfDeath.getFormattedText());
            int j = this.width / 2 - i / 2;
            int k = this.width / 2 + i / 2;
            int l = j;

            if (p_184870_1_ >= j && p_184870_1_ <= k)
            {
                for (ITextComponent itextcomponent : this.causeOfDeath)
                {
                    l += this.mc.fontRenderer.getStringWidth(GuiUtilRenderComponents.removeTextColorsIfConfigured(itextcomponent.getUnformattedComponentText(), false));

                    if (l > p_184870_1_)
                    {
                        return itextcomponent;
                    }
                }

                return null;
            }
            else
            {
                return null;
            }
        }
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }

    public void updateScreen()
    {
        super.updateScreen();
        ++this.enableButtonsTimer;

        if (this.enableButtonsTimer == 20)
        {
            for (GuiButton guibutton : this.buttonList)
            {
                guibutton.enabled = true;
            }
        }
    }
}