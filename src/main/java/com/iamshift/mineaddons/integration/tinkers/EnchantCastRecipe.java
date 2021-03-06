package com.iamshift.mineaddons.integration.tinkers;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.smeltery.ICastingRecipe;
import slimeknights.tconstruct.library.utils.ListUtil;

public class EnchantCastRecipe implements ICastingRecipe
{
	protected int time;
	protected ItemStack result;
	protected RecipeMatch cast;
	protected FluidStack fluid;

	public EnchantCastRecipe(ItemStack result, RecipeMatch cast, FluidStack fluid, int time)
	{
		this.time = time;
		this.cast = cast;
		this.fluid = fluid;
		this.result = result;
	}

	@Override
	public ItemStack getResult(ItemStack cast, Fluid fluid)
	{
		ItemStack trueResult = result.copy();

		if(cast.hasTagCompound())
		{
			if(!trueResult.hasTagCompound())
				trueResult.setTagCompound(new NBTTagCompound());

			trueResult.getTagCompound().merge(cast.getTagCompound());
		}

		return trueResult;
	}

	@Override
	public boolean matches(ItemStack cast, Fluid fluid)
	{
		ItemStack input = new ItemStack(cast.getItem());
		if((input.isEmpty() && this.cast == null) || (this.cast != null && this.cast.matches(ListUtil.getListFrom(input)).isPresent())) 
		{
			return this.fluid.getFluid() == fluid;
		}
		return false;
	}

	@Override
	public boolean switchOutputs()
	{
		return false;
	}

	@Override
	public boolean consumesCast()
	{
		return true;
	}

	@Override
	public int getTime()
	{
		return time;
	}

	@Override
	public int getFluidAmount()
	{
		return fluid.amount;
	}

}
