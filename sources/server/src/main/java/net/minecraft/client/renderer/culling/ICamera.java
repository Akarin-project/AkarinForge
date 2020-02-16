package net.minecraft.client.renderer.culling;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface ICamera
{
    boolean isBoundingBoxInFrustum(AxisAlignedBB p_78546_1_);

    void setPosition(double xIn, double yIn, double zIn);
}