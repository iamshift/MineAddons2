package com.iamshift.mineaddons.blocks;

import java.util.List;

import com.iamshift.mineaddons.MineAddons;
import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.init.ModBlocks;
import com.iamshift.mineaddons.init.ModItems;
import com.iamshift.mineaddons.interfaces.IHasModel;
import com.iamshift.mineaddons.interfaces.IRecipeProvider;
import com.iamshift.mineaddons.utils.ItemBlockVariants;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBase extends Block implements IHasModel, IRecipeProvider
{
	public BlockBase(String name, Material material, boolean variants) 
	{
		super(material);
		setUnlocalizedName(name);
		setRegistryName(new ResourceLocation(Refs.ID, name));
		
		setCreativeTab(MineAddons.minetab);
		
		ModBlocks.BLOCKS.add(this);
		
		if(!variants)
			registerItem();
		else
			registerItemVariants();
	}
	
	public void registerItem()
	{
		ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
	
	public void registerItemVariants()
	{
		ModItems.ITEMS.add(new ItemBlockVariants(this).setRegistryName(this.getRegistryName()));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels() 
	{
		MineAddons.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}

	@Override
	public List<IRecipe> getRecipe()
	{
		return null;
	}
}
