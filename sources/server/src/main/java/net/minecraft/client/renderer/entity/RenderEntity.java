package net.minecraft.client.renderer.entity;

import javax.annotation.Nullable;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderEntity extends Render<Entity>
{
    public RenderEntity(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    public void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        GlStateManager.pushMatrix();
        renderOffsetAABB(entity.getEntityBoundingBox(), x - entity.lastTickPosX, y - entity.lastTickPosY, z - entity.lastTickPosZ);
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Nullable
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return null;
    }
}