package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelPolarBear;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderPolarBear extends RenderLiving<EntityPolarBear>
{
    private static final ResourceLocation POLAR_BEAR_TEXTURE = new ResourceLocation("textures/entity/bear/polarbear.png");

    public RenderPolarBear(RenderManager p_i47197_1_)
    {
        super(p_i47197_1_, new ModelPolarBear(), 0.7F);
    }

    protected ResourceLocation getEntityTexture(EntityPolarBear entity)
    {
        return POLAR_BEAR_TEXTURE;
    }

    public void doRender(EntityPolarBear entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected void preRenderCallback(EntityPolarBear entitylivingbaseIn, float partialTickTime)
    {
        GlStateManager.scale(1.2F, 1.2F, 1.2F);
        super.preRenderCallback(entitylivingbaseIn, partialTickTime);
    }
}