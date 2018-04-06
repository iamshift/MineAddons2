package com.iamshift.mineaddons.entities.boss.renders;

import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.entities.boss.EntityDeadHorse;
import com.iamshift.mineaddons.entities.boss.EntityGhostRider;

import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderDeadHorse extends RenderLiving<EntityDeadHorse>
{
	private static final ResourceLocation TEX = new ResourceLocation(Refs.ID, "textures/entities/dead_horse.png");
	private static final ResourceLocation ARMOR = new ResourceLocation(Refs.ID, "textures/entities/dead_horse_invul.png");
	
	public RenderDeadHorse(RenderManager rendermanagerIn)
	{
		super(rendermanagerIn, new ModelHorse(), 1.0F);
	}

	@Override
	protected void preRenderCallback(EntityDeadHorse entitylivingbaseIn, float partialTickTime)
	{
		if(entitylivingbaseIn.isGhostHorse())
			GlStateManager.scale(1.5F, 1.5F, 1.5F);
		
		super.preRenderCallback(entitylivingbaseIn, partialTickTime);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityDeadHorse entity)
	{
		if(entity.isBeingRidden() && (entity.getPassengers().get(0) instanceof EntityGhostRider))
		{	
			int i = ((EntityGhostRider)entity.getPassengers().get(0)).getInvulTime();
			return i > 0 && (i > 80 || i / 5 % 2 != 1) ? ARMOR : TEX;
		}
		else
			return TEX;
	}
}
