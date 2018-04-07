package com.iamshift.mineaddons.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.EntityEntry;

public class Config 
{
	// CONTROL
	private static final int VERSION = 1;
	private static int ver;

	public static int 		TimeSkipDelay = 30;
	public static int 		TimeSkipCooldown = 60;
	public static int 		MaxAncientCarps = 20;
	public static boolean 	CaptureAncientCarps = false;
	
	private static List<String> cItems = new ArrayList<String>(); 
	private static List<String> cEntities = new ArrayList<String>();
	
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

		initCaptures();
		
		cItems = Arrays.asList(conf.getStringList("CaptureItems", "Capture", cItems.toArray(new String[0]), ""));
		cEntities = Arrays.asList(conf.getStringList("CaptureEntities", "Capture", cEntities.toArray(new String[0]), ""));
		
		if(conf.hasChanged())
			conf.save();

		if(ver != VERSION)
		{
			conf = null;
			configFile.delete();
			init(configFile);
		}
	}
	
	private static void initCaptures()
	{
		cItems.add("industrialforegoing:mob_imprisonment_tool");
		cItems.add("notenoughwands:capturing_wand");
		cItems.add("rftools:syringe");
		cItems.add("mob_grinding_utils:mob_swab");
		cItems.add("actuallyadditions:item_spawner_changer");
		cItems.add("extrautils2:goldenlasso");
		cItems.add("woot:endershard");
		cItems.add("enderio:item_soul_vial");
		
		cEntities.add("thermalexpansion:morb");
	}

	public static boolean isCaptureItem(String string)
	{
		return cItems.contains(string);
	}
	
	public static boolean isCaptureEntity(String string)
	{
		return cEntities.contains(string);
	}
}
