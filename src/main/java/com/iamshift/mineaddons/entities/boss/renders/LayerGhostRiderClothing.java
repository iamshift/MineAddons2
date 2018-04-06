package com.iamshift.mineaddons.entities.boss.renders;

import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.entities.boss.EntityGhostRider;
import com.iamshift.mineaddons.entities.boss.models.ModelGhostRider;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerGhostRiderClothing implements LayerRenderer<EntityGhostRider>
{
	private static final ResourceLocation CLOTHES = new ResourceLocation(Refs.ID, "textures/entities/ghost_rider_clothes.png");
	private static final ResourceLocation ARMOR = new ResourceLocation(Refs.ID, "textures/entities/ghost_rider_invul.png");
	private final RenderLivingBase<?> renderer;
    private final ModelGhostRider layerModel = new ModelGhostRider(0.25F, true);

	public LayerGhostRiderClothing(RenderLivingBase<?> render)
	{
		this.renderer = render;
	}
	
	@Override
	public void doRenderLayer(EntityGhostRider entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		this.layerModel.setModelAttributes(this.renderer.getMainModel());
        this.layerModel.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        
        int i = entitylivingbaseIn.getInvulTime();
        if((i > 0 && (i > 80 || i / 5 % 2 != 1)) || entitylivingbaseIn.getStage() == 1)
        	this.renderer.bindTexture(ARMOR);
        else
        	this.renderer.bindTexture(CLOTHES);
        
        this.layerModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
	}

	@Override
	public boolean shouldCombineTextures()
	{
		return true;
	}

}
