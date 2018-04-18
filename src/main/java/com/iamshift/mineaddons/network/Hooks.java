package com.iamshift.mineaddons.network;

import com.iamshift.mineaddons.init.ModNetwork;
import com.iamshift.mineaddons.utils.ElytraHelper;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

public class Hooks
{
	public static void update(EntityLivingBase entity)
	{
		boolean flag = entity.isElytraFlying();
		
		if(flag && !entity.onGround && !entity.isRiding())
		{
			ItemStack stack = ElytraHelper.findChest(entity);
			
			if(!stack.isEmpty() && ElytraHelper.isValid(stack))
				flag = true;
			else
				flag = false;
		}
		else
			flag = false;
		
		if(!entity.world.isRemote && entity instanceof EntityPlayer)
		{
			EntityPlayerMP playerMP = (EntityPlayerMP) entity;
			
			if(flag)
			{
				playerMP.setElytraFlying();
			}
			else
				playerMP.clearElytraFlying();
		}
	}
	
	
	public static void updateClient(EntityLivingBase entity)
	{
		ItemStack stack = ElytraHelper.findChest(entity);
		
		if(!stack.isEmpty() && ElytraHelper.isValid(stack))
		{
			ModNetwork.INSTANCE.sendToServer(new PacketFly());
		}
	}
}
