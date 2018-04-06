package com.iamshift.mineaddons.entities.renders;

import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.entities.EntityiSheep;
import com.iamshift.mineaddons.entities.models.ModeliSheep2;
import com.iamshift.mineaddons.entities.renders.layers.LayeriSheepWool;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderiSheep extends RenderLiving<EntityiSheep>
{
	public RenderiSheep(RenderManager rendermanagerIn)
	{
		super(rendermanagerIn, new ModeliSheep2(), 0.5F);
		this.addLayer(new LayeriSheepWool(this));
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityiSheep entity)
	{
		return new ResourceLocation(Refs.ID, "textures/entities/isheep.png");
	}
}
