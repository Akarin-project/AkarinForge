package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.entity.layers.LayerStrayClothing;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderStray extends RenderSkeleton
{
    private static final ResourceLocation STRAY_SKELETON_TEXTURES = new ResourceLocation("textures/entity/skeleton/stray.png");

    public RenderStray(RenderManager p_i47191_1_)
    {
        super(p_i47191_1_);
        this.addLayer(new LayerStrayClothing(this));
    }

    protected ResourceLocation getEntityTexture(AbstractSkeleton entity)
    {
        return STRAY_SKELETON_TEXTURES;
    }
}