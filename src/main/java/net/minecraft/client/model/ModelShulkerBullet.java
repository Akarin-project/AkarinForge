package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelShulkerBullet extends ModelBase
{
    public ModelRenderer renderer;

    public ModelShulkerBullet()
    {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.renderer = new ModelRenderer(this);
        this.renderer.setTextureOffset(0, 0).addBox(-4.0F, -4.0F, -1.0F, 8, 8, 2, 0.0F);
        this.renderer.setTextureOffset(0, 10).addBox(-1.0F, -4.0F, -4.0F, 2, 8, 8, 0.0F);
        this.renderer.setTextureOffset(20, 0).addBox(-4.0F, -1.0F, -4.0F, 8, 2, 8, 0.0F);
        this.renderer.setRotationPoint(0.0F, 0.0F, 0.0F);
    }

    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        this.renderer.render(scale);
    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        this.renderer.rotateAngleY = netHeadYaw * 0.017453292F;
        this.renderer.rotateAngleX = headPitch * 0.017453292F;
    }
}