package com.iamshift.mineaddons.integration.crafttweaker;

import java.util.ArrayList;

import com.iamshift.mineaddons.MineAddons;
import com.iamshift.mineaddons.blocks.containers.ContainerForgottenAnvil;
import com.iamshift.mineaddons.integration.CraftTweaker;
import com.iamshift.mineaddons.utils.AnvilRecipe;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.mineaddons.ForgottenAnvil")
public class CTAnvilHandler
{
	private static class Add implements IAction
	{
		private final AnvilRecipe recipe;

		public Add(AnvilRecipe recipe)
		{
			this.recipe = recipe;
		}

		@Override
		public void apply()
		{
			if(ContainerForgottenAnvil.recipes.containsKey(recipe.getIng().getItem()))
			{
				ArrayList<AnvilRecipe> list = ContainerForgottenAnvil.recipes.get(recipe.getIng().getItem());
				if(!list.contains(recipe))
				{
					list.add(recipe);
					ContainerForgottenAnvil.recipes.put(recipe.getIng().getItem(), list);
				}
			}
			else
			{
				ArrayList<AnvilRecipe> list = new ArrayList<AnvilRecipe>();
				list.add(recipe);
				ContainerForgottenAnvil.recipes.put(recipe.getIng().getItem(), list);
			}
		}

		@Override
		public String describe()
		{
			return "Adding recipe for " + recipe.getOut().getDisplayName();
		}
	}
	
	private static class Remove implements IAction
	{
		private final AnvilRecipe recipe;
		
		public Remove(AnvilRecipe recipe)
		{
			this.recipe = recipe;
		}
		
		@Override
		public void apply()
		{
			if(ContainerForgottenAnvil.recipes.containsKey(recipe.getIng().getItem()))
			{
				ArrayList<AnvilRecipe> list = ContainerForgottenAnvil.recipes.get(recipe.getIng().getItem());
				list.remove(recipe);

				if(list.isEmpty())
					ContainerForgottenAnvil.recipes.remove(recipe.getIng().getItem());
				else
					ContainerForgottenAnvil.recipes.put(recipe.getIng().getItem(), list);
			}
		}

		@Override
		public String describe()
		{
			return "Removing recipe for " + recipe.getOut();
		}
		
	}

	@ZenMethod
	public static void addAnvilRecipe(IItemStack output, IItemStack input, IItemStack ingredient)
	{
		addRecipe(output, input, ingredient, 1, 1, true);
	}

	@ZenMethod
	public static void addAnvilRecipe(IItemStack output, IItemStack input, IItemStack ingredient, int cost)
	{
		addRecipe(output, input, ingredient, cost, 1, true);
	}

	@ZenMethod
	public static void addAnvilRecipe(IItemStack output, IItemStack input, IItemStack ingredient, int cost, int xp)
	{
		addRecipe(output, input, ingredient, cost, xp, true);
	}

	@ZenMethod
	public static void addAnvilRecipe(IItemStack output, IItemStack input, IItemStack ingredient, int cost, int xp, boolean destroy)
	{
		addRecipe(output, input, ingredient, cost, xp, destroy);
	}

	private static void addRecipe(IItemStack output, IItemStack input, IItemStack ingredient, int cost, int xp, boolean destroy)
	{
		CraftTweaker.queue(() -> {
			ItemStack out = CraftTweakerMC.getItemStack(output);
			ItemStack in = CraftTweakerMC.getItemStack(input);
			ItemStack ing = CraftTweakerMC.getItemStack(ingredient);
			AnvilRecipe recipe = null;

			try
			{
				recipe = new AnvilRecipe(out, in, ing, cost, xp, destroy);
			}
			catch (IllegalArgumentException e) 
			{
				MineAddons.LOGGER.error("Invalid Recipe: " + e.getMessage());
			}

			CraftTweakerAPI.apply(new Add(recipe));
		});
	}

	@ZenMethod
	public static void removeAnvilRecipe(IItemStack output)
	{
		removeRecipe(output);
	}

	private static void removeRecipe(IItemStack output)
	{
		CraftTweaker.queue(() -> {
			AnvilRecipe recipe = null;
			for(Item ingredient : ContainerForgottenAnvil.recipes.keySet())
			{
				for(AnvilRecipe r : ContainerForgottenAnvil.recipes.get(ingredient))
				{
					if(ItemStack.areItemStacksEqual(r.getOut(), CraftTweakerMC.getItemStack(output)))
						recipe = r;
				}
			}

			if(recipe == null)
			{
				MineAddons.LOGGER.error("Recipe not found.");
				return;
			}

			CraftTweakerAPI.apply(new Remove(recipe));
		});
	}
}
