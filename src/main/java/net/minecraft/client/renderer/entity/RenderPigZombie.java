package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderPigZombie extends RenderBiped<EntityPigZombie>
{
    private static final ResourceLocation ZOMBIE_PIGMAN_TEXTURE = new ResourceLocation("textures/entity/zombie_pigman.png");

    public RenderPigZombie(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelZombie(), 0.5F);
        this.addLayer(new LayerBipedArmor(this)
        {
            protected void initArmor()
            {
                this.modelLeggings = new ModelZombie(0.5F, true);
                this.modelArmor = new ModelZombie(1.0F, true);
            }
        });
    }

    protected ResourceLocation getEntityTexture(EntityPigZombie entity)
    {
        return ZOMBIE_PIGMAN_TEXTURE;
    }
}