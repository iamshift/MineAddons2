package com.iamshift.mineaddons.integration;

import com.iamshift.mineaddons.core.Config;
import com.iamshift.mineaddons.init.ModFluids;
import com.iamshift.mineaddons.init.ModItems;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.recipe.matcher.ItemStackMatcher;
import exter.foundry.api.recipe.matcher.OreMatcher;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.manager.AlloyingCrucibleRecipeManager;
import exter.foundry.recipes.manager.InfuserRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class Foundry
{
	public static void init()
	{
		if(Config.FiberTools)
		{
			MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(ModItems.GlassPile), new FluidStack(ModFluids.Fiberglass, FoundryAPI.FLUID_AMOUNT_INGOT / 18), getMeltingTemp(ModFluids.Fiberglass.getTemperature(), FoundryAPI.FLUID_AMOUNT_INGOT / 18), 500);
			MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(ModItems.Fiberglass), new FluidStack(ModFluids.Fiberglass, FoundryAPI.FLUID_AMOUNT_INGOT / 2), getMeltingTemp(ModFluids.Fiberglass.getTemperature(), FoundryAPI.FLUID_AMOUNT_INGOT / 2), 500);
			MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(ModItems.FiberglassIngot), new FluidStack(ModFluids.Fiberglass, FoundryAPI.FLUID_AMOUNT_INGOT), getMeltingTemp(ModFluids.Fiberglass.getTemperature(), FoundryAPI.FLUID_AMOUNT_INGOT), 500);

			if(Config.HardLiquidStar)
				MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(Items.NETHER_STAR), new FluidStack(ModFluids.LiquidStar, FoundryAPI.FLUID_AMOUNT_INGOT * 2), getMeltingTemp(ModFluids.LiquidStar.getTemperature(), FoundryAPI.FLUID_AMOUNT_INGOT * 2), 500);
			else
				MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(Items.NETHER_STAR), new FluidStack(ModFluids.LiquidStar, FoundryAPI.FLUID_AMOUNT_INGOT), getMeltingTemp(ModFluids.LiquidStar.getTemperature(), FoundryAPI.FLUID_AMOUNT_INGOT), 500);

			if(Config.FiberTools)
			{
				FoundryMiscUtils.registerCasting(new ItemStack(ModItems.FiberPickaxe, 1), ModFluids.FiberStar, 3, ItemMold.SubItem.PICKAXE, new ItemStackMatcher(Items.DIAMOND_PICKAXE));
				FoundryMiscUtils.registerCasting(new ItemStack(ModItems.FiberAxe, 1), ModFluids.FiberStar, 3, ItemMold.SubItem.AXE, new ItemStackMatcher(Items.DIAMOND_AXE));
				FoundryMiscUtils.registerCasting(new ItemStack(ModItems.FiberShovel, 1), ModFluids.FiberStar, 3, ItemMold.SubItem.SHOVEL, new ItemStackMatcher(Items.DIAMOND_SHOVEL));
			}

			AlloyingCrucibleRecipeManager.INSTANCE.addRecipe(new FluidStack(ModFluids.FiberStar, 500), new FluidStack(ModFluids.InfusedDiamond, 500), new FluidStack(ModFluids.LiquidStar, 700));
			InfuserRecipeManager.INSTANCE.addRecipe(new FluidStack(ModFluids.InfusedDiamond, 500), new FluidStack(ModFluids.Fiberglass, 500), new ItemStackMatcher(Items.DIAMOND), 700);

			if(Config.FiberArmor)
			{
				FoundryMiscUtils.registerCasting(new ItemStack(ModItems.FiberHelmet), ModFluids.InfusedDiamond, 4, ItemMold.SubItem.HELMET);
				FoundryMiscUtils.registerCasting(new ItemStack(ModItems.FiberChestplate), ModFluids.InfusedDiamond, 8, ItemMold.SubItem.CHESTPLATE);
				FoundryMiscUtils.registerCasting(new ItemStack(ModItems.FiberLeggings), ModFluids.InfusedDiamond, 8, ItemMold.SubItem.LEGGINGS);
				FoundryMiscUtils.registerCasting(new ItemStack(ModItems.FiberBoots), ModFluids.InfusedDiamond, 4, ItemMold.SubItem.BOOTS);
			}

			if(Config.UltiArmor)
			{
				FoundryMiscUtils.registerCasting(new ItemStack(ModItems.UltimateHelmet), ModFluids.FiberStar, 4, ItemMold.SubItem.HELMET);
				FoundryMiscUtils.registerCasting(new ItemStack(ModItems.UltimateChestplate), ModFluids.FiberStar, 8, ItemMold.SubItem.CHESTPLATE);
				FoundryMiscUtils.registerCasting(new ItemStack(ModItems.UltimateLeggings), ModFluids.FiberStar, 8, ItemMold.SubItem.LEGGINGS);
				FoundryMiscUtils.registerCasting(new ItemStack(ModItems.UltimateBoots), ModFluids.FiberStar, 4, ItemMold.SubItem.BOOTS);
			}
		}
	}

	private static int getMeltingTemp(int temp, int amount)
	{
		int base = FoundryAPI.getAmountBlock();
		int max_tmp = Math.max(0, temp - 300);
		double f = (double) amount / (double) base;
		f = Math.pow(f, 0.31546487678);

		return 300 + (int) (f * (double) max_tmp);
	}
}
