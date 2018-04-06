package com.iamshift.mineaddons.core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;

public class Config 
{
	// CONTROL
	private static final int VERSION = 1;
	private static int ver;

	public static int 		TimeSkipDelay = 30;
	public static int 		TimeSkipCooldown = 60;
	public static int 		MaxAncientCarps = 20;
	public static boolean 	CaptureAncientCarps = false;
	
	public static Map<ResourceLocation, Integer> captureItems = new HashMap<ResourceLocation, Integer>();
	public static List<ResourceLocation> captureEntities = new ArrayList<ResourceLocation>();
	
	public static Configuration conf;

	public static void init(File configFile) 
	{
		File file = new File(configFile.getAbsolutePath() + File.separator + "MineAddons", "config.cfg");
		
		if(conf == null)
			conf = new Configuration(file);
		
		ver = conf.get("version", "version", VERSION).getInt();
		
		TimeSkipDelay = 		conf.getInt("TimeSkipDelay", 	"TimeSkipClock",  TimeSkipDelay,		0,	Integer.MAX_VALUE,	"");
		TimeSkipCooldown = 		conf.getInt("TimeSkipCooldown", "TimeSkipClock",  TimeSkipCooldown,	0,	Integer.MAX_VALUE,	"");
		
		MaxAncientCarps = 		conf.getInt("MaxAncientCarps",  "AncientCarps",  MaxAncientCarps,	0,	Integer.MAX_VALUE,	"");
		CaptureAncientCarps =	conf.getBoolean("CaptureAncientCarps", "AncientCarps", false, "Allow Ancient Carps to be capture.");
		
		captureItems.put(new ResourceLocation("industrialforegoing", "mob_imprisonment_tool"), -1);
		captureItems.put(new ResourceLocation("notenoughwands", "capturing_wand"), -1);
		captureItems.put(new ResourceLocation("rftools", "syringe"), -1);
//		captureItems.put(new ResourceLocation("mob_grinding_utils", "mob_swab"), -1);
		captureItems.put(new ResourceLocation("actuallyadditions", "item_spawner_changer"), -1);
		captureItems.put(new ResourceLocation("extrautils2", "goldenlasso"), -1);
		captureItems.put(new ResourceLocation("woot", "endershard"), -1);
		
		captureEntities.add(new ResourceLocation("thermalexpansion", "morb"));
		
		if(conf.hasChanged())
			conf.save();

		if(ver != VERSION)
		{
			conf = null;
			configFile.delete();
			init(configFile);
		}
	}
}
