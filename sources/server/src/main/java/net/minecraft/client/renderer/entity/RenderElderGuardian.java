package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderElderGuardian extends RenderGuardian
{
    private static final ResourceLocation GUARDIAN_ELDER_TEXTURE = new ResourceLocation("textures/entity/guardian_elder.png");

    public RenderElderGuardian(RenderManager p_i47209_1_)
    {
        super(p_i47209_1_);
    }

    protected void preRenderCallback(EntityGuardian entitylivingbaseIn, float partialTickTime)
    {
        GlStateManager.scale(2.35F, 2.35F, 2.35F);
    }

    protected ResourceLocation getEntityTexture(EntityGuardian entity)
    {
        return GUARDIAN_ELDER_TEXTURE;
    }
}