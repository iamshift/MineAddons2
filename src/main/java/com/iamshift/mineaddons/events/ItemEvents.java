package com.iamshift.mineaddons.events;

import com.iamshift.mineaddons.core.Refs;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFirework;
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
}
