package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelVex;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderVex extends RenderBiped<EntityVex>
{
    private static final ResourceLocation VEX_TEXTURE = new ResourceLocation("textures/entity/illager/vex.png");
    private static final ResourceLocation VEX_CHARGING_TEXTURE = new ResourceLocation("textures/entity/illager/vex_charging.png");
    private int modelVersion;

    public RenderVex(RenderManager p_i47190_1_)
    {
        super(p_i47190_1_, new ModelVex(), 0.3F);
        this.modelVersion = ((ModelVex)this.mainModel).getModelVersion();
    }

    protected ResourceLocation getEntityTexture(EntityVex entity)
    {
        return entity.isCharging() ? VEX_CHARGING_TEXTURE : VEX_TEXTURE;
    }

    public void doRender(EntityVex entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        int i = ((ModelVex)this.mainModel).getModelVersion();

        if (i != this.modelVersion)
        {
            this.mainModel = new ModelVex();
            this.modelVersion = i;
        }

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected void preRenderCallback(EntityVex entitylivingbaseIn, float partialTickTime)
    {
        GlStateManager.scale(0.4F, 0.4F, 0.4F);
    }
}