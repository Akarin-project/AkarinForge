package net.minecraft.client.gui;

import net.minecraft.util.IProgressUpdate;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiScreenWorking extends GuiScreen implements IProgressUpdate
{
    private String title = "";
    private String stage = "";
    private int progress;
    private boolean doneWorking;

    public void displaySavingString(String message)
    {
        this.resetProgressAndMessage(message);
    }

    public void resetProgressAndMessage(String message)
    {
        this.title = message;
        this.displayLoadingString("Working...");
    }

    public void displayLoadingString(String message)
    {
        this.stage = message;
        this.setLoadingProgress(0);
    }

    public void setLoadingProgress(int progress)
    {
        this.progress = progress;
    }

    public void setDoneWorking()
    {
        this.doneWorking = true;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        if (this.doneWorking)
        {
            if (!this.mc.isConnectedToRealms())
            {
                this.mc.displayGuiScreen((GuiScreen)null);
            }
        }
        else
        {
            this.drawDefaultBackground();
            this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, 70, 16777215);
            this.drawCenteredString(this.fontRenderer, this.stage + " " + this.progress + "%", this.width / 2, 90, 16777215);
            super.drawScreen(mouseX, mouseY, partialTicks);
        }
    }
}