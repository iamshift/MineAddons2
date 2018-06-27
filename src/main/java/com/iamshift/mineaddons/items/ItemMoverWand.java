package com.iamshift.mineaddons.items;

import java.util.ArrayList;
import java.util.List;

import com.iamshift.mineaddons.blocks.tiles.TileMover;
import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.init.ModBlocks;

import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ItemMoverWand extends ItemBase
{
	public ItemMoverWand(String name)
	{
		super(name);
		setMaxStackSize(1);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack)
	{
		if(stack.getTagCompound() != null)
			return stack.getTagCompound().getBoolean("linking");
			
		return false;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		if(playerIn.isSneaking())
		{
			ItemStack stack = playerIn.getHeldItem(handIn);
			if(stack.getTagCompound() != null && stack.getTagCompound().getBoolean("linking"))
				stack.getTagCompound().setBoolean("linking", false);
		}
			
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = player.getHeldItem(hand);
		IBlockState state = worldIn.getBlockState(pos);
		
		if(!stack.hasTagCompound())
		{
			if(BlockPistonBase.canPush(state, worldIn, pos, facing.getOpposite(), false, facing.getOpposite()))
			{
				stack.setTagCompound(new NBTTagCompound());
			}
			else
			{
				player.sendStatusMessage(new TextComponentString(TextFormatting.RED + "You can only select blocks that a Piston can push"), true);
				return EnumActionResult.FAIL;
			}
		}
		
		NBTTagCompound compound = stack.getTagCompound();
		boolean linking = compound.getBoolean("linking");
		
		if(linking)
		{
			if(state.getBlock() == ModBlocks.Mover)
			{
				BlockPos linkTo = new BlockPos(compound.getInteger("tX"), compound.getInteger("tY"), compound.getInteger("tZ"));
				if(!linkTo.equals(pos))
				{
					TileEntity te = worldIn.getTileEntity(pos);
					if(te != null && te instanceof TileMover)
					{
						((TileMover)te).setTarget(linkTo, compound.getInteger("tF"));
					}
				}
				
				compound.setBoolean("linking", false);
			}
			
			return EnumActionResult.SUCCESS;
		}
		else
		{
			if(state.getBlock() == ModBlocks.Mover)
			{
				TileEntity te = worldIn.getTileEntity(pos);
				if(te != null && te instanceof TileMover)
				{
					((TileMover)te).clearTarget();
				}
			}
			else
			{
				compound.setBoolean("linking", true);
				compound.setInteger("tX", pos.getX());
				compound.setInteger("tY", pos.getY());
				compound.setInteger("tZ", pos.getZ());
				compound.setInteger("tF", facing.getIndex());
			}
			
			return EnumActionResult.SUCCESS;
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		
		tooltip.add(TextFormatting.GREEN + "Connects block to move with a Wireless Block Mover.");
		if(stack.getTagCompound() != null && stack.getTagCompound().getBoolean("linking"))
		{
			tooltip.add("");
			
			NBTTagCompound tag = stack.getTagCompound();
			BlockPos pos = new BlockPos(tag.getInteger("tX"), tag.getInteger("tY"), tag.getInteger("tZ"));
			tooltip.add(TextFormatting.DARK_AQUA + "Target Block: " + TextFormatting.WHITE + worldIn.getBlockState(pos).getBlock().getLocalizedName());
			tooltip.add(TextFormatting.DARK_AQUA + "Position: " + TextFormatting.WHITE + pos.getX() + " " + pos.getY() + " " + pos.getZ());
			tooltip.add(TextFormatting.DARK_AQUA + "Direction: " + TextFormatting.WHITE + EnumFacing.values()[tag.getInteger("tF")].getOpposite().getName());

			tooltip.add("");
		}
	}
	
	@Override
	public List<IRecipe> getRecipe()
	{
		List<IRecipe> list = new ArrayList<IRecipe>();
		list.add(
				new ShapedOreRecipe(new ResourceLocation(Refs.ID), 
						new ItemStack(this, 1), 
						new Object[] {
								"  R", " S ", "S  ", 
								'R', new ItemStack(Items.REDSTONE, 1, 0),
								'S', new ItemStack(Items.STICK, 1, 0)
				}).setRegistryName(new ResourceLocation(Refs.ID, "mover_wand"))
				);

		return list;
	}
}
