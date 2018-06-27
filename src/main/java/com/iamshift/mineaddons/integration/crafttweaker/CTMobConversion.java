package com.iamshift.mineaddons.integration.crafttweaker;

import com.iamshift.mineaddons.MineAddons;
import com.iamshift.mineaddons.integration.CraftTweaker;
import com.iamshift.mineaddons.utils.ConversionHelper;
import com.iamshift.mineaddons.utils.ConversionHelper.Conversion;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.mineaddons.MobConversion")
public class CTMobConversion
{
	private static class Add implements IAction
	{
		private final Conversion conversion;
		private final String water;
		
		public Add(Conversion conversion, String water)
		{
			this.conversion = conversion;
			this.water = water;
		}
		
		@Override
		public void apply()
		{
			switch(this.water)
			{
				case "sacred":
					if(!ConversionHelper.sacredList.contains(conversion))
						ConversionHelper.sacredList.add(conversion);
					break;
				case "cursed":
					if(!ConversionHelper.cursedList.contains(conversion))
						ConversionHelper.cursedList.add(conversion);
					break;
				case "forgotten":
					if(!ConversionHelper.forgottenList.contains(conversion))
						ConversionHelper.forgottenList.add(conversion);
					break;
			}
		}
		
		@Override
		public String describe()
		{
			return "Adding mob conversion for " + conversion.getOutput();
		}
	}
	
	private static class Remove implements IAction
	{
		private final Conversion conversion;
		private final String water;
		
		public Remove(Conversion conversion, String water)
		{
			this.conversion = conversion;
			this.water = water;
		}
		
		@Override
		public void apply()
		{
			switch(this.water)
			{
				case "sacred":
					ConversionHelper.sacredList.remove(conversion);
					break;
				case "cursed":
					ConversionHelper.cursedList.remove(conversion);
					break;
				case "forgotten":
					ConversionHelper.forgottenList.remove(conversion);
					break;
			}
		}
		
		@Override
		public String describe()
		{
			return "Removing mob conversion for " + conversion.getOutput();
		}
	}
	
	@ZenMethod
	public static void addMob(String input, String output, String water)
	{
		add(input, output, water);
	}
	
	private static void add(String input, String output, String water)
	{
		CraftTweaker.queue(() -> {
			Conversion c = null;
			
			try
			{
				c = new Conversion(input, output);
			}
			catch (IllegalArgumentException e) 
			{
				MineAddons.LOGGER.error("Invalid conversion " + e.getMessage());
			}

			CraftTweakerAPI.apply(new Add(c, water));
		});
	}
	
	@ZenMethod
	public static void removeMob(String input, String water)
	{
		remove(input, "", water);
	}
	
	private static void remove(String input, String output, String water)
	{
		CraftTweaker.queue(() -> {
			Conversion c = null;
			
			try
			{
				c = new Conversion(input, output);
			}
			catch (IllegalArgumentException e) 
			{
				MineAddons.LOGGER.error("Invalid conversion " + e.getMessage());
			}

			CraftTweakerAPI.apply(new Remove(c, water));
		});
	}
}
