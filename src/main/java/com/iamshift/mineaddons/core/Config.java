package com.iamshift.mineaddons.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;

public class Config 
{
	// CATEGORIES
	private static final String MISC = "Miscellaneous";
	private static final String MOBS = "Mobs";
	private static final String WATERS = "Waters";
	private static final String VCHECK = "Version";
	private static final String ADDONS = "Addons";
	
	// VCHECK
	private static final int VERSION = 4;
	private static int ver;
	
	// ADDONS
	public static boolean Tinker = true;
	public static boolean Foundry = true;
	public static boolean CraftTweaker = true;
	public static boolean Waila = true;

	// MISC
	public static int 		TimeSkipDelay = 30;
	public static int 		TimeSkipCooldown = 60;
	public static boolean	ForgottenEnchantments = true;
	public static boolean	FiberTools = true;
	public static boolean	FiberArmor = true;
	public static boolean	UltiArmor = true;
	public static boolean	HardLiquidStar = true;
	public static int		RocketEnchantDropRate = 15;
	public static int		ElytraEnchantDropRate = 15;
	
	// MOBS
	public static int 		MaxAncientCarps = 20;
	public static boolean 	CaptureAncientCarps = false;
	public static int		AncientCarpSpawnRate = 25;
	public static int		EnderCarpSpawnRate = 5;
	public static int		VoidCreeperSpawnRate = 8;
	public static int		HellhoundSpawnRate = 7;
	public static int		iSheepSpawnRate = 6;
	public static int		ZlamaSpawnRate = 6;
	public static int		CreepierSpawnRate = 10;
	private static List<String> cItems = new ArrayList<String>(); 
	private static List<String> cEntities = new ArrayList<String>();
	private static List<String> nBoss = new ArrayList<String>();
	
	// WATERS
	public static boolean	MobConvertion = true;
	public static boolean	WaterSpread = true;
	public static boolean	WaterSource = false;

	
	public static Configuration conf;

