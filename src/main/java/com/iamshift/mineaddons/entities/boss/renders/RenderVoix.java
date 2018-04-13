package com.iamshift.mineaddons.entities.boss.renders;

import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.entities.boss.EntityVoix;
import com.iamshift.mineaddons.entities.boss.models.ModelVoix;
import com.iamshift.mineaddons.entities.renders.layers.LayerWings;

import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderVoix extends RenderBiped<EntityVoix>
{
	private static final ResourceLocation TEX = new ResourceLocation(Refs.ID, "textures/entities/voix.png");
	private static final ResourceLocation ARMOR = new ResourceLocation(Refs.ID, "textures/entities/voix_invul.png");
	
	public RenderVoix(RenderManager render)
	{
		super(render, new ModelVoix(), 1F);
		this.addLayer(new LayerWings(this));
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityVoix entity)
	{
		int i = entity.getInvulTime();
		return i > 0 && (i > 80 || i / 5 % 2 != 1) ? ARMOR : TEX;
	}
}
