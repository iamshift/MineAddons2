package com.iamshift.mineaddons.utils;

import java.util.Collections;
import java.util.Comparator;

import com.iamshift.mineaddons.init.ModFluids;
import com.iamshift.mineaddons.init.ModItems;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

public class MineTab extends CreativeTabs
{
	public MineTab() 
	{
		super("mineaddons");
	}

	@Override
	public ItemStack getTabIconItem() 
	{
		return new ItemStack(ModItems.AncientEssence);
	}

	@Override
	public void displayAllRelevantItems(NonNullList<ItemStack> list) 
	{
		super.displayAllRelevantItems(list);

		list.add(FluidUtil.getFilledBucket(new FluidStack(ModFluids.SacredWater, 1000)));
		list.add(FluidUtil.getFilledBucket(new FluidStack(ModFluids.CursedWater, 1000)));
		list.add(FluidUtil.getFilledBucket(new FluidStack(ModFluids.ForgottenWater, 1000)));

		list.add(FluidUtil.getFilledBucket(new FluidStack(ModFluids.Fiberglass, 1000)));
		list.add(FluidUtil.getFilledBucket(new FluidStack(ModFluids.LiquidStar, 1000)));

		Collections.sort(list, new Comparator<ItemStack>()
		{
			@Override
			public int compare(ItemStack s1, ItemStack s2)
			{
				if(s1.getItem() instanceof ItemBlock && !(s2.getItem() instanceof ItemBlock))
					return -1;

				if(s2.getItem() instanceof ItemBlock && !(s1.getItem() instanceof ItemBlock))
					return 1;

				if(Item.getIdFromItem(s1.getItem()) > Item.getIdFromItem(s2.getItem()))
					return 1;

				if(Item.getIdFromItem(s2.getItem()) > Item.getIdFromItem(s1.getItem()))
					return -1;

				if(Item.getIdFromItem(s2.getItem()) == Item.getIdFromItem(s1.getItem()))
				{
					if(s1.getItemDamage() > s2.getItemDamage())
						return 1;

					if(s2.getItemDamage() > s1.getItemDamage())
						return -1;
				}

				return s1.getDisplayName().compareToIgnoreCase(s2.getDisplayName());
			}
		});
	}
}
