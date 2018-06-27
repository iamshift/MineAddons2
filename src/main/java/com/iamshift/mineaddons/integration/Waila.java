package com.iamshift.mineaddons.integration;

import com.iamshift.mineaddons.blocks.BlockMover;
import com.iamshift.mineaddons.blocks.BlockSoul;
import com.iamshift.mineaddons.integration.waila.WailaProviderInfusedSoul;
import com.iamshift.mineaddons.integration.waila.WailaProviderMover;

import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class Waila
{
	public static void init()
	{
		FMLInterModComms.sendMessage("waila", "register", Waila.class.getName() + ".callbackReg");
	}
	
	public static void callbackReg(IWailaRegistrar reg)
	{
		reg.registerBodyProvider(new WailaProviderInfusedSoul(), BlockSoul.class);
		reg.registerBodyProvider(new WailaProviderMover(), BlockMover.class);
	}
}
