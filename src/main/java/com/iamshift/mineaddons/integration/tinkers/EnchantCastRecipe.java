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
		
		NBTTagList nbttaglist = cast.getEnchantmentTagList();
		for (int j = 0; j < nbttaglist.tagCount(); ++j)
        {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(j);
            int k = nbttagcompound.getShort("id");
            int l = nbttagcompound.getShort("lvl");
            Enchantment enchantment = Enchantment.getEnchantmentByID(k);
            
            trueResult.addEnchantment(enchantment, l);
        }
		
		return trueResult;
	}

	@Override
	public boolean matches(ItemStack cast, Fluid fluid)
	{
		 if((cast.isEmpty() && this.cast == null) || (this.cast != null && this.cast.matches(ListUtil.getListFrom(cast)).isPresent())) 
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
