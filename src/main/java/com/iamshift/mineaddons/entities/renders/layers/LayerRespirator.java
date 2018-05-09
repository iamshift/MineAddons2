package com.iamshift.mineaddons.entities.renders.layers;

import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.entities.models.ModelRespirator;
import com.iamshift.mineaddons.init.ModEnchants;
import com.iamshift.mineaddons.items.ItemRespirator;

import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerRespirator implements LayerRenderer
{
	private static final ResourceLocation TEX = new ResourceLocation(Refs.ID, "textures/entities/respirator.png");
	protected final RenderLivingBase<?> renderPlayer;
	private final ModelRespirator respirator = new ModelRespirator();

	public LayerRespirator(RenderLivingBase<?> p_i47185_1_)
	{
		this.renderPlayer = p_i47185_1_;
	}

	@Override
	public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		ItemStack itemstack = entitylivingbaseIn.getItemStackFromSlot(EntityEquipmentSlot.HEAD);

		if(itemstack.getItem() instanceof ItemRespirator || (isRespiratorHelm(itemstack) && entitylivingbaseIn.isInWater()))
		{
			this.renderPlayer.bindTexture(TEX);
			this.respirator.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entitylivingbaseIn);
			this.respirator.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		}
	}

	@Override
	public boolean shouldCombineTextures()
	{
		return false;
	}

	private static boolean isRespiratorHelm(ItemStack stack)
	{
		if(stack.getItem() instanceof ItemArmor && ((ItemArmor)stack.getItem()).armorType == EntityEquipmentSlot.HEAD)
		{
			if(stack.hasTagCompound() && stack.getTagCompound().hasKey("Respirator"))
				stack = convertHelmet(stack);
				
			return EnchantmentHelper.getEnchantments(stack).containsKey(ModEnchants.respirator);
		}

		return false;
	}
	
	private static ItemStack convertHelmet(ItemStack stack)
	{
		stack.removeSubCompound("Respirator");
		stack.addEnchantment(ModEnchants.respirator, 1);
		
		return stack;
	}
}
