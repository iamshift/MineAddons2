package com.iamshift.mineaddons.core;

import java.util.List;

import com.iamshift.mineaddons.init.ModBlocks;
import com.iamshift.mineaddons.init.ModEnchants;
import com.iamshift.mineaddons.init.ModItems;
import com.iamshift.mineaddons.init.ModPotions;
import com.iamshift.mineaddons.init.ModSounds;
import com.iamshift.mineaddons.interfaces.IHasModel;
import com.iamshift.mineaddons.interfaces.IRecipeProvider;
import com.iamshift.mineaddons.items.armors.ItemUltimateArmor;
import com.iamshift.mineaddons.utils.RecipesUltimateArmorDyes;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = Refs.ID)
public class Register 
{
	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().registerAll(ModItems.ITEMS.toArray(new Item[0]));
	}

	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event)
	{
		event.getRegistry().registerAll(ModBlocks.BLOCKS.toArray(new Block[0]));
	}

	@SubscribeEvent
	public static void onSoundRegister(RegistryEvent.Register<SoundEvent> event)
	{
		event.getRegistry().registerAll(ModSounds.SOUNDS.toArray(new SoundEvent[0]));
	}

	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event)
	{
		for(Item item : ModItems.ITEMS)
		{
			if(item instanceof IHasModel)
				((IHasModel)item).registerModels(); 
		}

		for(Block block : ModBlocks.BLOCKS)
		{
			if(block instanceof IHasModel)
				((IHasModel)block).registerModels();
		}
	}

	@SubscribeEvent
	public static void onRecipeRegister(RegistryEvent.Register<IRecipe> event)
	{
		for(Item item : ModItems.ITEMS)
		{
			if(item instanceof IRecipeProvider)
			{
				List<IRecipe> recipes = ((IRecipeProvider) item).getRecipe();
				if(recipes != null && !recipes.isEmpty())
					event.getRegistry().registerAll(recipes.toArray(new IRecipe[recipes.size()]));
			}

		}

		for(Block block : ModBlocks.BLOCKS)
		{
			if(block instanceof IRecipeProvider)
			{
				List<IRecipe> recipes = ((IRecipeProvider) block).getRecipe();
				if(recipes != null && !recipes.isEmpty())
					event.getRegistry().registerAll(recipes.toArray(new IRecipe[recipes.size()]));
			}
		}

		event.getRegistry().register(new RecipesUltimateArmorDyes().setRegistryName(Refs.ID, "ultimatedyes"));
	}

	@SubscribeEvent
	public static void onPotionRegister(RegistryEvent.Register<Potion> event)
	{
		for(Potion p : ModPotions.potions)
			event.getRegistry().register(p);
	}

	@SubscribeEvent
	public static void onEnchantmentRegister(RegistryEvent.Register<Enchantment> event)
	{
		for(Enchantment e : ModEnchants.enchants)
			event.getRegistry().register(e);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void registerItemColourHandlers(final ColorHandlerEvent.Item event) 
	{
		final ItemColors itemColors = event.getItemColors();

		if(Loader.isModLoaded("tconstruct"))
		{
			itemColors.registerItemColorHandler(new IItemColor() 
			{
				@Override
				public int colorMultiplier(ItemStack stack, int tintIndex) 
				{
					return tintIndex > 0 ? -1 : ((ItemUltimateArmor)stack.getItem()).getColor(stack);
				}
			}, ModItems.UltimateHelmet, ModItems.UltimateChestplate, ModItems.UltimateLeggings, ModItems.UltimateBoots);
		}

		itemColors.registerItemColorHandler(new IItemColor() 
		{
			@Override
			public int colorMultiplier(ItemStack stack, int tintIndex) 
			{
				return tintIndex != 1 ? -1 : EnumDyeColor.byDyeDamage(stack.getItemDamage()).getColorValue();
			}
		}, ModItems.BrainlessShulkerEgg);
	}
}