	public static void init(File configFile) 
	{
		File file = new File(configFile.getAbsolutePath() + File.separator + "MineAddons", "config.cfg");
		
		if(conf == null)
			conf = new Configuration(file);

		initCaptures();
		
		conf.setCategoryComment(ADDONS, "Enable / Disable mod addons");
		
		Tinker =				conf.getBoolean("Tinkers", ADDONS, Tinker, "Enable / Disable Tinkers integration.") && Loader.isModLoaded("tconstruct");
		Foundry =				conf.getBoolean("Foundry", ADDONS, Foundry, "Enable / Disable Foundry integration.") && Loader.isModLoaded("foundry");
		CraftTweaker =			Loader.isModLoaded("crafttweaker");
		Waila =					Loader.isModLoaded("waila");
		
		
		conf.setCategoryComment(MISC, "Some random stuff that don't need a category for itself.");
		
		TimeSkipDelay = 		conf.getInt("TimeSkipDelay", MISC, TimeSkipDelay, 0, Integer.MAX_VALUE, "Delay before skip.");
		TimeSkipCooldown = 		conf.getInt("TimeSkipCooldown", MISC, TimeSkipCooldown, 0, Integer.MAX_VALUE, "Cooldown between uses.");
		ForgottenEnchantments =	conf.getBoolean("ForgottenEnchantments", MISC, ForgottenEnchantments, "Enable / disable Forgotten Enchantments.");
		HardLiquidStar =		conf.getBoolean("HardLiquidStar", MISC, HardLiquidStar, "Easy[false] = 2 ingot per star | Hard[true] = 1 ingot per star");
		FiberTools =			conf.getBoolean("FiberTools", MISC, FiberTools, "Enable / disable Fiberglass Tools. Only works if Tinkers and Foundry not present.");
		FiberArmor =			conf.getBoolean("FiberArmor", MISC, FiberArmor, "Enable / disable Infused Diamond Armor. Only works if Tinkers and Foundry not present.");
		UltiArmor =				conf.getBoolean("UltiArmor", MISC, UltiArmor, "Enable / disable Ultimate Armor. Only works if Tinkers and Foundry not present.");
		RocketEnchantDropRate = conf.getInt("RocketEnchantment", MISC, RocketEnchantDropRate, 0, Integer.MAX_VALUE, "The higher the value the lower the rate. 0 - Disable | 1 - Always");
		ElytraEnchantDropRate = conf.getInt("ElytraEnchantment", MISC, ElytraEnchantDropRate, 0, Integer.MAX_VALUE, "The higher the value the lower the rate. 0 - Disable | 1 - Always");
		
		
		conf.setCategoryComment(MOBS, "All the configs related to mobs.");
		
		MaxAncientCarps = 		conf.getInt("AncientCarpsMaxAlive", MOBS, MaxAncientCarps, 0, Integer.MAX_VALUE, "The max amount of Ancient Carps alive at the same time.");
		CaptureAncientCarps =	conf.getBoolean("AncientCarpsCapturable", MOBS, false, "Allow / Prevent Ancient Carps to be capture.");
		AncientCarpSpawnRate =	conf.getInt("SpawnRateAncientCarp", MOBS, AncientCarpSpawnRate, 0, 99, "The higher the value the lower the rate. 0 - Disable | 1 - Always");
		EnderCarpSpawnRate =	conf.getInt("SpawnRateEnderCarp", MOBS, EnderCarpSpawnRate, 0, 99, "The higher the value the lower the rate. 0 - Disable | 1 - Always");
		VoidCreeperSpawnRate =	conf.getInt("SpawnRateVoidCreeper", MOBS, VoidCreeperSpawnRate, 0, 99, "The higher the value the lower the rate. 0 - Disable | 1 - Always");
		HellhoundSpawnRate =	conf.getInt("SpawnRateHellhound", MOBS, HellhoundSpawnRate, 0, 99, "The higher the value the lower the rate. 0 - Disable | 1 - Always");
		iSheepSpawnRate =		conf.getInt("SpawnRateiSheep", MOBS, iSheepSpawnRate, 0, 99, "The higher the value the lower the rate. 0 - Disable | 1 - Always");
		ZlamaSpawnRate =		conf.getInt("SpawnRateZlama", MOBS, ZlamaSpawnRate, 0, 99, "The higher the value the lower the rate. 0 - Disable | 1 - Always");
		CreepierSpawnRate =		conf.getInt("SpawnRateCreepier", MOBS, CreepierSpawnRate, 0, 99, "The higher the value the lower the rate. 0 - Disable | 1 - Always");
		cItems = 				Arrays.asList(conf.getStringList("ListCaptureItems", MOBS, cItems.toArray(new String[0]), "List of items used to capture entities."));
		cEntities = 			Arrays.asList(conf.getStringList("ListCaptureEntities", MOBS, cEntities.toArray(new String[0]), "List of entities used to capture entities."));
		nBoss = 				Arrays.asList(conf.getStringList("ListAntiBossItems", MOBS, nBoss.toArray(new String[0]), "List of items not usable on Bosses."));
		
		
		conf.setCategoryComment(WATERS, "All the configs related to water");
		
		WaterSpread =			conf.getBoolean("WaterSpread", WATERS, WaterSpread, "Enable / disable the sacred and cursed water spreading.");
		WaterSource =			conf.getBoolean("WaterSource", WATERS, WaterSource, "Enable / disable the sacred and cursed water infinite source creation.");
		MobConvertion =			conf.getBoolean("MobConvertion", WATERS, MobConvertion, "Enable / disable the sacred and cursed water ability to convert mobs.");
		
		
		conf.setCategoryComment(VCHECK, "Version Checker");
		
		ver = 					conf.getInt("version", VCHECK, VERSION, 1, Integer.MAX_VALUE, "");
		
		
		if(conf.hasChanged())
			conf.save();

		if(ver != VERSION)
		{
			conf = null;
			file.delete();
			
			cItems = new ArrayList<String>();
			cEntities = new ArrayList<String>();
			nBoss = new ArrayList<String>();
			
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
		
		nBoss.add("avaritia:infinity_sword");
	}

	public static boolean isCaptureItem(String string)
	{
		return cItems.contains(string);
	}
	
	public static boolean isCaptureEntity(String string)
	{
		return cEntities.contains(string);
	}
	
	public static boolean isAntiBoss(String string)
	{
		return nBoss.contains(string);
	}
}
