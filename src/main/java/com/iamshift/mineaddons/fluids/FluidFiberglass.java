package com.iamshift.mineaddons.fluids;

import com.iamshift.mineaddons.core.Refs;

import net.minecraft.item.EnumRarity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;

public class FluidFiberglass extends Fluid
{
	public FluidFiberglass()
	{
		super("liquid_fiberglass", new ResourceLocation(Refs.ID, "fluids/liquid_fiberglass_still"), new ResourceLocation(Refs.ID, "fluids/liquid_fiberglass_flow"));
		FluidRegistry.registerFluid(this);
		FluidRegistry.addBucketForFluid(this);
		
		if(Loader.isModLoaded("thermalfoundation") && Loader.isModLoaded("plentifluids"))
			setTemperature(2500);
		else
			setTemperature(800);
		
		setViscosity(4000);
		setDensity(3000);
		setRarity(EnumRarity.EPIC);
	}
}
