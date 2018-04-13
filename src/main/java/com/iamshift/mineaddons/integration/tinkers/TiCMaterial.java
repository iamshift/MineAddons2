package com.iamshift.mineaddons.integration.tinkers;

import static slimeknights.tconstruct.library.materials.MaterialTypes.HEAD;

import com.iamshift.mineaddons.init.ModFluids;
import com.iamshift.mineaddons.init.ModItems;
import com.iamshift.mineaddons.integration.Tinkers;

import net.minecraft.util.text.TextFormatting;
import slimeknights.tconstruct.library.MaterialIntegration;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.ArrowShaftMaterialStats;
import slimeknights.tconstruct.library.materials.BowMaterialStats;
import slimeknights.tconstruct.library.materials.ExtraMaterialStats;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.utils.HarvestLevels;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;

public class TiCMaterial
{
	private static int COLOR = 0xF5FFFA;
	
	public static void setup()
	{
		HarvestLevels.harvestLevelNames.put(6, TextFormatting.GRAY + "Fiberglass");
		
		Material fiberglass = new Material("fiberglass", COLOR);
		fiberglass.setFluid(ModFluids.Fiberglass);
		fiberglass.setCastable(true);
		fiberglass.setCraftable(false);
		
		fiberglass.addTrait(Tinkers.lightspeed, HEAD);
		
		TinkerRegistry.addMaterialStats(fiberglass, 
				new HeadMaterialStats(600, 1, 15, 6), 
				new HandleMaterialStats(1F, 175), 
				new ExtraMaterialStats(600), 
				new BowMaterialStats(40F, 7.5F, 200),
				new ArrowShaftMaterialStats(8F, 100));
		
		MaterialIntegration m = new MaterialIntegration(fiberglass, ModFluids.Fiberglass, "Fiberglass");
		TinkerRegistry.integrate(m).preInit();
		fiberglass.setRepresentativeItem(ModItems.Fiberglass);
		TinkerSmeltery.registerToolpartMeltingCasting(fiberglass);
	}
}
