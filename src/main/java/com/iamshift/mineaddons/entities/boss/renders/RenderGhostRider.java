package com.iamshift.mineaddons.entities.boss.renders;

import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.entities.boss.EntityGhostRider;
import com.iamshift.mineaddons.entities.boss.models.ModelGhostRider;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderGhostRider extends RenderBiped<EntityGhostRider>
{
	private static final ResourceLocation TEX = new ResourceLocation(Refs.ID, "textures/entities/ghost_rider.png");
	
	private static final float MAX_SCALE = 3F;
	
	public RenderGhostRider(RenderManager renderManagerIn)
	{
		super(renderManagerIn, new ModelGhostRider(), 0.5F);
		this.addLayer(new LayerGhostRiderClothing(this));
		this.addLayer(new LayerHeldItem(this));
	}
	
	@Override
	public void transformHeldFull3DItemLayer()
	{
		GlStateManager.translate(0.09375F, 0.1875F, 0.0F);
	}
	
	@Override
	protected void preRenderCallback(EntityGhostRider entity, float partialTickTime)
	{
		if(entity.getStage() == 3)
		{
			if(entity.getScale() < MAX_SCALE)
				entity.incScale();
		}
		
		GlStateManager.scale(entity.getScale(), entity.getScale(), entity.getScale());
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityGhostRider entity)
	{
		return TEX;
	}
}
