package com.iamshift.mineaddons.items;

import java.util.ArrayList;
import java.util.List;

import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.init.ModItems;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ItemSuperNameTag extends ItemBase
{
	public ItemSuperNameTag(String name) 
	{
		super(name);
	}

	@Override
	public boolean hasEffect(ItemStack stack) 
	{
		return true;
	}
	
	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) 
	{
		if(!stack.hasDisplayName())
			return false;
		else if(target instanceof EntityLiving)
		{
			EntityLiving entity = (EntityLiving) target;
			entity.setCustomNameTag(stack.getDisplayName());
			entity.enablePersistence();
			entity.setAlwaysRenderNameTag(true);
            stack.setCount(stack.getCount() - 1);
			return true;
		}
		else
			return super.itemInteractionForEntity(stack, playerIn, target, hand);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add(TextFormatting.DARK_AQUA + "Makes entity name always visible.");
	}
	
	@Override
	public List<IRecipe> getRecipe()
	{
		List<IRecipe> list = new ArrayList<IRecipe>();
		list.add(
				new ShapelessOreRecipe(new ResourceLocation(Refs.ID), 
				new ItemStack(this, 1), 
				new Object[] {
				new ItemStack(Items.NAME_TAG, 1, 0), 
				new ItemStack(Items.GOLDEN_APPLE, 1, 0)
				}).setRegistryName(new ResourceLocation(Refs.ID, "super_name_tag"))
				);
		
		return list;
	}
}
