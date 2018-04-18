package com.iamshift.mineaddons.items;

import java.util.ArrayList;
import java.util.List;

import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.init.ModFluids;
import com.iamshift.mineaddons.init.ModItems;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ItemWitherDust extends ItemBase
{
	public ItemWitherDust(String name) 
	{
		super(name);
	}
	
	@Override
	public int getEntityLifespan(ItemStack itemStack, World world) 
	{
		return Integer.MAX_VALUE;
	}
	
	@Override
	public boolean isDamageable() 
	{
		return false;
	}
	
	@Override
	public boolean isDamaged(ItemStack stack) 
	{
		return false;
	}
	
	@Override
	public List<IRecipe> getRecipe()
	{
		List<IRecipe> list = new ArrayList<IRecipe>();
		
		list.add(
				new ShapedOreRecipe(new ResourceLocation(Refs.ID), 
				FluidUtil.getFilledBucket(new FluidStack(ModFluids.CursedWater, 1000)), 
				new Object[] {
				"I", "B", 
				'I', new ItemStack(ModItems.WitherDust, 1), 
				'B', Items.WATER_BUCKET
				}).setRegistryName(new ResourceLocation(Refs.ID, "cursed_water"))
				);
		
		return list;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) 
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add(TextFormatting.GREEN + "Used to create Cursed Water.");
	}
}
