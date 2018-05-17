package com.iamshift.mineaddons.items;

import java.util.ArrayList;
import java.util.List;

import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.init.ModItems;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ItemFiberglass extends ItemBase
{
	public ItemFiberglass(String name)
	{
		super(name);
	}
	
	@Override
	public List<IRecipe> getRecipe()
	{
		List<IRecipe> list = new ArrayList<IRecipe>();
		list.add(
				new ShapedOreRecipe(new ResourceLocation(Refs.ID), 
				new ItemStack(this, 1), 
				new Object[] {
				"FFF", "FFF", "FFF",
				'F', new ItemStack(ModItems.GlassPile, 1, 0)
				}).setRegistryName(new ResourceLocation(Refs.ID, "fiberglass"))
				);
		
		return list;
	}
}
