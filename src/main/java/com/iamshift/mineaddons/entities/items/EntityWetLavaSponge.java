package com.iamshift.mineaddons.entities.items;

import com.iamshift.mineaddons.init.ModBlocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityWetLavaSponge extends EntityItem
{
	public EntityWetLavaSponge(World worldIn)
	{
		super(worldIn);
	}

	public EntityWetLavaSponge(World worldIn, double x, double y, double z, ItemStack stack) 
	{
		super(worldIn, x, y, z, stack);
	}
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		
		if(this.world.isRemote)
			return;

		if(this.isDead)
			return;

		Item item = this.getItem().getItem();

		int x = MathHelper.floor(this.posX);
		int y = MathHelper.floor((this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY) / 2.0D);
		int z = MathHelper.floor(this.posZ);

		BlockPos pos = new BlockPos(x, y, z);

		IBlockState state = this.world.getBlockState(pos);
		Material mat = state.getMaterial();
		final ItemStack stack = this.getItem();

		if(mat.isLiquid() && mat == Material.WATER && state.getBlock().getMetaFromState(state) == 0)
		{
			this.world.setBlockToAir(pos);
			InventoryHelper.spawnItemStack(this.world, this.posX, this.posY, this.posZ, new ItemStack(Item.getItemFromBlock(ModBlocks.LavaSponge), 1, 0));
			stack.shrink(1);
			return;
		}
		
		if(stack.getCount() <= 0)
			this.setDead();
	}
}
