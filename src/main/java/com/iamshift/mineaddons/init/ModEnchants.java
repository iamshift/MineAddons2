package com.iamshift.mineaddons.init;

import java.util.ArrayList;

import com.iamshift.mineaddons.enchantment.EnchantmentElytra;
import com.iamshift.mineaddons.enchantment.EnchantmentHungry;
import com.iamshift.mineaddons.enchantment.EnchantmentRespirator;
import com.iamshift.mineaddons.enchantment.EnchantmentRocket;
import com.iamshift.mineaddons.enchantment.EnchantmentWings;

import net.minecraft.enchantment.Enchantment;

public class ModEnchants
{
	public static ArrayList<Enchantment> enchants = new ArrayList<Enchantment>();
	
	public static Enchantment elytra;
	public static Enchantment wings;
	public static Enchantment rocket;
	public static Enchantment respirator;
	public static Enchantment hungry;
	
	public static void init()
	{
		elytra = new EnchantmentElytra("elytra");
		wings = new EnchantmentWings("wings");
		rocket = new EnchantmentRocket("rocket");
		respirator = new EnchantmentRespirator("respirator");
		hungry = new EnchantmentHungry("hungry");
	}
}
