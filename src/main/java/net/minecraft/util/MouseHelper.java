package net.minecraft.util;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

@SideOnly(Side.CLIENT)
public class MouseHelper
{
    public int deltaX;
    public int deltaY;

    public void grabMouseCursor()
    {
        if (Boolean.parseBoolean(System.getProperty("fml.noGrab","false"))) return;
        Mouse.setGrabbed(true);
        this.deltaX = 0;
        this.deltaY = 0;
    }

    public void ungrabMouseCursor()
    {
        Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
        Mouse.setGrabbed(false);
    }

    public void mouseXYChange()
    {
        this.deltaX = Mouse.getDX();
        this.deltaY = Mouse.getDY();
    }
}