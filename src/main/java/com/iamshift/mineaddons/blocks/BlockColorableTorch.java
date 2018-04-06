package com.iamshift.mineaddons.blocks;

import java.util.ArrayList;
import java.util.List;

import com.iamshift.mineaddons.MineAddons;
import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.init.ModBlocks;
import com.iamshift.mineaddons.init.ModItems;
import com.iamshift.mineaddons.interfaces.IHasModel;
import com.iamshift.mineaddons.interfaces.IRecipeProvider;

import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class BlockColorableTorch extends BlockTorch implements IHasModel, IRecipeProvider
{
	private final int meta;
	
	public BlockColorableTorch(String name, int meta)
	{
		super();
		setUnlocalizedName(name);
		setRegistryName(new ResourceLocation(Refs.ID, name));
		
		this.meta = meta;
		
		setCreativeTab(MineAddons.minetab);
		
		ModBlocks.BLOCKS.add(this);
		ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(worldIn.isRemote)
			return false;
		
		ItemStack heldItem = playerIn.getHeldItem(hand);
		
		if(heldItem == null)
			return false;

		if(heldItem.getItem() == null)
			return false;
		
		if(!(heldItem.getItem() instanceof ItemDye))
			return false;
		
		switch(heldItem.getMetadata())
		{
		case 0:
			worldIn.setBlockState(pos, ModBlocks.ColorableTorch_black.getDefaultState().withProperty(FACING, getDirection(state)));
			break;
		case 1:
			worldIn.setBlockState(pos, ModBlocks.ColorableTorch_red.getDefaultState().withProperty(FACING, getDirection(state)));
			break;
		case 2:
			worldIn.setBlockState(pos, ModBlocks.ColorableTorch_green.getDefaultState().withProperty(FACING, getDirection(state)));
			break;
		case 3:
			worldIn.setBlockState(pos, ModBlocks.ColorableTorch_brown.getDefaultState().withProperty(FACING, getDirection(state)));
			break;
		case 4:
			worldIn.setBlockState(pos, ModBlocks.ColorableTorch_blue.getDefaultState().withProperty(FACING, getDirection(state)));
			break;
		case 5:
			worldIn.setBlockState(pos, ModBlocks.ColorableTorch_purple.getDefaultState().withProperty(FACING, getDirection(state)));
			break;
		case 6:
			worldIn.setBlockState(pos, ModBlocks.ColorableTorch_cyan.getDefaultState().withProperty(FACING, getDirection(state)));
			break;
		case 7:
			worldIn.setBlockState(pos, ModBlocks.ColorableTorch_silver.getDefaultState().withProperty(FACING, getDirection(state)));
			break;
		case 8:
			worldIn.setBlockState(pos, ModBlocks.ColorableTorch_gray.getDefaultState().withProperty(FACING, getDirection(state)));
			break;
		case 9:
			worldIn.setBlockState(pos, ModBlocks.ColorableTorch_pink.getDefaultState().withProperty(FACING, getDirection(state)));
			break;
		case 10:
			worldIn.setBlockState(pos, ModBlocks.ColorableTorch_lime.getDefaultState().withProperty(FACING, getDirection(state)));
			break;
		case 11:
			worldIn.setBlockState(pos, ModBlocks.ColorableTorch_yellow.getDefaultState().withProperty(FACING, getDirection(state)));
			break;
		case 12:
			worldIn.setBlockState(pos, ModBlocks.ColorableTorch_lightblue.getDefaultState().withProperty(FACING, getDirection(state)));
			break;
		case 13:
			worldIn.setBlockState(pos, ModBlocks.ColorableTorch_magenta.getDefaultState().withProperty(FACING, getDirection(state)));
			break;
		case 14:
			worldIn.setBlockState(pos, ModBlocks.ColorableTorch_orange.getDefaultState().withProperty(FACING, getDirection(state)));
			break;
		case 15:
			worldIn.setBlockState(pos, ModBlocks.ColorableTorch_white.getDefaultState().withProperty(FACING, getDirection(state)));
			break;
		}
		
		return true;
	}
	
	private EnumFacing getDirection(IBlockState state)
	{
		int meta = this.getMetaFromState(state);
		
		EnumFacing face = EnumFacing.UP;
		
		switch (meta)
        {
            case 1:
            	face = EnumFacing.EAST;
            	break;
            case 2:
            	face = EnumFacing.WEST;
            	break;
            case 3:
            	face = EnumFacing.SOUTH;
            	break;
            case 4:
            	face = EnumFacing.NORTH;
            	break;
        }
		
		return face;
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
				new ItemStack(this, 1), 
				new Object[] {
				"T", "C", 
				'T', new ItemStack(Blocks.TORCH, 1, 0), 
				'C', new ItemStack(Blocks.CARPET, 1, meta)
				}).setRegistryName(new ResourceLocation(Refs.ID, "colorable_torch_" + EnumDyeColor.values()[meta]))
				);
		
		return list;
	}
}
