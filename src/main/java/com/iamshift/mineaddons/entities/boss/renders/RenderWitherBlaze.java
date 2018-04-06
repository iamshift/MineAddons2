package com.iamshift.mineaddons.entities.boss.renders;

import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.entities.EntityHellhound;
import com.iamshift.mineaddons.entities.boss.EntityWitherBlaze;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBlaze;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderWitherBlaze extends RenderLiving<EntityWitherBlaze>
{
	private static final ResourceLocation TEX = new ResourceLocation(Refs.ID, "textures/entities/wither_blaze.png");
	private static final ResourceLocation ARMOR = new ResourceLocation(Refs.ID, "textures/entities/wither_blaze_invul.png");
	
	private static final float MAX_SCALE = 1.5F;
	
	public RenderWitherBlaze(RenderManager rendermanagerIn)
	{
		super(rendermanagerIn, new ModelBlaze(), 0.5F);
	}
	
	@Override
	protected void preRenderCallback(EntityWitherBlaze entity, float partialTickTime)
	{
		if(entity.getStage() == 3)
		{
			if(entity.getScale() > MAX_SCALE)
				entity.decrScale();
		}
		
		this.shadowSize = 2F;
		GlStateManager.scale(entity.getScale(), entity.getScale(), entity.getScale());
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityWitherBlaze entity)
	{
		int i = entity.getInvulTime();
		return i > 0 && (i > 80 || i / 5 % 2 != 1) ? ARMOR : TEX;
	}
	
}
