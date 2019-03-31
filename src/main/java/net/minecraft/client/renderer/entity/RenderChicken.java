package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelChicken;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderChicken extends RenderLiving<EntityChicken>
{
    private static final ResourceLocation CHICKEN_TEXTURES = new ResourceLocation("textures/entity/chicken.png");

    public RenderChicken(RenderManager p_i47211_1_)
    {
        super(p_i47211_1_, new ModelChicken(), 0.3F);
    }

    protected ResourceLocation getEntityTexture(EntityChicken entity)
    {
        return CHICKEN_TEXTURES;
    }

    protected float handleRotationFloat(EntityChicken livingBase, float partialTicks)
    {
        float f = livingBase.oFlap + (livingBase.wingRotation - livingBase.oFlap) * partialTicks;
        float f1 = livingBase.oFlapSpeed + (livingBase.destPos - livingBase.oFlapSpeed) * partialTicks;
        return (MathHelper.sin(f) + 1.0F) * f1;
    }
}