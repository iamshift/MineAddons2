package com.iamshift.mineaddons.init;

import com.iamshift.mineaddons.core.Config;
import com.iamshift.mineaddons.integration.Tinkers;
import com.iamshift.mineaddons.integration.Waila;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;

public class ModIntegrations
{
	public static void init()
	{
		if(Loader.isModLoaded("tconstruct") && Config.Tinker)
			Tinkers.init();
		
		if(FMLCommonHandler.instance().getSide().isClient())
			if(Loader.isModLoaded("waila"))
				Waila.init();
	}
}
