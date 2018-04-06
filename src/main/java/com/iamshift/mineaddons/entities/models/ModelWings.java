package com.iamshift.mineaddons.entities.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelWings extends ModelBase
{
	protected ModelRenderer leftWing;
	protected ModelRenderer rightWing;

	public ModelWings()
	{
		this.textureWidth = 64;
		this.textureHeight = 32;

		this.leftWing = new ModelRenderer(this, 38, 2);
		this.leftWing.addBox(1.0F, 0.0F, -2.0F, 10, 2, 1);
		this.leftWing.addBox(11.0F, 1.0F, -2.0F, 7, 2, 1).setTextureOffset(20, 2);
		this.leftWing.addBox(4.0F, 2.0F, -2.0F, 7, 1, 1).setTextureOffset(38, 6);
		this.leftWing.addBox(18.0F, 1.0F, -2.0F, 1, 1, 1).setTextureOffset(14, 2);
		this.leftWing.addBox(6.0F, 3.0F, -2.0F, 9, 1, 1).setTextureOffset(16, 6); //shape5
		this.leftWing.addBox(6.0F, 4.0F, -2.0F, 2, 1, 1).setTextureOffset(38, 9);
		this.leftWing.addBox(5.0F, 5.0F, -2.0F, 2, 1, 1).setTextureOffset(38, 12);
		this.leftWing.addBox(4.0F, 6.0F, -2.0F, 2, 1, 1).setTextureOffset(38, 15);
		this.leftWing.addBox(5.0F, 7.0F, -2.0F, 1, 1, 1).setTextureOffset(38, 18);
		this.leftWing.addBox(9.0F, 4.0F, -2.0F, 7, 1, 1).setTextureOffset(18, 9); //shape10
		this.leftWing.addBox(11.0F, 5.0F, -2.0F, 5, 1, 1).setTextureOffset(17, 12);
		this.leftWing.addBox(11.0F, 6.0F, -2.0F, 2, 1, 1).setTextureOffset(27, 15);
		this.leftWing.addBox(12.0F, 7.0F, -2.0F, 1, 1, 1).setTextureOffset(27, 18);
		this.leftWing.addBox(14.0F, 6.0F, -2.0F, 3, 1, 1).setTextureOffset(15, 15);
		this.leftWing.addBox(15.0F, 7.0F, -2.0F, 3, 1, 1).setTextureOffset(13, 18); //shape15
		this.leftWing.addBox(16.0F, 8.0F, -2.0F, 2, 1, 1).setTextureOffset(13, 21);
		this.leftWing.addBox(16.0F, 9.0F, -2.0F, 3, 1, 1).setTextureOffset(11, 24);
		this.leftWing.addBox(17.0F, 10.0F, -2.0F, 2, 1, 1).setTextureOffset(11, 27);
		this.leftWing.addBox(18.0F, 11.0F, -2.0F, 2, 1, 1).setTextureOffset(10, 30);

		this.rightWing = new ModelRenderer(this, 38, 2);
		this.rightWing.mirror = true;
		this.rightWing.addBox(-1.0F, 0.0F, -2.0F, -10, 2, 1);
		this.rightWing.addBox(-11.0F, 1.0F, -2.0F, -7, 2, 1).setTextureOffset(20, 2);
		this.rightWing.addBox(-4.0F, 2.0F, -2.0F, -7, 1, 1).setTextureOffset(38, 6);
		this.rightWing.addBox(-18.0F, 1.0F, -2.0F, -1, 1, 1).setTextureOffset(14, 2);
		this.rightWing.addBox(-6.0F, 3.0F, -2.0F, -9, 1, 1).setTextureOffset(16, 6); //shape5
		this.rightWing.addBox(-6.0F, 4.0F, -2.0F, -2, 1, 1).setTextureOffset(38, 9);
		this.rightWing.addBox(-5.0F, 5.0F, -2.0F, -2, 1, 1).setTextureOffset(38, 12);
		this.rightWing.addBox(-4.0F, 6.0F, -2.0F, -2, 1, 1).setTextureOffset(38, 15);
		this.rightWing.addBox(-5.0F, 7.0F, -2.0F, -1, 1, 1).setTextureOffset(38, 18);
		this.rightWing.addBox(-9.0F, 4.0F, -2.0F, -7, 1, 1).setTextureOffset(18, 9); //shape10
		this.rightWing.addBox(-11.0F, 5.0F, -2.0F, -5, 1, 1).setTextureOffset(17, 12);
		this.rightWing.addBox(-11.0F, 6.0F, -2.0F, -2, 1, 1).setTextureOffset(27, 15);
		this.rightWing.addBox(-12.0F, 7.0F, -2.0F, -1, 1, 1).setTextureOffset(27, 18);
		this.rightWing.addBox(-14.0F, 6.0F, -2.0F, -3, 1, 1).setTextureOffset(15, 15);
		this.rightWing.addBox(-15.0F, 7.0F, -2.0F, -3, 1, 1).setTextureOffset(13, 18); //shape15
		this.rightWing.addBox(-16.0F, 8.0F, -2.0F, -2, 1, 1).setTextureOffset(13, 21);
		this.rightWing.addBox(-16.0F, 9.0F, -2.0F, -3, 1, 1).setTextureOffset(11, 24);
		this.rightWing.addBox(-17.0F, 10.0F, -2.0F, -2, 1, 1).setTextureOffset(11, 27);
		this.rightWing.addBox(-18.0F, 11.0F, -2.0F, -2, 1, 1).setTextureOffset(10, 30);
	}

	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

		GlStateManager.disableRescaleNormal();
		GlStateManager.disableCull();

		if(entityIn instanceof EntityLivingBase && ((EntityLivingBase)entityIn).isChild())
		{
			GlStateManager.pushMatrix();
			GlStateManager.scale(0.5F, 0.5F, 0.5F);
			GlStateManager.translate(0.0F, 1.5F, -0.1F);
			this.rightWing.render(scale);
			this.leftWing.render(scale);
			GlStateManager.popMatrix();
		}
		else
		{
			this.rightWing.render(scale);
			this.leftWing.render(scale);
		}
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
	{
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);

		this.rightWing.rotationPointZ = 2.0F;
		this.leftWing.rotationPointZ = 2.0F;
		this.rightWing.rotationPointY = 1.0F;
		this.leftWing.rotationPointY = 1.0F;

		if(entityIn.isSneaking())
		{
			this.leftWing.setRotationPoint(0.0F, 3.5F, -0.5F);
			this.rightWing.setRotationPoint(0.0F, 3.5F, -0.5F);
			
			this.leftWing.rotateAngleZ = -0.87123894F;
			this.leftWing.rotateAngleX = 0.87123894F;
			this.rightWing.rotateAngleX = 0.87123894F;
			this.rightWing.rotateAngleZ = 0.87123894F;
		}
		else
		{
			this.leftWing.setRotationPoint(0.0F, 1.1F, 0.0F);
			this.rightWing.setRotationPoint(0.0F, 1.1F, 0.0F);
			
			if(entityIn instanceof EntityPlayer && ((EntityPlayer) entityIn).isElytraFlying())
				this.rightWing.rotateAngleY = 0.47123894F + MathHelper.cos(ageInTicks * 0.5F) * (float)Math.PI * 0.05F;
			else
				this.rightWing.rotateAngleY = 0.47123894F + MathHelper.cos(ageInTicks * 0.05F) * (float)Math.PI * 0.05F;

			this.leftWing.rotateAngleY = -this.rightWing.rotateAngleY;
			
			this.leftWing.rotateAngleZ = -0.47123894F;
			this.leftWing.rotateAngleX = 0.47123894F;
			this.rightWing.rotateAngleX = 0.47123894F;
			this.rightWing.rotateAngleZ = 0.47123894F;
		}
	}
}
