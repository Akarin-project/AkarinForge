package net.minecraft.client.renderer.entity;

import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelEnderman;
import net.minecraft.client.renderer.entity.layers.LayerEndermanEyes;
import net.minecraft.client.renderer.entity.layers.LayerHeldBlock;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderEnderman extends RenderLiving<EntityEnderman>
{
    private static final ResourceLocation ENDERMAN_TEXTURES = new ResourceLocation("textures/entity/enderman/enderman.png");
    private final Random rnd = new Random();

    public RenderEnderman(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelEnderman(0.0F), 0.5F);
        this.addLayer(new LayerEndermanEyes(this));
        this.addLayer(new LayerHeldBlock(this));
    }

    public ModelEnderman getMainModel()
    {
        return (ModelEnderman)super.getMainModel();
    }

    public void doRender(EntityEnderman entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        IBlockState iblockstate = entity.getHeldBlockState();
        ModelEnderman modelenderman = this.getMainModel();
        modelenderman.isCarrying = iblockstate != null;
        modelenderman.isAttacking = entity.isScreaming();

        if (entity.isScreaming())
        {
            double d0 = 0.02D;
            x += this.rnd.nextGaussian() * 0.02D;
            z += this.rnd.nextGaussian() * 0.02D;
        }

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected ResourceLocation getEntityTexture(EntityEnderman entity)
    {
        return ENDERMAN_TEXTURES;
    }
}