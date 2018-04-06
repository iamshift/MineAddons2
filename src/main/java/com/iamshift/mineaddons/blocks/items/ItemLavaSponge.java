package com.iamshift.mineaddons.blocks.items;

import com.iamshift.mineaddons.entities.items.EntityWetLavaSponge;
import com.iamshift.mineaddons.utils.ItemBlockVariants;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemLavaSponge extends ItemBlockVariants
{
	public ItemLavaSponge(Block block)
	{
		super(block);
	}
	
	@Override
	public boolean hasCustomEntity(ItemStack stack) 
	{
		if(stack.getItemDamage() == 1)
			return true;
		
		return false;
	}

	@Override
	public Entity createEntity(World world, Entity location, ItemStack itemstack)
	{
		EntityWetLavaSponge item = new EntityWetLavaSponge(world, location.posX, location.posY, location.posZ, itemstack);
		
		item.motionX = location.motionX;
		item.motionY = location.motionY;
		item.motionZ = location.motionZ;
		
		item.setPickupDelay(40);
		
		return item;
	}
}
