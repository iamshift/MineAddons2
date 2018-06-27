package com.iamshift.mineaddons.init;

import com.iamshift.mineaddons.core.Config;
import com.iamshift.mineaddons.integration.CraftTweaker;
import com.iamshift.mineaddons.integration.Foundry;
import com.iamshift.mineaddons.integration.Tinkers;
import com.iamshift.mineaddons.integration.Waila;
import com.iamshift.mineaddons.integration.tinkers.TiCMaterial;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;

public class ModIntegrations
{
	public static void preInit()
	{
		if(Config.Tinker)
			TiCMaterial.setup();
		
		if(Config.CraftTweaker)
			CraftTweaker.preInit();
	}
	
	public static void init()
	{
		if(Config.Tinker)
			Tinkers.init();
			
		if(FMLCommonHandler.instance().getSide().isClient())
			if(Config.Waila)
				Waila.init();
		
		if(Config.Foundry)
			Foundry.init();
	}
	
	public static void postInit()
	{
		if(Config.CraftTweaker)
			CraftTweaker.postInit();
	}
}
