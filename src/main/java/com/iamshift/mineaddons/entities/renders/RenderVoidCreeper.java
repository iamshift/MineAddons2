package com.iamshift.mineaddons.entities.renders;

import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.entities.EntityVoidCreeper;

import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderVoidCreeper extends RenderLiving<EntityVoidCreeper>
{
	public RenderVoidCreeper(RenderManager rendermanagerIn) 
	{
		super(rendermanagerIn, new ModelCreeper(), 0.5F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityVoidCreeper entity) 
	{
		return new ResourceLocation(Refs.ID, "textures/entities/voidcreeper.png");
	}
}
