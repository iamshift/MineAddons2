package com.iamshift.mineaddons.items;

import java.util.ArrayList;
import java.util.List;

import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.init.ModBlocks;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ItemSoul extends ItemBase
{

	public ItemSoul(String name)
	{
		super(name);
	}

	@Override
	public List<IRecipe> getRecipe()
	{
		List<IRecipe> list = new ArrayList<IRecipe>();
		list.add(
				new ShapelessOreRecipe(new ResourceLocation(Refs.ID), 
				new ItemStack(this, 9), 
				new Object[] {
				new ItemStack(ModBlocks.SoulBlock, 1, 0)
				}).setRegistryName(new ResourceLocation(Refs.ID, "soul"))
				);
		
		return list;
	}
}
