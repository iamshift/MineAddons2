package com.iamshift.mineaddons.events;

import com.iamshift.mineaddons.blocks.tiles.TileMover;
import com.iamshift.mineaddons.core.Config;
import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.fluids.blocks.BlockCursedWater;
import com.iamshift.mineaddons.fluids.blocks.BlockSacredWater;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Refs.ID)
public class BlockEvents
{
	@SubscribeEvent
	public static void createFluidSource(BlockEvent.CreateFluidSourceEvent event)
	{
		if(!Config.WaterSource)
			return;

		if(event.getState().getBlock() instanceof BlockSacredWater)
		{
			event.setResult(Event.Result.ALLOW);
			return;
		}

		if(event.getState().getBlock() instanceof BlockCursedWater)
		{
			event.setResult(Event.Result.ALLOW);
			return;
		}
	}

	@SubscribeEvent
	public static void changeCarpetColor(RightClickBlock event)
	{
		if(Config.CarpetColorChange)
		{
			if(event.getWorld().getBlockState(event.getPos()).getBlock() instanceof BlockCarpet)
			{
				if(event.getItemStack().getItem() instanceof ItemDye)
				{
					IBlockState carpet = event.getWorld().getBlockState(event.getPos());
					ItemStack dye = event.getItemStack();
					EnumDyeColor color = EnumDyeColor.byDyeDamage(dye.getItemDamage());

					if(((EnumDyeColor)carpet.getValue(BlockCarpet.COLOR)) != color)
					{
						IBlockState newState = carpet.getBlock().getStateForPlacement(event.getWorld(), event.getPos(), event.getFace(), (float)event.getHitVec().x, (float)event.getHitVec().y, (float)event.getHitVec().z, color.getMetadata(), event.getEntityPlayer(), event.getHand());
						event.getWorld().setBlockState(event.getPos(), newState);

						if(!event.getEntityPlayer().capabilities.isCreativeMode)
							dye.shrink(1);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onBlockLeftClick(LeftClickBlock event)
    {
		if(!Config.WirelessMover)
			return;
		
		ItemStack stack = event.getItemStack();
		EntityPlayer playerIn = event.getEntityPlayer();
		World worldIn = playerIn.world;
    	
    	if (stack.getItem() instanceof ItemBlock && Block.getBlockFromItem(stack.getItem()) == Blocks.REDSTONE_TORCH)
    	{
    		TileEntity te = worldIn.getTileEntity(event.getPos());
            
            if (te instanceof TileMover)
            {
            	((TileMover) te).addRange(-1);
            	playerIn.sendStatusMessage(new TextComponentString(TextFormatting.GREEN + "The mover's range is now " + TextFormatting.WHITE + ((TileMover) te).getRange()), true);
            	
            	event.setCanceled(true);
            	return;
            }
    	}
    }
	
	@SubscribeEvent
	public static void onBlockRightClic(RightClickBlock event)
	{
		if(!Config.WirelessMover)
			return;
		
		if(event.getEntityPlayer().isSneaking())
			return;
		
		if(event.getItemStack().getItem() instanceof ItemBlock && Block.getBlockFromItem(event.getItemStack().getItem()) == Blocks.REDSTONE_TORCH)
		{
			if(event.getEntityPlayer().world.getTileEntity(event.getPos()) instanceof TileMover)
			{
				((TileMover)event.getEntityPlayer().world.getTileEntity(event.getPos())).addRange(1);
				event.getEntityPlayer().sendStatusMessage(new TextComponentString(TextFormatting.GREEN + "The mover's range is now " + TextFormatting.WHITE + ((TileMover)event.getEntityPlayer().world.getTileEntity(event.getPos())).getRange()), true);
				
				event.setCanceled(true);
            	return;
			}
		}
	}
}
