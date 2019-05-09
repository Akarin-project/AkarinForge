package net.minecraft.client.model;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TextureOffset
{
    public final int textureOffsetX;
    public final int textureOffsetY;

    public TextureOffset(int textureOffsetXIn, int textureOffsetYIn)
    {
        this.textureOffsetX = textureOffsetXIn;
        this.textureOffsetY = textureOffsetYIn;
    }
}