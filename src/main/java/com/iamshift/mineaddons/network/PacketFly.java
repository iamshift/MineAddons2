package com.iamshift.mineaddons.network;

import com.iamshift.mineaddons.utils.ElytraHelper;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketFly implements IMessage
{
	public PacketFly() {}
	
	@Override
	public void fromBytes(ByteBuf buf) {}

	@Override
	public void toBytes(ByteBuf buf) {}

	public static class Fly implements IMessageHandler<PacketFly, IMessage>
	{
		@Override
		public IMessage onMessage(PacketFly message, MessageContext ctx)
		{
			IThreadListener main = ctx.getServerHandler().player.getServerWorld();
			main.addScheduledTask(() -> {
				
				EntityPlayerMP sPlayer = ctx.getServerHandler().player;
				
				if(!sPlayer.onGround && sPlayer.motionY < 0.0D && !sPlayer.isElytraFlying() && !sPlayer.isInWater())
				{
					ItemStack stack = ElytraHelper.findChest(sPlayer);
					
					if(stack != ItemStack.EMPTY && ElytraHelper.isValid(stack))
					{
						sPlayer.setElytraFlying();
					}
				}
				else
				{
					sPlayer.clearElytraFlying();
				}
			});
			
			return null;
		}
	}
}
