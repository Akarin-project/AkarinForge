package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiScreenRealmsProxy extends GuiScreen
{
    private final RealmsScreen proxy;

    public GuiScreenRealmsProxy(RealmsScreen proxyIn)
    {
        this.proxy = proxyIn;
        this.buttonList = Collections.<GuiButton>synchronizedList(Lists.newArrayList());
    }

    public RealmsScreen getProxy()
    {
        return this.proxy;
    }

    public void initGui()
    {
        this.proxy.init();
        super.initGui();
    }

    public void drawCenteredString(String text, int x, int y, int color)
    {
        super.drawCenteredString(this.fontRenderer, text, x, y, color);
    }

    public void drawString(String text, int x, int y, int color, boolean p_154322_5_)
    {
        if (p_154322_5_)
        {
            super.drawString(this.fontRenderer, text, x, y, color);
        }
        else
        {
            this.fontRenderer.drawString(text, x, y, color);
        }
    }

    public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height)
    {
        this.proxy.blit(x, y, textureX, textureY, width, height);
        super.drawTexturedModalRect(x, y, textureX, textureY, width, height);
    }

    public void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor)
    {
        super.drawGradientRect(left, top, right, bottom, startColor, endColor);
    }

    public void drawDefaultBackground()
    {
        super.drawDefaultBackground();
    }

    public boolean doesGuiPauseGame()
    {
        return super.doesGuiPauseGame();
    }

    public void drawWorldBackground(int tint)
    {
        super.drawWorldBackground(tint);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.proxy.render(mouseX, mouseY, partialTicks);
    }

    public void renderToolTip(ItemStack stack, int x, int y)
    {
        super.renderToolTip(stack, x, y);
    }

    public void drawHoveringText(String text, int x, int y)
    {
        super.drawHoveringText(text, x, y);
    }

    public void drawHoveringText(List<String> textLines, int x, int y)
    {
        super.drawHoveringText(textLines, x, y);
    }

    public void updateScreen()
    {
        this.proxy.tick();
        super.updateScreen();
    }

    public int getFontHeight()
    {
        return this.fontRenderer.FONT_HEIGHT;
    }

    public int getStringWidth(String text)
    {
        return this.fontRenderer.getStringWidth(text);
    }

    public void fontDrawShadow(String text, int x, int y, int color)
    {
        this.fontRenderer.drawStringWithShadow(text, (float)x, (float)y, color);
    }

    public List<String> fontSplit(String text, int wrapWidth)
    {
        return this.fontRenderer.listFormattedStringToWidth(text, wrapWidth);
    }

    public final void actionPerformed(GuiButton button) throws IOException
    {
        this.proxy.buttonClicked(((GuiButtonRealmsProxy)button).getRealmsButton());
    }

    public void buttonsClear()
    {
        this.buttonList.clear();
    }

    public void buttonsAdd(RealmsButton button)
    {
        this.buttonList.add(button.getProxy());
    }

    public List<RealmsButton> buttons()
    {
        List<RealmsButton> list = Lists.<RealmsButton>newArrayListWithExpectedSize(this.buttonList.size());

        for (GuiButton guibutton : this.buttonList)
        {
            list.add(((GuiButtonRealmsProxy)guibutton).getRealmsButton());
        }

        return list;
    }

    public void buttonsRemove(RealmsButton button)
    {
        this.buttonList.remove(button.getProxy());
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        this.proxy.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void handleMouseInput() throws IOException
    {
        this.proxy.mouseEvent();
        super.handleMouseInput();
    }

    public void handleKeyboardInput() throws IOException
    {
        this.proxy.keyboardEvent();
        super.handleKeyboardInput();
    }

    public void mouseReleased(int mouseX, int mouseY, int state)
    {
        this.proxy.mouseReleased(mouseX, mouseY, state);
    }

    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
    {
        this.proxy.mouseDragged(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    public void keyTyped(char typedChar, int keyCode) throws IOException
    {
        this.proxy.keyPressed(typedChar, keyCode);
    }

    public void confirmClicked(boolean result, int id)
    {
        this.proxy.confirmResult(result, id);
    }

    public void onGuiClosed()
    {
        this.proxy.removed();
        super.onGuiClosed();
    }
}