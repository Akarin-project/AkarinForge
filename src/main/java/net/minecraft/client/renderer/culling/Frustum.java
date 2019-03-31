package net.minecraft.client.renderer.culling;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Frustum implements ICamera
{
    private final ClippingHelper clippingHelper;
    private double x;
    private double y;
    private double z;

    public Frustum()
    {
        this(ClippingHelperImpl.getInstance());
    }

    public Frustum(ClippingHelper clippingHelperIn)
    {
        this.clippingHelper = clippingHelperIn;
    }

    public void setPosition(double xIn, double yIn, double zIn)
    {
        this.x = xIn;
        this.y = yIn;
        this.z = zIn;
    }

    public boolean isBoxInFrustum(double p_78548_1_, double p_78548_3_, double p_78548_5_, double p_78548_7_, double p_78548_9_, double p_78548_11_)
    {
        return this.clippingHelper.isBoxInFrustum(p_78548_1_ - this.x, p_78548_3_ - this.y, p_78548_5_ - this.z, p_78548_7_ - this.x, p_78548_9_ - this.y, p_78548_11_ - this.z);
    }

    public boolean isBoundingBoxInFrustum(AxisAlignedBB p_78546_1_)
    {
        return this.isBoxInFrustum(p_78546_1_.minX, p_78546_1_.minY, p_78546_1_.minZ, p_78546_1_.maxX, p_78546_1_.maxY, p_78546_1_.maxZ);
    }
}