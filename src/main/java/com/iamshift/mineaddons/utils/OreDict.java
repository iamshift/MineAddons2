package com.iamshift.mineaddons.utils;

import com.iamshift.mineaddons.init.ModBlocks;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreDict
{
	public static void createDitc()
	{
		OreDictionary.registerOre("fence", Blocks.ACACIA_FENCE);
    	OreDictionary.registerOre("fence", Blocks.BIRCH_FENCE);
    	OreDictionary.registerOre("fence", Blocks.DARK_OAK_FENCE);
    	OreDictionary.registerOre("fence", Blocks.JUNGLE_FENCE);
    	OreDictionary.registerOre("fence", Blocks.OAK_FENCE);
    	OreDictionary.registerOre("fence", Blocks.SPRUCE_FENCE);
	}
}
