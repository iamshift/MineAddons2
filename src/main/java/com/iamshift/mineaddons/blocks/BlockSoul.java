package com.iamshift.mineaddons.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.init.ModBlocks;
import com.iamshift.mineaddons.init.ModItems;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class BlockSoul extends BlockBase
{
	public BlockSoul(String name)
	{
		super(name, Material.ROCK, false);
		setHardness(0.3F);
		setSoundType(SoundType.GLASS);
	}
	
	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
		Block block = iblockstate.getBlock();

		return blockState != iblockstate ? true : block == this ? false : super.shouldSideBeRendered(blockState, blockAccess, pos, side);
	}

	@Override
	public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity)
	{
		return false;
	}

	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
	{
		IBlockState state = world.getBlockState(pos);
		
		if(this.getRegistryName().getResourcePath().equals("infused_soul_block"))
			return 6000000.0F * 3.0F;
		
		return super.getExplosionResistance(world, pos, exploder, explosion);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		if(this.getRegistryName().getResourcePath().equals("infused_soul_block"))
			return Item.getItemFromBlock(ModBlocks.InfusedSoulBlock);
			
		return ModItems.Soul;
	}

	@Override
	protected boolean canSilkHarvest()
	{
		return true;
	}

	@Override
	protected ItemStack getSilkTouchDrop(IBlockState state)
	{
		return new ItemStack(Item.getItemFromBlock(this));
	}

	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random)
	{
		if(this.getRegistryName().getResourcePath().equals("infused_soul_block"))
			return 1;
		
		return 9;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced)
	{
		super.addInformation(stack, player, tooltip, advanced);
		
		if(this.getRegistryName().getResourcePath().equals("infused_soul_block"))
			tooltip.add(TextFormatting.DARK_AQUA + "Wither Proof");
	}
	
	@Override
	public List<IRecipe> getRecipe()
	{
		List<IRecipe> list = new ArrayList<IRecipe>();
		list.add(
				new ShapedOreRecipe(new ResourceLocation(Refs.ID), 
				new ItemStack(this, 1), 
				new Object[] {
				"BBB", "BBB", "BBB", 
				'B', new ItemStack(ModItems.Soul, 1, 0)
				}).setRegistryName(new ResourceLocation(Refs.ID, "soul_block"))
				);
		
		return list;
	}
}
