package com.iamshift.mineaddons.integration;

import java.util.ArrayList;
import java.util.List;

import com.iamshift.mineaddons.integration.crafttweaker.CTAnvilHandler;
import com.iamshift.mineaddons.integration.crafttweaker.CTMobConversion;

import crafttweaker.CraftTweakerAPI;

public class CraftTweaker
{
	private static final List<Runnable> QUEUE = new ArrayList<>();
	
	public static void preInit()
	{
		CraftTweakerAPI.registerClass(CTAnvilHandler.class);
		CraftTweakerAPI.registerClass(CTMobConversion.class);
	}
	
	public static void postInit()
	{
		for(Runnable r : QUEUE)
			r.run();
		
		QUEUE.clear();
	}
	
	public static void queue(Runnable action)
	{
		QUEUE.add(action);
	}
}
