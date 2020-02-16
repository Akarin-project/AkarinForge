package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelLeashKnot extends ModelBase
{
    public ModelRenderer knotRenderer;

    public ModelLeashKnot()
    {
        this(0, 0, 32, 32);
    }

    public ModelLeashKnot(int p_i46365_1_, int p_i46365_2_, int p_i46365_3_, int p_i46365_4_)
    {
        this.textureWidth = p_i46365_3_;
        this.textureHeight = p_i46365_4_;
        this.knotRenderer = new ModelRenderer(this, p_i46365_1_, p_i46365_2_);
        this.knotRenderer.addBox(-3.0F, -6.0F, -3.0F, 6, 8, 6, 0.0F);
        this.knotRenderer.setRotationPoint(0.0F, 0.0F, 0.0F);
    }

    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        this.knotRenderer.render(scale);
    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        this.knotRenderer.rotateAngleY = netHeadYaw * 0.017453292F;
        this.knotRenderer.rotateAngleX = headPitch * 0.017453292F;
    }
}