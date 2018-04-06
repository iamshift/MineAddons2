package com.iamshift.mineaddons.init;

import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.network.PacketFly;
import com.iamshift.mineaddons.network.PacketToggle;

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
	}
	
	private static void registerMessage(Class handler, Class type, Side side)
	{
		INSTANCE.registerMessage(handler, type, id++, side);
	}
}
