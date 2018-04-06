package com.iamshift.mineaddons.entities.renders.layers;

import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.entities.EntityiSheep;
import com.iamshift.mineaddons.entities.models.ModeliSheep1;
import com.iamshift.mineaddons.entities.renders.RenderiSheep;

import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayeriSheepWool implements LayerRenderer<EntityiSheep>
{
	private static final ResourceLocation TEXTURE = new ResourceLocation(Refs.ID, "textures/entities/isheep_fur.png");
    private final RenderiSheep sheepRenderer;
    private final ModeliSheep1 sheepModel = new ModeliSheep1();

    public LayeriSheepWool(RenderiSheep sheepRendererIn)
    {
        this.sheepRenderer = sheepRendererIn;
    }

    public void doRenderLayer(EntityiSheep entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        if (!entitylivingbaseIn.isSheared() && !entitylivingbaseIn.isInvisible())
        {
            this.sheepRenderer.bindTexture(TEXTURE);
            this.sheepModel.setModelAttributes(this.sheepRenderer.getMainModel());
            this.sheepModel.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
            this.sheepModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    public boolean shouldCombineTextures()
    {
        return true;
    }
}
