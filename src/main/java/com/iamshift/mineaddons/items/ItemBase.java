package com.iamshift.mineaddons.items;

import java.util.List;

import com.iamshift.mineaddons.MineAddons;
import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.init.ModItems;
import com.iamshift.mineaddons.interfaces.IHasModel;
import com.iamshift.mineaddons.interfaces.IRecipeProvider;

import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBase extends Item implements IHasModel, IRecipeProvider
{
	public ItemBase(String name)
	{
		setUnlocalizedName(name);
		setRegistryName(new ResourceLocation(Refs.ID, name));
		setMaxStackSize(64);
		
		setCreativeTab(MineAddons.minetab);
		
		ModItems.ITEMS.add(this);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels() 
	{
		MineAddons.proxy.registerItemRenderer(this, 0, "inventory");
	}

	@Override
	public List<IRecipe> getRecipe()
	{
		return null;
	}
}
