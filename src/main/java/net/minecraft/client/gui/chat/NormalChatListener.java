package net.minecraft.client.gui.chat;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class NormalChatListener implements IChatListener
{
    private final Minecraft mc;

    public NormalChatListener(Minecraft p_i47393_1_)
    {
        this.mc = p_i47393_1_;
    }

    public void say(ChatType chatTypeIn, ITextComponent message)
    {
        this.mc.ingameGUI.getChatGUI().printChatMessage(message);
    }
}