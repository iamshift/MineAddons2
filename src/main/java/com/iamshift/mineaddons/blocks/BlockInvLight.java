package com.iamshift.mineaddons.blocks;

import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.init.ModBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGlowstone;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockInvLight extends BlockGlowstone
{
	public BlockInvLight(String name)
	{
		super(Material.GLASS);
		setUnlocalizedName(name);
		setRegistryName(new ResourceLocation(Refs.ID, name));
		
		this.setBlockUnbreakable();
        this.setResistance(6000001.0F);
        this.disableStats();
        this.translucent = true;
		setLightLevel(1.0F);
		
		ModBlocks.BLOCKS.add(this);
	}

	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer)
	{
		return false;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.INVISIBLE;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public float getAmbientOcclusionLightValue(IBlockState state)
	{
		return 1.0F;
	}
	
	@Override
	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
	{
		return NULL_AABB;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return new AxisAlignedBB(0.4D, 0.4D, 0.4D, 0.5D, 0.5D, 0.5D);
	}
	
	@Override
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos)
	{
		return new AxisAlignedBB(0.4D, 0.4D, 0.4D, 0.5D, 0.5D, 0.5D);
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos,EnumFacing side) 
	{
		IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
		Block block = iblockstate.getBlock();

		if (blockState != iblockstate)
		{
			return true;
		}

		if (block == this)
		{
			return false;
		}

		return block == this ? false : super.shouldSideBeRendered(blockState, blockAccess, pos, side);
	}
}
