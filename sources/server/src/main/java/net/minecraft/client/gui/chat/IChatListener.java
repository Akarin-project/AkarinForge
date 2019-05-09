package net.minecraft.client.gui.chat;

import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IChatListener
{
    void say(ChatType chatTypeIn, ITextComponent message);
}