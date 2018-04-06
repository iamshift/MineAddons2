package com.iamshift.mineaddons.entities.renders;

import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.entities.EntityBlazelier;

import net.minecraft.client.model.ModelBlaze;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBlazelier extends RenderLiving<EntityBlazelier>
{
	public RenderBlazelier(RenderManager renderManagerIn)
	{
		super(renderManagerIn, new ModelBlaze(), 0.5F);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityBlazelier entity)
	{
		return new ResourceLocation(Refs.ID, "textures/entities/blazelier.png");
	}
}
