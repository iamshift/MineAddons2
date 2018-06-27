package com.iamshift.mineaddons.blocks;

import java.util.ArrayList;
import java.util.List;

import com.iamshift.mineaddons.MineAddons;
import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.init.ModBlocks;
import com.iamshift.mineaddons.init.ModItems;
import com.iamshift.mineaddons.interfaces.IHasModel;
import com.iamshift.mineaddons.interfaces.IRecipeProvider;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.SoundType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class BlockBlackSandStoneStairs extends BlockStairs implements IHasModel, IRecipeProvider
{
	public BlockBlackSandStoneStairs(String name)
	{
		super(ModBlocks.BlackSandstone.getDefaultState());
		setUnlocalizedName(name);
		setRegistryName(new ResourceLocation(Refs.ID, name));
		setHardness(0.8F);
		setSoundType(SoundType.STONE);
		setHarvestLevel("pickaxe", 0);
		
		setCreativeTab(MineAddons.minetab);
		
		ModBlocks.BLOCKS.add(this);
		ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
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
		List<IRecipe> list = new ArrayList<IRecipe>();
		list.add(
				new ShapedOreRecipe(new ResourceLocation(Refs.ID), 
				new ItemStack(this, 8), 
				new Object[] {
				"S  ", "SS ", "SSS",  
				'S', new ItemStack(ModBlocks.BlackSandstone)
				}).setRegistryName(new ResourceLocation(Refs.ID, "black_sandstone_stairs"))
				);
		
		return list;
	}
}
