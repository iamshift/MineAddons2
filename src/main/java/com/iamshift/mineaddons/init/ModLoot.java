package com.iamshift.mineaddons.init;

import com.iamshift.mineaddons.core.Refs;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class ModLoot
{
	public static ResourceLocation ENDER_CARP;
	public static ResourceLocation ANCIENT_CARP;
	public static ResourceLocation ISHEEP;
	public static ResourceLocation ZLAMA;
	public static ResourceLocation HELLHOUND;
	public static ResourceLocation WITHER_BLAZE;
	public static ResourceLocation BLAZELLIER;
	public static ResourceLocation GHOST_RIDER;
	public static ResourceLocation VOIX;
	
	public static void init()
	{
			ENDER_CARP = reg("ender_carp");
			ANCIENT_CARP = reg("ancient_carp");
			VOIX = reg("voix");
			

			ISHEEP = reg("isheep");
			ZLAMA = reg("zlama");
			HELLHOUND = reg("hellhound");
			WITHER_BLAZE = reg("wither_blaze");
			BLAZELLIER = reg("blazelier");
		

			GHOST_RIDER = reg("ghost_rider");
	}
	
	private static ResourceLocation reg(String name)
	{
		ResourceLocation temp = new ResourceLocation(Refs.ID, name);
		LootTableList.register(temp);
		
		return temp;
	}
}
