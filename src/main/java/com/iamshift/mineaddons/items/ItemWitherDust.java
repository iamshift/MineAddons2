package com.iamshift.mineaddons.items;

import java.util.List;

import com.iamshift.mineaddons.entities.items.EntityWaterChanger;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemWitherDust extends ItemBase
{
	private String thrower;
	
	public ItemWitherDust(String name) 
	{
		super(name);
	}
	
	@Override
	public int getEntityLifespan(ItemStack itemStack, World world) 
	{
		return Integer.MAX_VALUE;
	}
	
	@Override
	public boolean isDamageable() 
	{
		return false;
	}
	
	@Override
	public boolean isDamaged(ItemStack stack) 
	{
		return false;
	}
	
	@Override
	public boolean hasCustomEntity(ItemStack stack) 
	{
		return true;
	}
	
	@Override
	public Entity createEntity(World world, Entity location, ItemStack itemstack) 
	{
		EntityWaterChanger entity = new EntityWaterChanger(world, location.posX, location.posY, location.posZ, itemstack);

		entity.motionX = location.motionX;
		entity.motionY = location.motionY;
		entity.motionZ = location.motionZ;
		
		entity.setPickupDelay(40);

		entity.setThrower(thrower);
		
		thrower = null;
		
		return entity;
	}
	
	@Override
	public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player) 
	{
		this.thrower = player.getName();
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) 
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add(TextFormatting.GREEN + "Throw on water to see the magic.");
		tooltip.add(TextFormatting.RED + "Will not work in Ocean or Deep Ocean!");
	}
}
