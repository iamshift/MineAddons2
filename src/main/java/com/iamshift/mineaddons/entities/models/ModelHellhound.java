package com.iamshift.mineaddons.entities.models;

import com.iamshift.mineaddons.entities.EntityHellhound;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelHellhound extends ModelBase 
{
    public ModelRenderer hellhoundHeadMain;
    public ModelRenderer hellhoundBody;
    public ModelRenderer hellhoundLeg1;
    public ModelRenderer hellhoundLeg2;
    public ModelRenderer hellhoundLeg3;
    public ModelRenderer hellhoundLeg4;
    ModelRenderer hellhoundTail;
    ModelRenderer hellhoundMane;
    
    ModelRenderer hellhoundColar;
    ModelRenderer colarSpike0;
    ModelRenderer colarSpike1;
    ModelRenderer colarSpike2;
    ModelRenderer colarSpike3;
    ModelRenderer colarSpike4;
    ModelRenderer colarSpike5;
    ModelRenderer colarSpike6;
    ModelRenderer colarSpike7;

    public ModelHellhound()
    {
    	this.textureWidth = 64;
        this.textureHeight = 48;
        this.hellhoundHeadMain = new ModelRenderer(this, 0, 0);
        this.hellhoundHeadMain.addBox(-2.0F, -3.0F, -2.0F, 6, 6, 4, 0.0F);
        this.hellhoundHeadMain.setRotationPoint(-1.0F, 13.5F, -7.0F);
        this.hellhoundBody = new ModelRenderer(this, 18, 14);
        this.hellhoundBody.addBox(-3.0F, -2.0F, -3.0F, 6, 9, 6, 0.0F);
        this.hellhoundBody.setRotationPoint(0.0F, 14.0F, 2.0F);
        this.hellhoundMane = new ModelRenderer(this, 21, 0);
        this.hellhoundMane.addBox(-3.0F, -2.0F, -3.0F, 8, 5, 7, 0.0F);
        this.hellhoundMane.setRotationPoint(-1.0F, 14.0F, 2.0F);
        this.hellhoundLeg1 = new ModelRenderer(this, 0, 18);
        this.hellhoundLeg1.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        this.hellhoundLeg1.setRotationPoint(-2.5F, 16.0F, 7.0F);
        this.hellhoundLeg2 = new ModelRenderer(this, 0, 18);
        this.hellhoundLeg2.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        this.hellhoundLeg2.setRotationPoint(0.5F, 16.0F, 7.0F);
        this.hellhoundLeg3 = new ModelRenderer(this, 0, 18);
        this.hellhoundLeg3.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        this.hellhoundLeg3.setRotationPoint(-2.5F, 16.0F, -4.0F);
        this.hellhoundLeg4 = new ModelRenderer(this, 0, 18);
        this.hellhoundLeg4.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        this.hellhoundLeg4.setRotationPoint(0.5F, 16.0F, -4.0F);
        this.hellhoundTail = new ModelRenderer(this, 9, 18);
        this.hellhoundTail.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        this.hellhoundTail.setRotationPoint(-1.0F, 12.0F, 8.0F);
        this.hellhoundHeadMain.setTextureOffset(16, 14).addBox(-2.0F, -5.0F, 0.0F, 2, 2, 1, 0.0F);
        this.hellhoundHeadMain.setTextureOffset(16, 14).addBox(2.0F, -5.0F, 0.0F, 2, 2, 1, 0.0F);
        this.hellhoundHeadMain.setTextureOffset(0, 10).addBox(-0.5F, 0.0F, -5.0F, 3, 3, 4, 0.0F);
        
        this.hellhoundColar = new ModelRenderer(this, 0, 31);
        this.hellhoundColar.addBox(-2.0F, -3.0F, -3.0F, 6, 1, 7, 0.0F);
        this.hellhoundColar.setTextureOffset(1, 32).addBox(-2.5F, -3.0F, -2.5F, 7, 1, 6);
        this.hellhoundColar.setRotationPoint(-1.0F, 14.0F, -3.0F);
        
        this.colarSpike0 = new ModelRenderer(this, 33, 32);
        this.colarSpike0.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.colarSpike0.setRotationPoint(3.9F, -2.5F, 3.4F);
        
        this.colarSpike1 = new ModelRenderer(this, 33, 32);
        this.colarSpike1.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.colarSpike1.setRotationPoint(-1.9F, -2.5F, 3.4F);
        
        this.colarSpike2 = new ModelRenderer(this, 33, 32);
        this.colarSpike2.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.colarSpike2.setRotationPoint(3.9F, -2.5F, -2.4F);
        
        this.colarSpike3 = new ModelRenderer(this, 33, 32);
        this.colarSpike3.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.colarSpike3.setRotationPoint(-1.9F, -2.5F, -2.4F);
        
        this.colarSpike4 = new ModelRenderer(this, 33, 32);
        this.colarSpike4.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.colarSpike4.setRotationPoint(1.0F, -2.5F, 3.4F);
        
        this.colarSpike5 = new ModelRenderer(this, 33, 32);
        this.colarSpike5.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.colarSpike5.setRotationPoint(1.0F, -2.5F, -2.0F);
        
        this.colarSpike6 = new ModelRenderer(this, 33, 32);
        this.colarSpike6.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.colarSpike6.setRotationPoint(3.9F, -2.5F, 1.0F);
        
        this.colarSpike7 = new ModelRenderer(this, 33, 32);
        this.colarSpike7.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.colarSpike7.setRotationPoint(-1.9F, -2.5F, 1.0F);
        
        this.hellhoundColar.addChild(this.colarSpike0);
        this.hellhoundColar.addChild(this.colarSpike1);
        this.hellhoundColar.addChild(this.colarSpike2);
        this.hellhoundColar.addChild(this.colarSpike3);
        this.hellhoundColar.addChild(this.colarSpike4);
        this.hellhoundColar.addChild(this.colarSpike5);
        this.hellhoundColar.addChild(this.colarSpike6);
        this.hellhoundColar.addChild(this.colarSpike7);
    }
    
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);

        if (this.isChild)
        {
            float f = 2.0F;
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, 5.0F * scale, 2.0F * scale);
            this.hellhoundHeadMain.renderWithRotation(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
            this.hellhoundBody.render(scale);
            this.hellhoundLeg1.render(scale);
            this.hellhoundLeg2.render(scale);
            this.hellhoundLeg3.render(scale);
            this.hellhoundLeg4.render(scale);
            this.hellhoundTail.renderWithRotation(scale);
            this.hellhoundMane.render(scale);
            this.hellhoundColar.render(scale);
            GlStateManager.popMatrix();
        }
        else
        {
            this.hellhoundHeadMain.renderWithRotation(scale);
            this.hellhoundBody.render(scale);
            this.hellhoundLeg1.render(scale);
            this.hellhoundLeg2.render(scale);
            this.hellhoundLeg3.render(scale);
            this.hellhoundLeg4.render(scale);
            this.hellhoundTail.renderWithRotation(scale);
            this.hellhoundMane.render(scale);
            this.hellhoundColar.render(scale);
        }
    }

    public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime)
    {
        EntityHellhound entityhellhound = (EntityHellhound)entitylivingbaseIn;

        if (entityhellhound.isAngry())
        {
            this.hellhoundTail.rotateAngleY = 0.0F;
        }
        else
        {
            this.hellhoundTail.rotateAngleY = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        }

        if (entityhellhound.isSitting())
        {
            this.hellhoundMane.setRotationPoint(-1.0F, 16.0F, -3.0F);
            this.hellhoundMane.rotateAngleX = ((float)Math.PI * 2F / 5F);
            this.hellhoundMane.rotateAngleY = 0.0F;
            this.hellhoundColar.setRotationPoint(-1.0F, 16.0F, -3.0F);
            this.hellhoundColar.rotateAngleX = ((float)Math.PI * 2F / 5F);
            this.hellhoundColar.rotateAngleY = 0.0F;
            this.hellhoundBody.setRotationPoint(0.0F, 18.0F, 0.0F);
            this.hellhoundBody.rotateAngleX = ((float)Math.PI / 4F);
            this.hellhoundTail.setRotationPoint(-1.0F, 21.0F, 6.0F);
            this.hellhoundLeg1.setRotationPoint(-2.5F, 22.0F, 2.0F);
            this.hellhoundLeg1.rotateAngleX = ((float)Math.PI * 3F / 2F);
            this.hellhoundLeg2.setRotationPoint(0.5F, 22.0F, 2.0F);
            this.hellhoundLeg2.rotateAngleX = ((float)Math.PI * 3F / 2F);
            this.hellhoundLeg3.rotateAngleX = 5.811947F;
            this.hellhoundLeg3.setRotationPoint(-2.49F, 17.0F, -4.0F);
            this.hellhoundLeg4.rotateAngleX = 5.811947F;
            this.hellhoundLeg4.setRotationPoint(0.51F, 17.0F, -4.0F);
        }
        else
        {
            this.hellhoundBody.setRotationPoint(0.0F, 14.0F, 2.0F);
            this.hellhoundBody.rotateAngleX = ((float)Math.PI / 2F);
            this.hellhoundMane.setRotationPoint(-1.0F, 14.0F, -3.0F);
            this.hellhoundMane.rotateAngleX = this.hellhoundBody.rotateAngleX;
            this.hellhoundColar.setRotationPoint(-1.0F, 14.0F, -3.0F);
            this.hellhoundColar.rotateAngleX = this.hellhoundBody.rotateAngleX;
            this.hellhoundTail.setRotationPoint(-1.0F, 12.0F, 8.0F);
            this.hellhoundLeg1.setRotationPoint(-2.5F, 16.0F, 7.0F);
            this.hellhoundLeg2.setRotationPoint(0.5F, 16.0F, 7.0F);
            this.hellhoundLeg3.setRotationPoint(-2.5F, 16.0F, -4.0F);
            this.hellhoundLeg4.setRotationPoint(0.5F, 16.0F, -4.0F);
            this.hellhoundLeg1.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            this.hellhoundLeg2.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
            this.hellhoundLeg3.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
            this.hellhoundLeg4.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        }

        this.hellhoundHeadMain.rotateAngleZ = entityhellhound.getInterestedAngle(partialTickTime) + entityhellhound.getShakeAngle(partialTickTime, 0.0F);
        this.hellhoundMane.rotateAngleZ = entityhellhound.getShakeAngle(partialTickTime, -0.08F);
        this.hellhoundColar.rotateAngleZ = entityhellhound.getShakeAngle(partialTickTime, -0.08F);
        this.hellhoundBody.rotateAngleZ = entityhellhound.getShakeAngle(partialTickTime, -0.16F);
        this.hellhoundTail.rotateAngleZ = entityhellhound.getShakeAngle(partialTickTime, -0.2F);
    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        this.hellhoundHeadMain.rotateAngleX = headPitch * 0.017453292F;
        this.hellhoundHeadMain.rotateAngleY = netHeadYaw * 0.017453292F;
        this.hellhoundTail.rotateAngleX = ageInTicks;
        
        this.colarSpike0.rotateAngleX = 1.5707963267948966F;
        this.colarSpike0.rotateAngleY = 0.7853981633974483F;
        this.colarSpike1.rotateAngleX = 1.5707963267948966F;
        this.colarSpike1.rotateAngleY = -0.7853981633974483F;
        this.colarSpike2.rotateAngleX = -1.5707963267948966F;
        this.colarSpike2.rotateAngleY = -0.7853981633974483F;
        this.colarSpike3.rotateAngleX = -1.5707963267948966F;
        this.colarSpike3.rotateAngleY = 0.7853981633974483F;
        
        this.colarSpike4.rotateAngleX = 1.5707963267948966F;
        this.colarSpike4.rotateAngleY = 0.0F;
        this.colarSpike5.rotateAngleX = -1.5707963267948966F;
        this.colarSpike5.rotateAngleY = 0.0F;
        this.colarSpike6.rotateAngleX = 1.5707963267948966F;
        this.colarSpike6.rotateAngleY = 1.5707963267948966F;
        this.colarSpike7.rotateAngleX = 1.5707963267948966F;
        this.colarSpike7.rotateAngleY = -1.5707963267948966F;
    }
    
    private void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
{
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
