package com.iamshift.mineaddons.utils;

import net.minecraft.init.Blocks;
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
