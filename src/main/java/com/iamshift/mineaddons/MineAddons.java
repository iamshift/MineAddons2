package com.iamshift.mineaddons;

import com.iamshift.mineaddons.cmds.TimeSkipCommand;
import com.iamshift.mineaddons.core.Config;
import com.iamshift.mineaddons.core.MineTab;
import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.core.SmeltingRecipes;
import com.iamshift.mineaddons.init.ModBlocks;
import com.iamshift.mineaddons.init.ModEnchants;
import com.iamshift.mineaddons.init.ModEntities;
import com.iamshift.mineaddons.init.ModFluids;
import com.iamshift.mineaddons.init.ModIntegrations;
import com.iamshift.mineaddons.init.ModItems;
import com.iamshift.mineaddons.init.ModLoot;
import com.iamshift.mineaddons.init.ModNetwork;
import com.iamshift.mineaddons.init.ModPotions;
import com.iamshift.mineaddons.init.ModSounds;
import com.iamshift.mineaddons.proxy.CommonProxy;
import com.iamshift.mineaddons.utils.ForgottenAnvilHelper;
import com.iamshift.mineaddons.utils.OreDict;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = Refs.ID, name = Refs.NAME ,version = Refs.VERSION, acceptedMinecraftVersions = Refs.MCVERSIONS, dependencies = Refs.DEPENDENCIES, certificateFingerprint = Refs.FINGER)
public class MineAddons
{
	@Mod.Instance
	public static MineAddons instance;

	@SidedProxy(clientSide = Refs.CLIENT, serverSide = Refs.SERVER)
	public static CommonProxy proxy;

	public static CreativeTabs minetab;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Config.init(event.getModConfigurationDirectory());

		minetab = new MineTab();

		ModSounds.init();
		ModLoot.init();
		ModPotions.init();
		ModFluids.init();
		ModItems.init();
		ModBlocks.init();
		ModEntities.init();
		ModEnchants.init();

		proxy.registerEntityRender();
		OreDict.createDitc();

		SmeltingRecipes.init();
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		ModIntegrations.init();
		ModNetwork.init();

		ForgottenAnvilHelper.loadEnchantments();

		proxy.addKeybind();
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		proxy.addLayers();
	}

	@Mod.EventHandler
	public void serverStart(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new TimeSkipCommand());
	}

	static
	{
		FluidRegistry.enableUniversalBucket();
	}
}
