package com.iamshift.mineaddons.entities.renders;

import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.entities.EntityAncientCarp;
import com.iamshift.mineaddons.entities.models.ModelAncientCarp;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderAncientCarp extends RenderLiving<EntityAncientCarp>
{
	public RenderAncientCarp(RenderManager rendermanagerIn) 
	{
		super(rendermanagerIn, new ModelAncientCarp(), 0.5F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityAncientCarp entity) 
	{
		return new ResourceLocation(Refs.ID, "textures/entities/ancientcarp.png");
	}
}
