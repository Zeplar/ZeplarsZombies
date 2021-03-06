package com.zeplar.zeplarszombies.Monsters;

import net.minecraft.client.model.ModelVillager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderInfestedVillager extends RenderLiving<EntityInfestedVillager> {

    private ResourceLocation mobTexture = new ResourceLocation("zeplarszombies:textures/entity/infested_villager.png");
    public static final Factory FACTORY = new Factory();

    public RenderInfestedVillager(RenderManager renderManagerIn)
        {
            super(renderManagerIn, new ModelVillager(0.0f), 0.5F);
        }

        /**
         * Allows the render to do state modifications necessary before the model is rendered.
         */
    protected void preRenderCallback(EntityInfestedVillager entitylivingbaseIn, float partialTickTime)
    {
        float f = entitylivingbaseIn.getInfestedVillagerFlashIntensity(partialTickTime);
        float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        f = f * f;
        f = f * f;
        float f2 = (0.9375F + f * 0.8F) * f1;
        float f3 = (0.9375F + f * 0.3F) / f1;
        GlStateManager.scale(f2, f3, f2);
    }

    /**
     * Gets an RGBA int color multiplier to apply.
     */
    protected int getColorMultiplier(EntityInfestedVillager entitylivingbaseIn, float lightBrightness, float partialTickTime)
    {
        float f = entitylivingbaseIn.getInfestedVillagerFlashIntensity(partialTickTime);

        if ((int)(f * 10.0F) % 2 == 0)
        {
            return 0;
        }
        else
        {
            int i = (int)(f * 0.2F * 255.0F);
            i = MathHelper.clamp(i, 0, 255);
            return i << 24 | 822083583;
        }
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityInfestedVillager entity)
    {
        return mobTexture;
    }

    public static class Factory implements IRenderFactory<EntityInfestedVillager> {

        @Override
        public Render<? super EntityInfestedVillager> createRenderFor(RenderManager manager) {
            return new RenderInfestedVillager(manager);
        }

    }
}
