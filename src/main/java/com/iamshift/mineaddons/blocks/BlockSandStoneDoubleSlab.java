package com.iamshift.mineaddons.blocks;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockSandStoneDoubleSlab extends BlockSandStoneSlab
{
	public BlockSandStoneDoubleSlab(String name)
	{
		super(name);
	}
	
	@Override
	public boolean isDouble() 
	{
		return true;
	}
}
