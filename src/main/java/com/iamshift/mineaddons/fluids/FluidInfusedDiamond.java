package com.iamshift.mineaddons.fluids;

import com.iamshift.mineaddons.core.Refs;

import net.minecraft.item.EnumRarity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;

public class FluidInfusedDiamond extends Fluid
{
	public FluidInfusedDiamond()
	{
		super("alloy_idiamond", new ResourceLocation(Refs.ID, "fluids/alloy_idiamond_still"), new ResourceLocation(Refs.ID, "fluids/alloy_idiamond_flow"));
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
