package com.iamshift.mineaddons.blocks;

import net.minecraft.block.properties.IProperty;
import net.minecraft.item.ItemStack;

public class BlockSandStoneHalfSlab extends BlockSandStoneSlab
{
	public BlockSandStoneHalfSlab(String name)
	{
		super(name);
	}
	
	@Override
	public boolean isDouble()
	{
		return false; 
	}
}
