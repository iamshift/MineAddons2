package com.iamshift.mineaddons.items;

import java.util.List;

import com.iamshift.mineaddons.entities.boss.EntityBoss;
import com.iamshift.mineaddons.init.ModItems;
import com.iamshift.mineaddons.interfaces.IUncapturable;
import com.iamshift.mineaddons.utils.NoTargetHelper;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
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
		tooltip.add(TextFormatting.GREEN + "Right click on a Mob to turn him peaceful.");
		tooltip.add(TextFormatting.GREEN + "If used on a Shulker will create a Brainless Shulker.");
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

			if(target instanceof EntityLiving && !(target instanceof EntityWither) && !(target instanceof EntityDragon) && !(target instanceof IUncapturable))
			{
				EntityLiving l = (EntityLiving) target;
				if(l.targetTasks.taskEntries.size() > 0)
				{
					NoTargetHelper.removeTargetTasks(l);
					l.addTag("notarget");

					if(!playerIn.capabilities.isCreativeMode)
						stack.shrink(1);
					
					return true;
				}
			}
		}

		return false;
	}
}
