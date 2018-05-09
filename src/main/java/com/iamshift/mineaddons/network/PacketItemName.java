package com.iamshift.mineaddons.network;

import com.iamshift.mineaddons.blocks.containers.ContainerForgottenAnvil;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketItemName implements IMessage
{
	private String name;
	
	public PacketItemName() {}
	
	public PacketItemName(String name)
	{
		this.name = name;
	}
	
	public void fromBytes(io.netty.buffer.ByteBuf buf) 
	{
		name = ByteBufUtils.readUTF8String(buf);
	}
	
	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeUTF8String(buf, name);
	}
	
	public static class Rename implements IMessageHandler<PacketItemName, IMessage>
	{
		@Override
		public IMessage onMessage(PacketItemName message, MessageContext ctx)
		{
			IThreadListener main = ctx.getServerHandler().player.getServerWorld();
			main.addScheduledTask(() -> {
				ContainerForgottenAnvil anvil = (ContainerForgottenAnvil) ctx.getServerHandler().player.openContainer;
				anvil.updateItemName(message.name);
			});
			
			return null;
		}
	}
}
