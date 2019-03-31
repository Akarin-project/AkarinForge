package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderWitherSkeleton extends RenderSkeleton
{
    private static final ResourceLocation WITHER_SKELETON_TEXTURES = new ResourceLocation("textures/entity/skeleton/wither_skeleton.png");

    public RenderWitherSkeleton(RenderManager p_i47188_1_)
    {
        super(p_i47188_1_);
    }

    protected ResourceLocation getEntityTexture(AbstractSkeleton entity)
    {
        return WITHER_SKELETON_TEXTURES;
    }

    protected void preRenderCallback(AbstractSkeleton entitylivingbaseIn, float partialTickTime)
    {
        GlStateManager.scale(1.2F, 1.2F, 1.2F);
    }
}