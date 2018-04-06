package com.iamshift.mineaddons.items;

import java.util.List;

import com.iamshift.mineaddons.init.ModItems;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemAncientEssence extends ItemBase
{
	public ItemAncientEssence(String name) 
	{
		super(name);
	}

	@Override
	public boolean hasEffect(ItemStack stack) 
	{
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) 
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add(TextFormatting.GREEN + "Right click on a Shulker to use.");
	}
	
	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) 
	{
		if(!target.world.isRemote)
		{
			if(target instanceof EntityShulker)
			{
				target.setDropItemsWhenDead(false);
				target.setDead();
				target.entityDropItem(new ItemStack(ModItems.BrainlessShulkerEgg, 1, 5), 0.0F);

				if(!playerIn.capabilities.isCreativeMode)
					stack.shrink(1);

				return true;
			}
		}
		
		return false;
	}
}
