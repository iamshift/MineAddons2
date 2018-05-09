package com.iamshift.mineaddons.items;

import java.util.ArrayList;
import java.util.List;

import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.entities.EntityBlazelier;
import com.iamshift.mineaddons.init.ModBlocks;
import com.iamshift.mineaddons.init.ModItems;
import com.iamshift.mineaddons.interfaces.ISpawner;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ItemBlazelier extends ItemBase implements ISpawner
{
	public ItemBlazelier(String name)
	{
		super(name);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(!worldIn.isRemote)
		{
			ItemStack stack = player.getHeldItem(hand);
			BlockPos spawnPos = pos.offset(facing);
			AxisAlignedBB bb = new AxisAlignedBB(spawnPos);

			if(worldIn.checkNoEntityCollision(bb))
			{
				EntityBlazelier blazelier = (EntityBlazelier) spawnCreature(worldIn, new ResourceLocation(Refs.ID, "blazelier"), (double)spawnPos.getX() + .5D, (double)spawnPos.getY() + .5D, (double)spawnPos.getZ() + .5D, true);
				blazelier.setHealth(blazelier.getMaxHealth());
				blazelier.enablePersistence();
				
				Block blk = ModBlocks.InvLight;
				BlockPos p1 = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
				blk.setLightLevel(1.0F);
				IBlockState state = blk.getDefaultState();
				worldIn.setBlockState(p1, state);
				
				if(!player.capabilities.isCreativeMode)
					stack.shrink(1);
			}
		}

		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add(TextFormatting.DARK_AQUA + "Summons a nice chandelier!");
	}

	@Override
	public List<IRecipe> getRecipe()
	{
		List<IRecipe> list = new ArrayList<IRecipe>();
		list.add(
				new ShapedOreRecipe(new ResourceLocation(Refs.ID), 
				new ItemStack(this, 1), 
				new Object[] {
				" C ", "BBB", " B ", 
				'C', new ItemStack(ModItems.DarkCore, 1, 0), 
				'B', new ItemStack(Items.BLAZE_ROD, 1, 0)
				}).setRegistryName(new ResourceLocation(Refs.ID, "blazelier"))
				);

		return list;
	}
}
