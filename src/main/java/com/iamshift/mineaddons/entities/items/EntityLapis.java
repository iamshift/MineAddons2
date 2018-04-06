package com.iamshift.mineaddons.entities.items;

import java.util.List;

import com.iamshift.mineaddons.fluids.blocks.BlockForgottenWater;
import com.iamshift.mineaddons.init.ModItems;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class EntityLapis extends EntityItem
{
	public EntityLapis(World worldIn)
	{
		super(worldIn);
	}

	public EntityLapis(World worldIn, double x, double y, double z, ItemStack stack) 
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

		int x = MathHelper.floor(this.posX);
		int y = MathHelper.floor((this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY) / 2.0D);
		int z = MathHelper.floor(this.posZ);

		BlockPos pos = new BlockPos(x, y, z);

		IBlockState state = this.world.getBlockState(pos);

		if(state.getBlock() instanceof BlockForgottenWater && state.getBlock().getMetaFromState(state) == 0)
		{
			final ItemStack item = this.getItem();

			final AxisAlignedBB region = new AxisAlignedBB( this.posX - 1, this.posY - 1, this.posZ - 1, this.posX + 1, this.posY + 1, this.posZ + 1 );
			final List<Entity> entities = this.world.getEntitiesWithinAABBExcludingEntity(this, region);

			EntityItem cursedLapis = null;

			for(final Entity e : entities)
			{
				if(e instanceof EntityItem && !e.isDead)
				{
					final ItemStack otherItem = ((EntityItem) e).getItem();

					if(!otherItem.isEmpty())
					{
						if(ItemStack.areItemsEqual( otherItem, new ItemStack(ModItems.Lapis, otherItem.getCount(), 1)))
							cursedLapis = (EntityItem) e;
					}
				}
			}
			
			if(cursedLapis != null)
			{
				this.getItem().shrink(1);
				cursedLapis.getItem().shrink(1);

				if(this.getItem().getCount() <= 0)
					this.setDead();

				if(cursedLapis.getItem().getCount() <= 0)
					cursedLapis.setDead();

				InventoryHelper.spawnItemStack(this.world, this.posX, this.posY, this.posZ, new ItemStack(ModItems.Lapis, 1, 2));
			}
		}
	}
}
