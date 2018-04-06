package com.iamshift.mineaddons.items;

import java.util.ArrayList;
import java.util.List;

import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.init.ModItems;
import com.iamshift.mineaddons.utils.TimeSkipHandler;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ItemTimeSkipClock extends ItemBase 
{
	private int timeskip = 1;
	private String time = "0:00";
	
	public ItemTimeSkipClock(String name) 
	{
		super(name);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) 
	{
		ItemStack stack = playerIn.getHeldItem(handIn);
		
		if(worldIn.isRemote)
			return new ActionResult(EnumActionResult.FAIL, stack);
		
		if(playerIn.isSneaking())
		{
			this.timeskip++;
			
			if(this.timeskip > 24)
				this.timeskip = 1;
			
			playerIn.sendMessage(new TextComponentString("Skiping " + this.timeskip + " hours."));
		}
		else
			TimeSkipHandler.startTimeSkip(this.timeskip, worldIn, playerIn);
		
		return new ActionResult(EnumActionResult.SUCCESS, stack);
	}
	
	@Override
	public boolean hasEffect(ItemStack stack) 
	{
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add(TextFormatting.WHITE + "Sneak + Right click to increase time skip.");
		tooltip.add(TextFormatting.GREEN + "Right click to start time skip.");
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) 
	{
		return super.getItemStackDisplayName(stack) + " [" + this.time + "]";
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) 
	{
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
		
		if(!worldIn.isRemote)
			return;
		
		this.time = convertTime(worldIn.getWorldTime());
	}
	
	private String convertTime(long time)
	{
		long worldTime = time % 24000L;
		long fixedTime = worldTime + 6000L;
		if(fixedTime >= 24000L) fixedTime -= 24000L;
		long allSecs = fixedTime / 20L;
		
		return String.format("%d:%02d", (long)Math.floor(allSecs / 50L), (long)Math.ceil(allSecs % 50L));
	}
	
	@Override
	public List<IRecipe> getRecipe()
	{
		List<IRecipe> list = new ArrayList<IRecipe>();
		list.add(
				new ShapedOreRecipe(new ResourceLocation(Refs.ID), 
				new ItemStack(this, 1), 
				new Object[] {
				" D ", "SCS", " D ", 
				'D', new ItemStack(Blocks.DIAMOND_BLOCK, 1, 0), 
				'S', new ItemStack(Items.NETHER_STAR, 1, 0),
				'C', new ItemStack(Items.CLOCK, 1, 0)
				}).setRegistryName(new ResourceLocation(Refs.ID, "time_skip_clock"))
				);
		
		return list;
	}
}
