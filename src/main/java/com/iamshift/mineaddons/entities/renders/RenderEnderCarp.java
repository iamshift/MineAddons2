package com.iamshift.mineaddons.entities.renders;

import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.entities.EntityEnderCarp;
import com.iamshift.mineaddons.entities.models.ModelEnderCarp;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderEnderCarp extends RenderLiving<EntityEnderCarp> 
{
	public RenderEnderCarp(RenderManager rendermanagerIn) 
	{
		super(rendermanagerIn, new ModelEnderCarp(), 0.5F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityEnderCarp entity) 
	{
		return new ResourceLocation(Refs.ID, "textures/entities/endercarp_" + (entity.getCarpSize() - 1) + ".png");
	}

	@Override
	protected void preRenderCallback(EntityEnderCarp entitylivingbaseIn, float partialTickTime) 
	{
		GlStateManager.scale(.5F * entitylivingbaseIn.getCarpSize(), .5F * entitylivingbaseIn.getCarpSize(), .5F * entitylivingbaseIn.getCarpSize());
		this.shadowSize = 0.25f * entitylivingbaseIn.getCarpSize();
	}
}
