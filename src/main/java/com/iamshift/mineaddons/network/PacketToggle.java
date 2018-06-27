package com.iamshift.mineaddons.network;

import com.iamshift.mineaddons.events.ArmorEvents;
import com.iamshift.mineaddons.init.ModPotions;
import com.iamshift.mineaddons.items.armors.ItemUltimateArmor;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketToggle implements IMessage
{
	private int status = 0;
	
	/******************
	   Type:
	   0 - Rocket
	   1 - Nigh Vision
	 ******************/
	private int type;

	public PacketToggle() {}

	public PacketToggle(int flag, int type)
	{
		this.status = flag;
		this.type = type;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		status = buf.readInt();
		type = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(status);
		buf.writeInt(type);
	}

	public static class Toggle implements IMessageHandler<PacketToggle, IMessage>
	{
		@Override
		public IMessage onMessage(PacketToggle message, MessageContext ctx)
		{
			IThreadListener main = ctx.getServerHandler().player.getServerWorld();
			main.addScheduledTask(() -> {

				EntityPlayerMP sPlayer = ctx.getServerHandler().player;
				
				ItemStack stack;
				NBTTagCompound compound;
				Potion potion;
				
				switch(message.type) 
				{
					case 0:
						stack = sPlayer.getItemStackFromSlot(EntityEquipmentSlot.FEET);
						compound = stack.getSubCompound("Rocket");
						if(message.status == 0)
						{
							compound.setInteger("Active", 1);
							sPlayer.sendStatusMessage(new TextComponentTranslation("text.rocketboots.toggle.on"), true);
							sPlayer.addPotionEffect(new PotionEffect(ModPotions.PotionRocket, 999999, 0));
						}
						else if(message.status == 1)
						{
							compound.setInteger("Active", 0);
							sPlayer.sendStatusMessage(new TextComponentTranslation("text.rocketboots.toggle.off"), true);
							
							if(sPlayer.isPotionActive(ModPotions.PotionRocket))
								sPlayer.removePotionEffect(ModPotions.PotionRocket);
						}
						
						break;
					case 1:
						stack = sPlayer.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
						compound = stack.getSubCompound("ArmorEffect");
						
						potion = ArmorEvents.armorEffects.get(EntityEquipmentSlot.HEAD).getPotion();
						
						if(message.status == 0)
						{
							compound.setInteger("Active", 1);
							sPlayer.sendStatusMessage(new TextComponentTranslation("text.vision.toggle.on"), true);
						}
						else if(message.status == 1)
						{
							compound.setInteger("Active", 0);
							sPlayer.sendStatusMessage(new TextComponentTranslation("text.vision.toggle.off"), true);
							
							if(sPlayer.isPotionActive(potion))
								sPlayer.removePotionEffect(potion);
						}
						
						break;
				}

			});

			return null;
		}
	}
}
