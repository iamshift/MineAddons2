package com.iamshift.mineaddons.entities.items;

import com.iamshift.mineaddons.init.ModBlocks;
import com.iamshift.mineaddons.items.ItemRainbowBottle;
import com.iamshift.mineaddons.items.ItemWitherDust;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class EntityWaterChanger extends EntityItem
{
	public EntityWaterChanger(World worldIn) 
	{
		super(worldIn);
	}

	public EntityWaterChanger(World worldIn, double x, double y, double z, ItemStack stack) 
	{
		super(worldIn, x, y, z, stack);
	}
	
	@Override
	public void onUpdate() 
	{
		super.onUpdate();
		
		Item item = this.getItem().getItem();
		
		if(this.getThrower() == null)
			return;
		
		int x = MathHelper.floor(this.posX);
		int y = MathHelper.floor((this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY) / 2.0D);
		int z = MathHelper.floor(this.posZ);
		
		BlockPos pos = new BlockPos(x, y, z);

		IBlockState state = this.world.getBlockState(pos);
		Material mat = state.getMaterial();
		Biome biome = this.world.getBiome(pos);
		
		IBlockState stateAbove = this.world.getBlockState(new BlockPos(x, y+1, z));
		if(stateAbove != Blocks.AIR.getDefaultState())
			return;
		
		if(mat.isLiquid() 
				&& mat == Material.WATER 
				&& state.getBlock().getUnlocalizedName().equals("tile.water") 
				&& state.getBlock().getMetaFromState(state) == 0 
				&& biome != Biomes.OCEAN 
				&& biome != Biomes.DEEP_OCEAN)
		{
			if(item instanceof ItemRainbowBottle)
			{
				this.world.setBlockState(pos, ModBlocks.SacredWater.getDefaultState());
				
				this.getItem().shrink(1);
				
				if(this.getItem().getCount() <= 0)
					this.setDead();
				
				return;
			}
			
			if(item instanceof ItemWitherDust)
			{
				this.world.setBlockState(pos, ModBlocks.CursedWater.getDefaultState());

				this.getItem().shrink(1);
				
				if(this.getItem().getCount() <= 0)
					this.setDead();
				
				return;
			}
		}
	}
}
