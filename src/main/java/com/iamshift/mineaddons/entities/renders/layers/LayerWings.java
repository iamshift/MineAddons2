package com.iamshift.mineaddons.entities.renders.layers;

import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.entities.models.ModelWings;
import com.iamshift.mineaddons.init.ModEnchants;
import com.iamshift.mineaddons.items.ItemWings;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerWings implements LayerRenderer
{
	private static final ResourceLocation TEX = new ResourceLocation(Refs.ID, "textures/entities/wings.png");
	protected final RenderLivingBase<?> renderPlayer;
	private final ModelWings modelWings = new ModelWings();
	
	public LayerWings(RenderLivingBase<?> p_i47185_1_) 
	{
		this.renderPlayer = p_i47185_1_;
	}

	public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		ItemStack itemstack = entitylivingbaseIn.getItemStackFromSlot(EntityEquipmentSlot.CHEST);

        if (EnchantmentHelper.getEnchantments(itemstack).containsKey(ModEnchants.wings) || itemstack.getItem() instanceof ItemWings)
        {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

            if (entitylivingbaseIn instanceof AbstractClientPlayer)
            {
                AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer)entitylivingbaseIn;

                if (abstractclientplayer.isPlayerInfoSet() && abstractclientplayer.getLocationElytra() != null)
                {
                    this.renderPlayer.bindTexture(abstractclientplayer.getLocationElytra());
                }
                else if (abstractclientplayer.hasPlayerInfo() && abstractclientplayer.getLocationCape() != null && abstractclientplayer.isWearing(EnumPlayerModelParts.CAPE))
                {
                    this.renderPlayer.bindTexture(abstractclientplayer.getLocationCape());
                }
                else
                {
                    this.renderPlayer.bindTexture(TEX);
                }
            }
            else
            {
                this.renderPlayer.bindTexture(TEX);
            }

            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, 0.0F, 0.125F);
            this.modelWings.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entitylivingbaseIn);
            this.modelWings.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

            if (itemstack.isItemEnchanted())
            {
                LayerArmorBase.renderEnchantedGlint(this.renderPlayer, entitylivingbaseIn, this.modelWings, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
            }

            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
	}
	
	@Override
	public boolean shouldCombineTextures()
	{
		return false;
	}
	
	private boolean checkEquip(EntityPlayer p)
	{
		if(p != null && p.getItemStackFromSlot(EntityEquipmentSlot.CHEST) != null)
		{
			ItemStack s = p.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
			
			if(s.getSubCompound("Wings") != null && s.getSubCompound("Wings").hasKey("enabled"))
				return true;
		}
		
		return false;
	}
}
