package com.iamshift.mineaddons.integration;

import com.iamshift.mineaddons.core.Config;
import com.iamshift.mineaddons.init.ModBlocks;
import com.iamshift.mineaddons.init.ModFluids;
import com.iamshift.mineaddons.init.ModItems;
import com.iamshift.mineaddons.integration.tinkers.EnchantCastRecipe;
import com.iamshift.mineaddons.integration.tinkers.RepairRecipe;
import com.iamshift.mineaddons.integration.tinkers.TiCMaterial;
import com.iamshift.mineaddons.integration.tinkers.TraitLightspeed;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.smeltery.CastingRecipe;
import slimeknights.tconstruct.library.traits.AbstractTrait;

public class Tinkers
{
	public static final AbstractTrait lightspeed = new TraitLightspeed();
	
	public static void init()
	{
		TiCMaterial.setup();
		
		TinkerRegistry.registerMelting(ModItems.FiberHelmet, ModFluids.Fiberglass, Material.VALUE_Ingot * 5);
		TinkerRegistry.registerMelting(ModItems.FiberChestplate, ModFluids.Fiberglass, Material.VALUE_Ingot * 8);
		TinkerRegistry.registerMelting(ModItems.FiberLeggings, ModFluids.Fiberglass, Material.VALUE_Ingot * 7);
		TinkerRegistry.registerMelting(ModItems.FiberBoots, ModFluids.Fiberglass, Material.VALUE_Ingot * 4);
		
		if(Loader.isModLoaded("plentifluids"))
		{
			OreDictionary.registerOre("dustTinyFiberglass", ModItems.GlassPile);
			OreDictionary.registerOre("dustFiberglass", ModItems.Fiberglass);
		}
		else
		{
			OreDictionary.registerOre("nuggetFiberglass", ModItems.GlassPile);
			OreDictionary.registerOre("ingotFiberglass", ModItems.Fiberglass);
		}
		
		TinkerRegistry.registerTableCasting(new EnchantCastRecipe(new ItemStack(ModItems.FiberHelmet), RecipeMatch.of(Items.DIAMOND_HELMET), new FluidStack(ModFluids.Fiberglass, Material.VALUE_Ingot * 4), 496));
		TinkerRegistry.registerTableCasting(new EnchantCastRecipe(new ItemStack(ModItems.FiberChestplate), RecipeMatch.of(Items.DIAMOND_CHESTPLATE), new FluidStack(ModFluids.Fiberglass, Material.VALUE_Ingot * 8), 984));
		TinkerRegistry.registerTableCasting(new EnchantCastRecipe(new ItemStack(ModItems.FiberLeggings), RecipeMatch.of(Items.DIAMOND_LEGGINGS), new FluidStack(ModFluids.Fiberglass, Material.VALUE_Ingot * 8), 984));
		TinkerRegistry.registerTableCasting(new EnchantCastRecipe(new ItemStack(ModItems.FiberBoots), RecipeMatch.of(Items.DIAMOND_BOOTS), new FluidStack(ModFluids.Fiberglass, Material.VALUE_Ingot * 4), 496));
		
		TinkerRegistry.registerBasinCasting(new CastingRecipe(new ItemStack(ModBlocks.InfusedSoulBlock), RecipeMatch.of(ModBlocks.SoulBlock), new FluidStack(ModFluids.Fiberglass, Material.VALUE_Block), 1105, true, false));
		
		TinkerRegistry.registerBasinCasting(new CastingRecipe(new ItemStack(ModBlocks.ForgottenAnvil), RecipeMatch.of(new ItemStack(Blocks.ANVIL, 1, 0)), new FluidStack(ModFluids.ForgottenWater, 1000), 260, true, false));
		TinkerRegistry.registerBasinCasting(new CastingRecipe(new ItemStack(ModBlocks.ForgottenAnvil), RecipeMatch.of(new ItemStack(Blocks.ANVIL, 1, 1)), new FluidStack(ModFluids.ForgottenWater, 1000), 260, true, false));
		TinkerRegistry.registerBasinCasting(new CastingRecipe(new ItemStack(ModBlocks.ForgottenAnvil), RecipeMatch.of(new ItemStack(Blocks.ANVIL, 1, 2)), new FluidStack(ModFluids.ForgottenWater, 1000), 260, true, false));
		
		if(Config.HardLiquidStar)
			TinkerRegistry.registerMelting(Items.NETHER_STAR, ModFluids.LiquidStar, Material.VALUE_Ingot);
		else
			TinkerRegistry.registerMelting(Items.NETHER_STAR, ModFluids.LiquidStar, Material.VALUE_Ingot * 2);
		
		TinkerRegistry.registerTableCasting(new EnchantCastRecipe(new ItemStack(ModItems.UltimateHelmet), RecipeMatch.of(ModItems.FiberHelmet), new FluidStack(ModFluids.LiquidStar, Material.VALUE_Ingot * 4), 496));
		TinkerRegistry.registerTableCasting(new EnchantCastRecipe(new ItemStack(ModItems.UltimateChestplate), RecipeMatch.of(ModItems.FiberChestplate), new FluidStack(ModFluids.LiquidStar, Material.VALUE_Ingot * 8), 984));
		TinkerRegistry.registerTableCasting(new EnchantCastRecipe(new ItemStack(ModItems.UltimateLeggings), RecipeMatch.of(ModItems.FiberLeggings), new FluidStack(ModFluids.LiquidStar, Material.VALUE_Ingot * 8), 984));
		TinkerRegistry.registerTableCasting(new EnchantCastRecipe(new ItemStack(ModItems.UltimateBoots), RecipeMatch.of(ModItems.FiberBoots), new FluidStack(ModFluids.LiquidStar, Material.VALUE_Ingot * 4), 496));
		
		// REPAIR
		TinkerRegistry.registerTableCasting(new RepairRecipe(new ItemStack(ModItems.FiberHelmet), RecipeMatch.of(ModItems.FiberHelmet), 75, 100, new FluidStack(ModFluids.Fiberglass, Material.VALUE_Ingot * 1), 124));
		TinkerRegistry.registerTableCasting(new RepairRecipe(new ItemStack(ModItems.FiberHelmet), RecipeMatch.of(ModItems.FiberHelmet), 50, 75, new FluidStack(ModFluids.Fiberglass, Material.VALUE_Ingot * 2), 248));
		TinkerRegistry.registerTableCasting(new RepairRecipe(new ItemStack(ModItems.FiberHelmet), RecipeMatch.of(ModItems.FiberHelmet), 25, 50, new FluidStack(ModFluids.Fiberglass, Material.VALUE_Ingot * 3), 372));
		TinkerRegistry.registerTableCasting(new RepairRecipe(new ItemStack(ModItems.FiberHelmet), RecipeMatch.of(ModItems.FiberHelmet), 0, 25, new FluidStack(ModFluids.Fiberglass, Material.VALUE_Ingot * 4), 496));
		
		TinkerRegistry.registerTableCasting(new RepairRecipe(new ItemStack(ModItems.FiberChestplate), RecipeMatch.of(ModItems.FiberChestplate), 75, 100, new FluidStack(ModFluids.Fiberglass, Material.VALUE_Ingot * 2), 246));
		TinkerRegistry.registerTableCasting(new RepairRecipe(new ItemStack(ModItems.FiberChestplate), RecipeMatch.of(ModItems.FiberChestplate), 50, 75, new FluidStack(ModFluids.Fiberglass, Material.VALUE_Ingot * 4), 492));
		TinkerRegistry.registerTableCasting(new RepairRecipe(new ItemStack(ModItems.FiberChestplate), RecipeMatch.of(ModItems.FiberChestplate), 25, 50, new FluidStack(ModFluids.Fiberglass, Material.VALUE_Ingot * 6), 738));
		TinkerRegistry.registerTableCasting(new RepairRecipe(new ItemStack(ModItems.FiberChestplate), RecipeMatch.of(ModItems.FiberChestplate), 0, 25, new FluidStack(ModFluids.Fiberglass, Material.VALUE_Ingot * 8), 984));
		
		TinkerRegistry.registerTableCasting(new RepairRecipe(new ItemStack(ModItems.FiberLeggings), RecipeMatch.of(ModItems.FiberLeggings), 75, 100, new FluidStack(ModFluids.Fiberglass, Material.VALUE_Ingot * 2), 246));
		TinkerRegistry.registerTableCasting(new RepairRecipe(new ItemStack(ModItems.FiberLeggings), RecipeMatch.of(ModItems.FiberLeggings), 50, 75, new FluidStack(ModFluids.Fiberglass, Material.VALUE_Ingot * 4), 492));
		TinkerRegistry.registerTableCasting(new RepairRecipe(new ItemStack(ModItems.FiberLeggings), RecipeMatch.of(ModItems.FiberLeggings), 25, 50, new FluidStack(ModFluids.Fiberglass, Material.VALUE_Ingot * 6), 738));
		TinkerRegistry.registerTableCasting(new RepairRecipe(new ItemStack(ModItems.FiberLeggings), RecipeMatch.of(ModItems.FiberLeggings), 0, 25, new FluidStack(ModFluids.Fiberglass, Material.VALUE_Ingot * 8), 984));
		
		TinkerRegistry.registerTableCasting(new RepairRecipe(new ItemStack(ModItems.FiberBoots), RecipeMatch.of(ModItems.FiberBoots), 75, 100, new FluidStack(ModFluids.Fiberglass, Material.VALUE_Ingot * 1), 124));
		TinkerRegistry.registerTableCasting(new RepairRecipe(new ItemStack(ModItems.FiberBoots), RecipeMatch.of(ModItems.FiberBoots), 50, 75, new FluidStack(ModFluids.Fiberglass, Material.VALUE_Ingot * 2), 248));
		TinkerRegistry.registerTableCasting(new RepairRecipe(new ItemStack(ModItems.FiberBoots), RecipeMatch.of(ModItems.FiberBoots), 25, 50, new FluidStack(ModFluids.Fiberglass, Material.VALUE_Ingot * 3), 372));
		TinkerRegistry.registerTableCasting(new RepairRecipe(new ItemStack(ModItems.FiberBoots), RecipeMatch.of(ModItems.FiberBoots), 0, 25, new FluidStack(ModFluids.Fiberglass, Material.VALUE_Ingot * 4), 496));
		
		TinkerRegistry.registerTableCasting(new RepairRecipe(new ItemStack(ModItems.UltimateHelmet), RecipeMatch.of(ModItems.UltimateHelmet), 75, 100, new FluidStack(ModFluids.LiquidStar, Material.VALUE_Ingot * 1), 124));
		TinkerRegistry.registerTableCasting(new RepairRecipe(new ItemStack(ModItems.UltimateHelmet), RecipeMatch.of(ModItems.UltimateHelmet), 50, 75, new FluidStack(ModFluids.LiquidStar, Material.VALUE_Ingot * 2), 248));
		TinkerRegistry.registerTableCasting(new RepairRecipe(new ItemStack(ModItems.UltimateHelmet), RecipeMatch.of(ModItems.UltimateHelmet), 25, 50, new FluidStack(ModFluids.LiquidStar, Material.VALUE_Ingot * 3), 372));
		TinkerRegistry.registerTableCasting(new RepairRecipe(new ItemStack(ModItems.UltimateHelmet), RecipeMatch.of(ModItems.UltimateHelmet), 0, 25, new FluidStack(ModFluids.LiquidStar, Material.VALUE_Ingot * 4), 496));
		
		TinkerRegistry.registerTableCasting(new RepairRecipe(new ItemStack(ModItems.UltimateChestplate), RecipeMatch.of(ModItems.UltimateChestplate), 75, 100, new FluidStack(ModFluids.LiquidStar, Material.VALUE_Ingot * 2), 246));
		TinkerRegistry.registerTableCasting(new RepairRecipe(new ItemStack(ModItems.UltimateChestplate), RecipeMatch.of(ModItems.UltimateChestplate), 50, 75, new FluidStack(ModFluids.LiquidStar, Material.VALUE_Ingot * 4), 492));
		TinkerRegistry.registerTableCasting(new RepairRecipe(new ItemStack(ModItems.UltimateChestplate), RecipeMatch.of(ModItems.UltimateChestplate), 25, 50, new FluidStack(ModFluids.LiquidStar, Material.VALUE_Ingot * 6), 738));
		TinkerRegistry.registerTableCasting(new RepairRecipe(new ItemStack(ModItems.UltimateChestplate), RecipeMatch.of(ModItems.UltimateChestplate), 0, 25, new FluidStack(ModFluids.LiquidStar, Material.VALUE_Ingot * 8), 984));
		
		TinkerRegistry.registerTableCasting(new RepairRecipe(new ItemStack(ModItems.UltimateLeggings), RecipeMatch.of(ModItems.UltimateLeggings), 75, 100, new FluidStack(ModFluids.LiquidStar, Material.VALUE_Ingot * 2), 246));
		TinkerRegistry.registerTableCasting(new RepairRecipe(new ItemStack(ModItems.UltimateLeggings), RecipeMatch.of(ModItems.UltimateLeggings), 50, 75, new FluidStack(ModFluids.LiquidStar, Material.VALUE_Ingot * 4), 492));
		TinkerRegistry.registerTableCasting(new RepairRecipe(new ItemStack(ModItems.UltimateLeggings), RecipeMatch.of(ModItems.UltimateLeggings), 25, 50, new FluidStack(ModFluids.LiquidStar, Material.VALUE_Ingot * 6), 738));
		TinkerRegistry.registerTableCasting(new RepairRecipe(new ItemStack(ModItems.UltimateLeggings), RecipeMatch.of(ModItems.UltimateLeggings), 0, 25, new FluidStack(ModFluids.LiquidStar, Material.VALUE_Ingot * 8), 984));
		
		TinkerRegistry.registerTableCasting(new RepairRecipe(new ItemStack(ModItems.UltimateBoots), RecipeMatch.of(ModItems.UltimateBoots), 75, 100, new FluidStack(ModFluids.LiquidStar, Material.VALUE_Ingot * 1), 124));
		TinkerRegistry.registerTableCasting(new RepairRecipe(new ItemStack(ModItems.UltimateBoots), RecipeMatch.of(ModItems.UltimateBoots), 50, 75, new FluidStack(ModFluids.LiquidStar, Material.VALUE_Ingot * 2), 248));
		TinkerRegistry.registerTableCasting(new RepairRecipe(new ItemStack(ModItems.UltimateBoots), RecipeMatch.of(ModItems.UltimateBoots), 25, 50, new FluidStack(ModFluids.LiquidStar, Material.VALUE_Ingot * 3), 372));
		TinkerRegistry.registerTableCasting(new RepairRecipe(new ItemStack(ModItems.UltimateBoots), RecipeMatch.of(ModItems.UltimateBoots), 0, 25, new FluidStack(ModFluids.LiquidStar, Material.VALUE_Ingot * 4), 496));
	}
}
