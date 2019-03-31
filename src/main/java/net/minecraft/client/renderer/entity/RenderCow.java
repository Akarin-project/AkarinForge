package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelCow;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderCow extends RenderLiving<EntityCow>
{
    private static final ResourceLocation COW_TEXTURES = new ResourceLocation("textures/entity/cow/cow.png");

    public RenderCow(RenderManager p_i47210_1_)
    {
        super(p_i47210_1_, new ModelCow(), 0.7F);
    }

    protected ResourceLocation getEntityTexture(EntityCow entity)
    {
        return COW_TEXTURES;
    }
}