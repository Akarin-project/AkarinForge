package net.minecraft.client.gui.chat;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class OverlayChatListener implements IChatListener
{
    private final Minecraft mc;

    public OverlayChatListener(Minecraft minecraftIn)
    {
        this.mc = minecraftIn;
    }

    public void say(ChatType chatTypeIn, ITextComponent message)
    {
        this.mc.ingameGUI.setOverlayMessage(message, false);
    }
}