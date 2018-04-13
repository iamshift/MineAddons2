package com.iamshift.mineaddons.items;

import java.util.ArrayList;
import java.util.List;

import com.iamshift.mineaddons.MineAddons;
import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.init.ModItems;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ItemEnchanted extends ItemBase
{
	public ItemEnchanted(String name)
	{
		super(name);
		setHasSubtypes(true);
	}

	@Override
	public boolean hasEffect(ItemStack stack)
	{
		return true;
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if(this.isInCreativeTab(tab))
		{
			for(int i = 0; i < Types.values().length; i++)
				items.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return getUnlocalizedName() + "." + Types.values()[stack.getItemDamage()].getName();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels()
	{
		for(int i = 0; i < Types.values().length; i++)
			MineAddons.proxy.registerVariantRenderer(this, i, "enchanted_" + Types.values()[i], "inventory");
	}

	@Override
	public List<IRecipe> getRecipe()
	{
		List<IRecipe> list = new ArrayList<IRecipe>();

		list.add(
				new ShapedOreRecipe(new ResourceLocation(Refs.ID), 
				new ItemStack(this, 1, 0), 
				new Object[] {
				"LLL", "LIL", "LLL", 
				'L', new ItemStack(ModItems.Lapis, 1, 2), 
				'I', new ItemStack(Items.BONE, 1, 0)
				}).setRegistryName(new ResourceLocation(Refs.ID, "enchanted_bone"))
				);
			
		list.add(
				new ShapedOreRecipe(new ResourceLocation(Refs.ID), 
				new ItemStack(this, 1, 1), 
				new Object[] {
				"LLL", "LIL", "LLL", 
				'L', new ItemStack(ModItems.Lapis, 1, 2), 
				'I', new ItemStack(Items.DIAMOND, 1, 0)
				}).setRegistryName(new ResourceLocation(Refs.ID, "enchanted_diamond"))
				);
		
		list.add(
				new ShapedOreRecipe(new ResourceLocation(Refs.ID), 
				new ItemStack(this, 1, 2), 
				new Object[] {
				"LLL", "LIL", "LLL", 
				'L', new ItemStack(ModItems.Lapis, 1, 2), 
				'I', new ItemStack(Items.NETHER_STAR, 1, 0)
				}).setRegistryName(new ResourceLocation(Refs.ID, "enchanted_star"))
				);
			
		return list;
	}

	static enum Types
	{
		BONE("bone"),
		DIAMOND("diamond"),
		STAR("star");

		private String name;

		private Types(String name)
		{
			this.name = name;
		}

		public String getName()
		{
			return this.name;
		}

		@Override
		public String toString()
		{
			return getName();
		}
	}
}
