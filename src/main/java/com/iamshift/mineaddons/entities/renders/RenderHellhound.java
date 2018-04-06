package com.iamshift.mineaddons.entities.renders;

import java.util.List;

import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.entities.EntityHellhound;
import com.iamshift.mineaddons.entities.models.ModelHellhound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderHellhound extends RenderLiving<EntityHellhound>
{
	public RenderHellhound(RenderManager rendermanagerIn)
	{
		super(rendermanagerIn, new ModelHellhound(), 0.5F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityHellhound entity)
	{
		if(entity.isHidden() && !entity.isTamed())
			return new ResourceLocation(Refs.ID, "textures/entities/hellhound_hidden.png");
			
		if(entity.isTamed())
			return new ResourceLocation(Refs.ID, "textures/entities/hellhound_tamed.png");
		
		return new ResourceLocation(Refs.ID, "textures/entities/hellhound.png");
	}
	
	@Override
	protected void preRenderCallback(EntityHellhound entitylivingbaseIn, float partialTickTime)
	{
		GlStateManager.scale(1.5F, 1.5F, 1.5F);
		this.shadowSize = 1F;
	}
	
	@Override
	protected float handleRotationFloat(EntityHellhound livingBase, float partialTicks)
	{
		return livingBase.getTailRotation();
	}
	
	@Override
	public void doRender(EntityHellhound entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		if (entity.isWolfWet())
        {
            float f = entity.getBrightness() * entity.getShadingWhileWet(partialTicks);
            GlStateManager.color(f, f, f);
        }
		
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
}
