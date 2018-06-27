package com.iamshift.mineaddons.utils;

import java.util.HashMap;
import java.util.Map;

import com.iamshift.mineaddons.blocks.containers.ContainerForgottenAnvil;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentLootBonus;
import net.minecraft.enchantment.EnchantmentUntouching;

public class ForgottenAnvilHelper
{
	public static Map<Enchantment, Integer> enchantments = new HashMap<>();

	public static void loadEnchantments()
	{
		enchantments.put(Enchantment.getEnchantmentByID(0), 6); // PROTECTION
		enchantments.put(Enchantment.getEnchantmentByID(1), 6); // FIRE PROTECTION
		enchantments.put(Enchantment.getEnchantmentByID(3), 6); // BLAST PROTECTION
		enchantments.put(Enchantment.getEnchantmentByID(4), 6); // PROJECTILE PROTECTION
		enchantments.put(Enchantment.getEnchantmentByID(7), 5); // THORNS
		enchantments.put(Enchantment.getEnchantmentByID(34), 5); // UNBREAKING
		enchantments.put(Enchantment.getEnchantmentByID(16), 7); // SHARPNESS
		enchantments.put(Enchantment.getEnchantmentByID(21), 5); // LOOTING
		enchantments.put(Enchantment.getEnchantmentByID(32), 7); // EFFICIENCY
		enchantments.put(Enchantment.getEnchantmentByID(35), 5); // FORTUNE
	}

	public static boolean isCompatible(EnchantmentData fe, Map<Enchantment, Integer> list)
	{
		for(Enchantment e : list.keySet())
		{
			if(fe.enchantment instanceof EnchantmentLootBonus && e instanceof EnchantmentUntouching)
				return false;
			
			if(fe.enchantment.getClass() == e.getClass())
			{
				if(fe.enchantment.getName().equals(e.getName()) && fe.enchantmentLevel > list.get(e))
					return true;
				
				return false;
			}
		}
		
		return true;
	}
}
