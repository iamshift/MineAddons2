package com.iamshift.mineaddons.utils;

import com.iamshift.mineaddons.init.ModEnchants;
import com.iamshift.mineaddons.items.ItemWings;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ElytraHelper
{
	public static ItemStack findChest(EntityLivingBase entity)
	{
		ItemStack stack = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		if(!stack.isEmpty() && (stack.getItem() instanceof ItemArmor && ((ItemArmor)stack.getItem()).armorType == EntityEquipmentSlot.CHEST) || stack.getItem() instanceof ItemWings)
			return stack;
		
		return ItemStack.EMPTY;
	}
	
	public static boolean isValid(ItemStack stack)
	{
		if(EnchantmentHelper.getEnchantments(stack).containsKey(ModEnchants.elytra) || EnchantmentHelper.getEnchantments(stack).containsKey(ModEnchants.wings) || stack.getItem() instanceof ItemWings)
			return true;
		
		return false;
	}
}
