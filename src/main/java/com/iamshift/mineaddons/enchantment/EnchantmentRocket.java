package com.iamshift.mineaddons.enchantment;

import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.init.ModEnchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class EnchantmentRocket extends Enchantment
{
	public EnchantmentRocket(String name)
	{
		super(Rarity.VERY_RARE, EnumEnchantmentType.ARMOR_FEET, new EntityEquipmentSlot[] {EntityEquipmentSlot.FEET});
		this.setRegistryName(Refs.ID, name);
		this.setName(name);
		
		ModEnchants.enchants.add(this);
	}
	
	@Override
	public int getMinEnchantability(int enchantmentLevel)
	{
		return 30;
	}
	
	@Override
	public int getMaxEnchantability(int enchantmentLevel)
	{
		return 30;
	}

	@Override
	public int getMinLevel()
	{
		return 1;
	}
	
	@Override
	public int getMaxLevel()
	{
		return 1;
	}
	
	@Override
	public boolean canApply(ItemStack stack)
	{
		return stack.getItem() instanceof ItemArmor && ((ItemArmor)stack.getItem()).armorType == EntityEquipmentSlot.FEET ? true : false;
	}
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack)
	{
		return false;
	}
	
	@Override
	public boolean isAllowedOnBooks()
	{
		return true;
	}
}
