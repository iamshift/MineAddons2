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
public class ModelWingsBAK extends ModelBase
{
	private final ModelRenderer rightWing;
    private final ModelRenderer leftWing = new ModelRenderer(this, 0, 32);
    
    public ModelWingsBAK()
	{
    	  this.rightWing = new ModelRenderer(this, 0, 32);
          this.rightWing.addBox(-20.0F, 0.0F, 0.0F, 20, 12, 1);
          this.leftWing.mirror = true;
          this.leftWing.addBox(0.0F, 0.0F, 0.0F, 20, 12, 1);
	}
    
    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
    	 GlStateManager.disableRescaleNormal();
         GlStateManager.disableCull();

         if (entityIn instanceof EntityLivingBase && ((EntityLivingBase)entityIn).isChild())
         {
             GlStateManager.pushMatrix();
             GlStateManager.scale(0.5F, 0.5F, 0.5F);
             GlStateManager.translate(0.0F, 1.5F, -0.1F);
             this.leftWing.render(scale);
             this.rightWing.render(scale);
             GlStateManager.popMatrix();
         }
         else
         {
             this.leftWing.render(scale);
             this.rightWing.render(scale);
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
        this.rightWing.rotateAngleY = 0.47123894F + MathHelper.cos(ageInTicks * 0.8F) * (float)Math.PI * 0.05F;
        this.leftWing.rotateAngleY = -this.rightWing.rotateAngleY;
        this.leftWing.rotateAngleZ = -0.47123894F;
        this.leftWing.rotateAngleX = 0.47123894F;
        this.rightWing.rotateAngleX = 0.47123894F;
        this.rightWing.rotateAngleZ = 0.47123894F;
    }
}
