package com.iamshift.mineaddons.items;

import com.iamshift.mineaddons.MineAddons;
import com.iamshift.mineaddons.entities.items.EntityLapis;
import com.iamshift.mineaddons.items.ItemEnchanted.Types;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemLapis extends ItemBase
{
	private String thrower;
	
	public ItemLapis(String name)
	{
		super(name);
		setHasSubtypes(true);
	}
	
	@Override
	public boolean hasEffect(ItemStack stack)
	{
		if(stack.getItemDamage() == 2)
			return true;
		
		return false;
	}
	
	@Override
	public boolean hasCustomEntity(ItemStack stack)
	{
		if(stack.getItemDamage() == 0)
			return true;
		
		return false;
	}
	
	@Override
	public Entity createEntity(World world, Entity location, ItemStack itemstack)
	{
		EntityLapis entity = new EntityLapis(world, location.posX, location.posY, location.posZ, itemstack);
		
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
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if(this.isInCreativeTab(tab))
		{
			for(int i = 0; i < Types.values().length; i++)
				items.add(new ItemStack(this, 1, i));
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return getUnlocalizedName() + "." + Types.values()[stack.getItemDamage()].getName();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels()
	{
		for(int i = 0; i < Types.values().length; i++)
			MineAddons.proxy.registerVariantRenderer(this, i, "lapis_" + Types.values()[i], "inventory");
	}
	
	static enum Types
	{
		SACRED("sacred"),
		CURSED("cursed"),
		FORGOTTEN("forgotten");
		
		private String name;
		
		private Types(String name)
		{
			this.name = name;
		}
		
		public String getName()
		{
			return this.name;
		}
		
		@Override
		public String toString()
		{
			return getName();
		}
	}
}
