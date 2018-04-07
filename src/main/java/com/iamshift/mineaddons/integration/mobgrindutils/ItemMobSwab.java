package com.iamshift.mineaddons.integration.mobgrindutils;

import java.util.List;

import javax.annotation.Nullable;

import com.iamshift.mineaddons.MineAddons;
import com.iamshift.mineaddons.entities.boss.EntityBoss;
import com.iamshift.mineaddons.interfaces.IUncapturable;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.ModItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMobSwab extends mob_grinding_utils.items.ItemMobSwab
{
	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) 
	{
		if(target instanceof IUncapturable)
			return false;
		else
			return super.itemInteractionForEntity(stack, player, target, hand);
	}
}
