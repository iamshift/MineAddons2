package com.iamshift.mineaddons.integration;

import java.util.ArrayList;
import java.util.List;

import com.iamshift.mineaddons.integration.crafttweaker.CTAnvilHandler;

import crafttweaker.CraftTweakerAPI;

public class CraftTweaker
{
	private static final List<Runnable> QUEUE = new ArrayList<>();
	
	public static void preInit()
	{
		CraftTweakerAPI.registerClass(CTAnvilHandler.class);
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
