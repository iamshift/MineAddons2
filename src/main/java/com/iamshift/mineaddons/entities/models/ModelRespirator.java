package com.iamshift.mineaddons.entities.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelRespirator extends ModelBase
{
	private ModelRenderer modelhead;
	
	public ModelRespirator()
	{
		this.textureWidth = 16;
		this.textureHeight = 8;
		
		this.modelhead = new ModelRenderer(this, 0, 0);
		this.modelhead.addBox(-1.0F, -2.0F, -5.0F, 2, 1, 1, 0.0F);
		this.modelhead.addBox(-1.5F, -2.5F, -6.5F, 3, 2, 2, 0.0F);
		this.modelhead.addBox(-2.5F, -2.0F, -6.0F, 5, 1, 1, 0.0F);
	}
	
	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
		if(entityIn instanceof EntityLivingBase && ((EntityLivingBase)entityIn).isChild())
		{
			GlStateManager.pushMatrix();
			GlStateManager.scale(0.5F, 0.5F, 0.5F);
			GlStateManager.translate(0.0F, 1.5F, -0.1F);
			this.modelhead.render(scale);
			GlStateManager.popMatrix();
		}
		else
		{
			if (entityIn.isSneaking())
            {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }
			
			this.modelhead.render(scale);
		}
	}
	
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
	{
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
		
		boolean flag = entityIn instanceof EntityLivingBase && ((EntityLivingBase)entityIn).getTicksElytraFlying() > 4;
        this.modelhead.rotateAngleY = netHeadYaw * 0.017453292F;

        if (flag)
        {
            this.modelhead.rotateAngleX = -((float)Math.PI / 4F);
        }
        else
        {
            this.modelhead.rotateAngleX = headPitch * 0.017453292F;
        }
        
        float f = 1.0F;

        if (flag)
        {
            f = (float)(entityIn.motionX * entityIn.motionX + entityIn.motionY * entityIn.motionY + entityIn.motionZ * entityIn.motionZ);
            f = f / 0.2F;
            f = f * f * f;
        }

        if (f < 1.0F)
        {
            f = 1.0F;
        }

        float f3 = MathHelper.sin(this.swingProgress * (float)Math.PI) * -(this.modelhead.rotateAngleX - 0.7F) * 0.75F;
        
        if(entityIn.isSneaking())
        	this.modelhead.rotationPointY = 1.0F;
        else
        	this.modelhead.rotationPointY = 0.0F;
	}
}
