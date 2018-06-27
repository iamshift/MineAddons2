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
	private static final String TINKERS = "Tinkers";
	private static final String BOSSES = "Bosses";
	private static final String BREAKER = "Breaker";
	private static final String ENCHANTS = "Enchants";
	private static final String TIMESKIP = "TimeSkip";
	private static final String TOOLS = "Tools & Armors";
	private static final String COLOR = "Colorable Items & Blocks";
	private static final String WMOVER = "Wireless Mover";
	
	// VCHECK
	private static final int VERSION = 6;
	private static int ver;
	
	// ADDONS
	public static boolean Tinker = true;
	public static boolean Foundry = true;
	public static boolean CraftTweaker = true;
	public static boolean Waila = true;

	// MISC
	public static boolean	HardLiquidStar = true;
	public static boolean	Scafolding = true;
	public static boolean	LavaSponge = true;
	public static boolean	SpongeRecipe = true;
	public static boolean	Respirator = true;
	public static boolean	Sandstones = true;
	
	// COLOR
	public static boolean	ColorableTorch = true;
	public static boolean	ColorablePlanks = true;
	public static boolean	CarpetColorChange = true;
	
	// ENCHANTS
	public static boolean	ForgottenEnchantments = true;
	public static float		RocketBoost = 1.0F; 
	public static int		RocketTrigger = 5;
	public static int		RocketEnchantDropRate = 15;
	public static int		ElytraEnchantDropRate = 5;
	
	// TIMESKIP
	public static boolean	TimeSkipClock = true;
	public static int 		TimeSkipDelay = 30;
	public static int 		TimeSkipCooldown = 60;
	
	// TOOLS
	public static boolean	FiberTools = true;
	public static boolean	FiberArmor = true;
	public static boolean	UltiArmor = true;
	public static boolean	UpgradeArmor = true;
	
	// BREAKER
	public static boolean	Breaker = true;
	private static List<String> unbreak = new ArrayList<String>();
	
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
	
	// BOSSES
	public static boolean FakePlayer = false;
	
	// WIRELESS MOVER
	public static boolean	WirelessMover = true;
	public static int		DistanceLimit = 16;
	
	// TINKERS
	public static boolean Harmonious = true;
	public static boolean InfusedSoulBlock = true;
	
	
	public static Configuration conf;

	public static void init(File configFile) 
	{
		File file = new File(configFile.getAbsolutePath() + File.separator + "MineAddons", "config.cfg");
		
		if(conf == null)
			conf = new Configuration(file);

		initCaptures();
		initUnbreakables();
		
		conf.setCategoryComment(ADDONS, "Enable / Disable mod addons");
		Tinker =				conf.getBoolean("Tinkers", ADDONS, true, "Enable / Disable Tinkers integration.") && Loader.isModLoaded("tconstruct");
		Foundry =				conf.getBoolean("Foundry", ADDONS, true, "Enable / Disable Foundry integration.") && Loader.isModLoaded("foundry");
		CraftTweaker =			Loader.isModLoaded("crafttweaker");
		Waila =					Loader.isModLoaded("waila");
		
		
		conf.setCategoryComment(MISC, "Some random stuff that don't need a category for itself.");
		HardLiquidStar =		conf.getBoolean("HardLiquidStar", MISC, HardLiquidStar, "Easy[false] = 2 ingot per star | Hard[true] = 1 ingot per star");
		Scafolding =			conf.getBoolean("Scafolding", MISC, Scafolding, "Enable / Disable scafolding block.");
		LavaSponge = 			conf.getBoolean("LavaSpong", MISC, LavaSponge, "Enable / Disable lava sponge block.");
		SpongeRecipe = 			conf.getBoolean("SpongeRecipe", MISC, SpongeRecipe, "Enable / Disable sponge recipe.");
		Respirator = 			conf.getBoolean("Respirator", MISC, Respirator, "Enable / Disable respirator.");
		Sandstones =			conf.getBoolean("Sandstones", MISC, Sandstones, "Enable / Disable Sandstones.");
		
		
		conf.setCategoryComment(COLOR, "All the configs related to color blocks");
		ColorableTorch = 		conf.getBoolean("ColorableTorch", COLOR, ColorableTorch, "Enable / Disable colorable torchs.");
		ColorablePlanks = 		conf.getBoolean("ColorablePlanks", COLOR, ColorablePlanks, "Enable / Disable colorable planks.");
		CarpetColorChange = 	conf.getBoolean("CarpetColorChange", COLOR, CarpetColorChange, "Enable / Disable carpet color change.");
		
		
		conf.setCategoryComment(ENCHANTS, "All the configs related to enchantmenst");
		ForgottenEnchantments =	conf.getBoolean("ForgottenEnchantments", ENCHANTS, ForgottenEnchantments, "Enable / Disable Forgotten Enchantments.");
		RocketBoost = 			conf.getFloat("RocketBoost", ENCHANTS, RocketBoost, 0.1F, 1.0F, "Rocket Enchantment speed boost.");
		RocketEnchantDropRate = conf.getInt("RocketEnchantment", ENCHANTS, RocketEnchantDropRate, 0, Integer.MAX_VALUE, "The higher the value the lower the rate. 0 - Disable | 1 - Always");
		ElytraEnchantDropRate = conf.getInt("ElytraEnchantment", ENCHANTS, ElytraEnchantDropRate, 0, Integer.MAX_VALUE, "The higher the value the lower the rate. 0 - Disable | 1 - Always");
		
		
		conf.setCategoryComment(TIMESKIP, "All the configs related to timeskip");
		TimeSkipClock =			conf.getBoolean("TimeSkipClock", TIMESKIP, TimeSkipClock, "Enable / Disable timeskip clock.");
		TimeSkipDelay = 		conf.getInt("TimeSkipDelay", TIMESKIP, TimeSkipDelay, 0, Integer.MAX_VALUE, "Delay before skip.");
		TimeSkipCooldown = 		conf.getInt("TimeSkipCooldown", TIMESKIP, TimeSkipCooldown, 0, Integer.MAX_VALUE, "Cooldown between uses.");
		
		
		conf.setCategoryComment(TOOLS, "All the configs related to tools and armors");
		FiberTools =			conf.getBoolean("FiberTools", TOOLS, FiberTools, "Enable / Disable Fiberglass Tools. Only works with Foundry or Vanilla versions.");
		FiberArmor =			conf.getBoolean("FiberArmor", TOOLS, FiberArmor, "Enable / Disable Infused Diamond Armor.");
		UltiArmor =				conf.getBoolean("UltiArmor", TOOLS, UltiArmor, "Enable / Disable Ultimate Armor.");
		UpgradeArmor =			conf.getBoolean("UpgradeArmor", TOOLS, UpgradeArmor, "Enable / Disable Ultimate Upgrade Armor.");
		
		
		conf.setCategoryComment(BREAKER, "Breaker config");
		Breaker = 				conf.getBoolean("Breaker", BREAKER, Breaker, "Enable / Disable Breaker Tool.");
		unbreak =				Arrays.asList(conf.getStringList("UnbreakableBlocks", BREAKER, unbreak.toArray(new String[0]), "List of blocks ignored by Breaker."));
		
		
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
		WaterSpread =			conf.getBoolean("WaterSpread", WATERS, WaterSpread, "Enable / Disable the sacred and cursed water spreading.");
		WaterSource =			conf.getBoolean("WaterSource", WATERS, WaterSource, "Enable / Disable the sacred and cursed water infinite source creation.");
		MobConvertion =			conf.getBoolean("MobConvertion", WATERS, MobConvertion, "Enable / Disable the sacred and cursed water ability to convert mobs.");
		
		
		conf.setCategoryComment(BOSSES, "All the configs related to Bosses");
		FakePlayer =			conf.getBoolean("FakePlayer", BOSSES, FakePlayer, "Allow Bosses to be killed with machines.");
		
		
		conf.setCategoryComment(WMOVER, "All the configs related to Wireless Mover.");
		WirelessMover =			conf.getBoolean("WirelessMover", WMOVER, WirelessMover, "Enable / Disable wireless mover.");
		DistanceLimit =			conf.getInt("DistanceLimit", WMOVER, DistanceLimit, 1, 16, "Maximum distance the Mover can push a block.");
		
		
		conf.setCategoryComment(TINKERS, "Tinkers Addon config");
		Harmonious =			conf.getBoolean("HarmoniousLiquid", TINKERS, Harmonious, "Enable / disable this alloy.");
		InfusedSoulBlock = 		conf.getBoolean("InfusedSoulBlock", TINKERS, InfusedSoulBlock, "Enable / Disable infused soul block.");
		
		
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
			unbreak = new ArrayList<String>();
			
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
	
	private static void initUnbreakables()
	{
		unbreak.add("minecraft:barrier");
		unbreak.add("mineaddons:invlight");
		unbreak.add("bigmarket:market_portal");
		unbreak.add("bigmarket:market_portal2");
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
	
	public static boolean isUnbreakble(String string)
	{
		return unbreak.contains(string);
	}
}
