package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelBlaze extends ModelBase
{
    private final ModelRenderer[] blazeSticks = new ModelRenderer[12];
    private final ModelRenderer blazeHead;

    public ModelBlaze()
    {
        for (int i = 0; i < this.blazeSticks.length; ++i)
        {
            this.blazeSticks[i] = new ModelRenderer(this, 0, 16);
            this.blazeSticks[i].addBox(0.0F, 0.0F, 0.0F, 2, 8, 2);
        }

        this.blazeHead = new ModelRenderer(this, 0, 0);
        this.blazeHead.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8);
    }

    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        this.blazeHead.render(scale);

        for (ModelRenderer modelrenderer : this.blazeSticks)
        {
            modelrenderer.render(scale);
        }
    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        float f = ageInTicks * (float)Math.PI * -0.1F;

        for (int i = 0; i < 4; ++i)
        {
            this.blazeSticks[i].rotationPointY = -2.0F + MathHelper.cos(((float)(i * 2) + ageInTicks) * 0.25F);
            this.blazeSticks[i].rotationPointX = MathHelper.cos(f) * 9.0F;
            this.blazeSticks[i].rotationPointZ = MathHelper.sin(f) * 9.0F;
            ++f;
        }

        f = ((float)Math.PI / 4F) + ageInTicks * (float)Math.PI * 0.03F;

        for (int j = 4; j < 8; ++j)
        {
            this.blazeSticks[j].rotationPointY = 2.0F + MathHelper.cos(((float)(j * 2) + ageInTicks) * 0.25F);
            this.blazeSticks[j].rotationPointX = MathHelper.cos(f) * 7.0F;
            this.blazeSticks[j].rotationPointZ = MathHelper.sin(f) * 7.0F;
            ++f;
        }

        f = 0.47123894F + ageInTicks * (float)Math.PI * -0.05F;

        for (int k = 8; k < 12; ++k)
        {
            this.blazeSticks[k].rotationPointY = 11.0F + MathHelper.cos(((float)k * 1.5F + ageInTicks) * 0.5F);
            this.blazeSticks[k].rotationPointX = MathHelper.cos(f) * 5.0F;
            this.blazeSticks[k].rotationPointZ = MathHelper.sin(f) * 5.0F;
            ++f;
        }

        this.blazeHead.rotateAngleY = netHeadYaw * 0.017453292F;
        this.blazeHead.rotateAngleX = headPitch * 0.017453292F;
    }
}