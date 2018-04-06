package com.iamshift.mineaddons.entities.renders;

import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.entities.EntityZlama;
import com.iamshift.mineaddons.entities.EntityiSheep;
import com.iamshift.mineaddons.entities.renders.layers.LayerZlamaDecor;

import net.minecraft.client.model.ModelLlama;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerLlamaDecor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderZlama extends RenderLiving<EntityZlama>
{
	public RenderZlama(RenderManager rendermanagerIn)
	{
		super(rendermanagerIn, new ModelLlama(0.0F), 0.7F);
		this.addLayer(new LayerZlamaDecor(this));
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityZlama entity)
	{
		return new ResourceLocation(Refs.ID, "textures/entities/zlama.png");
	}
}
