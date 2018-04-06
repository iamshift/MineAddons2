package com.iamshift.mineaddons.items;

import java.util.List;

import com.iamshift.mineaddons.MineAddons;
import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.entities.EntityBrainlessShulker;
import com.iamshift.mineaddons.interfaces.ISpawner;
import com.iamshift.mineaddons.items.ItemEnchanted.Types;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBrainlessShulkerEgg extends ItemBase implements ISpawner
{
	public ItemBrainlessShulkerEgg(String name) 
	{
		super(name);
		setHasSubtypes(true);
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) 
	{
		if(this.isInCreativeTab(tab))
		{
			for(int i = 0; i < EnumDyeColor.values().length; i++)
				items.add(new ItemStack(this, 1, i));
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) 
	{
		return getUnlocalizedName() + "." + EnumDyeColor.values()[stack.getItemDamage()].getName();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels()
	{
		for(int i = 0; i < EnumDyeColor.values().length; i++)
			MineAddons.proxy.registerVariantRenderer(this, i, "brainless_shulker_egg", "inventory");
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) 
	{
		if(!worldIn.isRemote)
		{
			ItemStack stack = player.getHeldItem(hand);
			BlockPos spawnPos = pos.offset(facing);
			
			EntityBrainlessShulker shulker = (EntityBrainlessShulker) spawnCreature(worldIn, new ResourceLocation(Refs.ID, "brainless_shulker"), (double)spawnPos.getX() + .5D, (double)spawnPos.getY() + .5D, (double)spawnPos.getZ() + .5D, false);
			shulker.setHealth(shulker.getMaxHealth());
			shulker.enablePersistence();
			shulker.setColor(EnumDyeColor.byDyeDamage(stack.getItemDamage()));
			
			if(!player.capabilities.isCreativeMode)
				stack.shrink(1);
		}
		
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add(TextFormatting.DARK_AQUA + "Spawns a Brainless Shulker.");
		
		
		String color = new TextComponentTranslation("shulker." + EnumDyeColor.byDyeDamage(stack.getItemDamage())).getFormattedText();
		
		tooltip.add(TextFormatting.WHITE + "Color: " + EnumDyeColor.byDyeDamage(stack.getItemDamage()).chatColor + color);
	}
}
