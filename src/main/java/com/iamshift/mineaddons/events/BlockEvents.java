package com.iamshift.mineaddons.events;

import com.iamshift.mineaddons.core.Config;
import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.fluids.blocks.BlockCursedWater;
import com.iamshift.mineaddons.fluids.blocks.BlockSacredWater;

import net.minecraft.block.BlockCarpet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
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
