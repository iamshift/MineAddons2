package com.iamshift.mineaddons.events;

import java.util.Set;

import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.items.tools.ItemFiberAxe;
import com.iamshift.mineaddons.items.tools.ItemFiberPickaxe;
import com.iamshift.mineaddons.items.tools.ItemFiberShovel;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemFirework;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Refs.ID)
public class ItemEvents
{
	@SubscribeEvent
	public static void onFireworkUse(RightClickItem event) 
	{
		if(event.getItemStack() == null)
			return;

		EntityPlayer player = event.getEntityPlayer();
		if(player == null)
			return;

		if((event.getItemStack().getItem() instanceof ItemFirework))
		{	
			if(player.isSneaking() && !player.isElytraFlying())
			{
				player.addVelocity(0D, 3D, 0D);
				player.playSound(SoundEvents.ENTITY_FIREWORK_LAUNCH, 1.0F, 1.0F);

				if(!player.capabilities.isCreativeMode)
					event.getItemStack().shrink(1);
			}

			return;
		}
	}
	
	@SubscribeEvent
	public static void onFiberToolUse(PlayerEvent.BreakSpeed event)
	{
		ItemStack stack = event.getEntityPlayer().getHeldItemMainhand();
		if(!(stack.getItem() instanceof ItemTool))
			return;
		
		if(!(stack.getItem() instanceof ItemFiberPickaxe) || !(stack.getItem() instanceof ItemFiberAxe) || !(stack.getItem() instanceof ItemFiberShovel))
			return;
		
		IBlockState state = event.getState();
		Material mat = state.getMaterial();
		Block block = state.getBlock();
		float hardness = state.getBlockHardness(event.getEntityPlayer().world, event.getPos());
		
		Set<Block> EFFECTIVE_ON = stack.getItem() instanceof ItemFiberPickaxe ? ItemPickaxe.EFFECTIVE_ON : 
								  stack.getItem() instanceof ItemFiberAxe ? ItemAxe.EFFECTIVE_ON :
								  stack.getItem() instanceof ItemFiberShovel ? ItemSpade.EFFECTIVE_ON :
								  null; 
		
		
		if(mat == Material.IRON || mat == Material.ANVIL || mat == Material.ROCK)
		{
			event.setNewSpeed(hardness * 50.0F);
			return;
		}
		
		for(String type : ((ItemTool)stack.getItem()).getToolClasses(stack))
		{
			if(state.getBlock().isToolEffective(type, state))
			{
				event.setNewSpeed(hardness * 50.0F);
				return;
			}
		}
		
		if(EFFECTIVE_ON != null)
		{
			if(EFFECTIVE_ON.contains(state.getBlock()))
			{
				event.setNewSpeed(hardness * 50.0F);
				return;
			}
		}
		
		event.setNewSpeed(hardness * 1.0F);
	}
}
