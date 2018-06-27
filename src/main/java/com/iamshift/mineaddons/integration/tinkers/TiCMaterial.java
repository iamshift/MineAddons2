package com.iamshift.mineaddons.integration.tinkers;

import static slimeknights.tconstruct.library.materials.MaterialTypes.HEAD;

import com.iamshift.mineaddons.core.Config;
import com.iamshift.mineaddons.init.ModFluids;
import com.iamshift.mineaddons.init.ModItems;
import com.iamshift.mineaddons.integration.Tinkers;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.oredict.OreDictionary;
import slimeknights.tconstruct.library.MaterialIntegration;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.client.MaterialRenderInfo;
import slimeknights.tconstruct.library.materials.BowMaterialStats;
import slimeknights.tconstruct.library.materials.ExtraMaterialStats;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.smeltery.Cast;
import slimeknights.tconstruct.library.tinkering.MaterialItem;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.library.utils.HarvestLevels;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.tools.TinkerTraits;

public class TiCMaterial
{
	public static Material fiberglass;
	public static Material harmonious;

	public static void setup()
	{
		HarvestLevels.harvestLevelNames.put(6, TextFormatting.GRAY + "Fiberglass");
		
		fiberglass = new Material("fiberglass", 0xf5fffa); // 0xF5FFFA
		
		fiberglass.addItem(ModItems.FiberglassIngot);
		fiberglass.setRepresentativeItem(ModItems.FiberglassIngot);
		fiberglass.setFluid(ModFluids.Fiberglass);
		fiberglass.setCastable(true);
		fiberglass.setCraftable(false);

		fiberglass.addTrait(Tinkers.lightspeed, HEAD);

		TinkerRegistry.addMaterialStats(fiberglass, 
				new HeadMaterialStats(600, 1, 15, 6), 
				new HandleMaterialStats(1F, 175), 
				new ExtraMaterialStats(600));
		
		TinkerRegistry.integrate(fiberglass).preInit();

		registerToolParts(fiberglass);

		
		if(Config.Harmonious)
		{
			harmonious = new Material("harmonious", 0x117a7a); // 0x117A7A
			
			harmonious.addItem(ModItems.HarmoniousIngot);
			harmonious.setRepresentativeItem(ModItems.HarmoniousIngot);
			harmonious.setFluid(ModFluids.Harmonious);
			harmonious.setCastable(true);
			harmonious.setCraftable(false);

			harmonious.addTrait(Tinkers.lightspeed, HEAD);
			harmonious.addTrait(TinkerTraits.coldblooded);
			harmonious.addTrait(TinkerTraits.enderference);
			
			TinkerRegistry.addMaterialStats(harmonious, 
					new HeadMaterialStats(2000, 1, 30, 6), 
					new HandleMaterialStats(1.5F, 500), 
					new ExtraMaterialStats(750),
					new BowMaterialStats(8F, 5F, 100F));
			
			TinkerRegistry.integrate(harmonious).preInit();

			registerToolParts(harmonious);
		}
	}

	private static void registerToolParts(Material material)
	{
		Fluid fluid = material.getFluid();

		for(IToolPart toolPart : TinkerRegistry.getToolParts()) 
		{
			if(!toolPart.canBeCasted()) 
				continue;

			if(!toolPart.canUseMaterial(material)) 
				continue;

			if(toolPart instanceof MaterialItem) 
			{
				ItemStack stack = toolPart.getItemstackWithMaterial(material);

				ItemStack originCast = Cast.setTagForPart(new ItemStack(TinkerSmeltery.cast), stack.getItem());
				String part = originCast.getDisplayName().replaceAll(" Cast", "");
				ItemStack cast = ModItems.castliquidstar.addPart(part, stack.getItem());

				if(fluid != null) 
				{
					TinkerRegistry.registerMelting(stack, fluid, toolPart.getCost());
					TinkerRegistry.registerTableCasting(stack, cast, fluid, toolPart.getCost());
				}
			}
		}
	}

	public static void setRenderInfo()
	{
		fiberglass.setRenderInfo(0xf5fffa);
		harmonious.setRenderInfo(0x117a7a);
	}
}
