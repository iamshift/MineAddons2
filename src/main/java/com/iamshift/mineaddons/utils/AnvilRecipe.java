package com.iamshift.mineaddons.utils;

import com.iamshift.mineaddons.api.IAnvilRecipe;

import net.minecraft.item.ItemStack;

public class AnvilRecipe implements IAnvilRecipe
{
	private ItemStack out;
	private ItemStack in;
	private ItemStack ing;
	private int cost;
	private int xp;
	private boolean destroy;
	
	public AnvilRecipe(ItemStack out, ItemStack in, ItemStack ing, int cost, int xp, boolean destroy)
	{
		this.out = out;
		this.in = in;
		this.ing = ing;
		this.cost = cost;
		this.xp = xp;
		this.destroy = destroy;
	}

	public ItemStack getOut()
	{
		return out;
	}

	public ItemStack getIn()
	{
		return in;
	}

	public ItemStack getIng()
	{
		return ing;
	}

	public int getCost()
	{
		return cost;
	}

	public int getXp()
	{
		return xp;
	}

	public boolean shouldDestroy()
	{
		return destroy;
	}
	
	public boolean matches(ItemStack in, ItemStack ing)
	{
		if(in == null || ing == null)
		{
			return false;
		}
		
		if(!ItemStack.areItemStacksEqual(ing, this.ing))
		{
			return false;
		}
		
		return true;
	}
}
