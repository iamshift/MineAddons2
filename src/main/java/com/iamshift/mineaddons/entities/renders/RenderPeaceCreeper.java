package com.iamshift.mineaddons.entities.renders;

import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.entities.EntityPeaceCreeper;
import com.iamshift.mineaddons.entities.models.ModelPeaceCreeper;
import com.iamshift.mineaddons.entities.renders.layers.LayerPeaceCreeperHeadband;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderPeaceCreeper extends RenderLiving<EntityPeaceCreeper>
{
    public RenderPeaceCreeper(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelPeaceCreeper(), 0.5F);
        this.addLayer(new LayerPeaceCreeperHeadband(this));
    }

	@Override
	protected ResourceLocation getEntityTexture(EntityPeaceCreeper entity) 
	{
		return new ResourceLocation(Refs.ID, "textures/entities/peacecreeper.png");
	}
}