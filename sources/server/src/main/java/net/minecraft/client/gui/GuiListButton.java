package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiListButton extends GuiButton
{
    private boolean value;
    private final String localizationStr;
    private final GuiPageButtonList.GuiResponder guiResponder;

    public GuiListButton(GuiPageButtonList.GuiResponder responder, int buttonId, int x, int y, String localizationStrIn, boolean valueIn)
    {
        super(buttonId, x, y, 150, 20, "");
        this.localizationStr = localizationStrIn;
        this.value = valueIn;
        this.displayString = this.buildDisplayString();
        this.guiResponder = responder;
    }

    private String buildDisplayString()
    {
        return I18n.format(this.localizationStr) + ": " + I18n.format(this.value ? "gui.yes" : "gui.no");
    }

    public void setValue(boolean valueIn)
    {
        this.value = valueIn;
        this.displayString = this.buildDisplayString();
        this.guiResponder.setEntryValue(this.id, valueIn);
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        if (super.mousePressed(mc, mouseX, mouseY))
        {
            this.value = !this.value;
            this.displayString = this.buildDisplayString();
            this.guiResponder.setEntryValue(this.id, this.value);
            return true;
        }
        else
        {
            return false;
        }
    }
}