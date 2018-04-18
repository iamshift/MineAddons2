package com.iamshift.mineaddons.core;

import com.iamshift.mineaddons.init.ModItems;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;

public class SmeltingRecipes
{
	public static void init()
	{
		FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(Items.WHEAT, 1), new ItemStack(ModItems.Cellulose, 1), 1.0F);
		FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(Items.REEDS, 1), new ItemStack(ModItems.Cellulose, 2), 2.0F);

		for(ItemStack stack : OreDictionary.getOres("treeLeaves"))
			FurnaceRecipes.instance().addSmeltingRecipe(stack, new ItemStack(ModItems.Cellulose, 3), 3.0F);

		for(ItemStack stack : OreDictionary.getOres("treeSapling"))
			FurnaceRecipes.instance().addSmeltingRecipe(stack, new ItemStack(ModItems.Cellulose, 5), 5.0F);

		if(Loader.isModLoaded("tconstruct"))
		{
			for(ItemStack stack : OreDictionary.getOres("blockGlass"))
				FurnaceRecipes.instance().addSmeltingRecipe(stack, new ItemStack(ModItems.GlassPile, 2), 2.0F);

			for(ItemStack stack : OreDictionary.getOres("paneGlass"))
				FurnaceRecipes.instance().addSmeltingRecipe(stack, new ItemStack(ModItems.GlassPile, 1), 1.0F);
		}
	}
}
