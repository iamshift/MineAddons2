package com.iamshift.mineaddons.init;

import com.iamshift.mineaddons.MineAddons;
import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.network.PacketFly;
import com.iamshift.mineaddons.network.PacketItemName;
import com.iamshift.mineaddons.network.PacketToggle;
import com.iamshift.mineaddons.utils.GuiHandler;

import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class ModNetwork
{
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Refs.ID);
	
	private static int id = 0;
	
	public static void init()
	{
		registerMessage(PacketToggle.Toggle.class, PacketToggle.class, Side.SERVER);
		registerMessage(PacketFly.Fly.class, PacketFly.class, Side.SERVER);
		registerMessage(PacketItemName.Rename.class, PacketItemName.class, Side.SERVER);
		
		registerGui(MineAddons.instance, new GuiHandler());
	}
	
	private static void registerMessage(Class handler, Class type, Side side)
	{
		INSTANCE.registerMessage(handler, type, id++, side);
	}
	
	private static void registerGui(Object mod, IGuiHandler handler)
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(mod, handler);
	}
}
