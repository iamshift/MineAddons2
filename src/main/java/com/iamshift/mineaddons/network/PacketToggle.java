package com.iamshift.mineaddons.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketToggle implements IMessage
{
	private int status = 0;

	public PacketToggle() {}

	public PacketToggle(int flag)
	{
		this.status = flag;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		status = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(status);
	}

	public static class Toggle implements IMessageHandler<PacketToggle, IMessage>
	{
		@Override
		public IMessage onMessage(PacketToggle message, MessageContext ctx)
		{
			IThreadListener main = ctx.getServerHandler().player.getServerWorld();
			main.addScheduledTask(() -> {

				EntityPlayerMP sPlayer = ctx.getServerHandler().player;

				ItemStack stack = sPlayer.getItemStackFromSlot(EntityEquipmentSlot.FEET);

				if(stack.hasTagCompound() && stack.getTagCompound().hasKey("Rocket"))
				{
					NBTTagCompound compound = stack.getSubCompound("Rocket");

					int flag = compound.getInteger("Active");

					if(flag == 1)
					{
						compound.setInteger("Active", 0);
						sPlayer.sendMessage(new TextComponentTranslation("text.rocketboots.toggle.off"));
					}
					else if(flag == 0)
					{
						compound.setInteger("Active", 1);
						sPlayer.sendMessage(new TextComponentTranslation("text.rocketboots.toggle.on"));
					}
					
				}

			});

			return null;
		}
	}
}
