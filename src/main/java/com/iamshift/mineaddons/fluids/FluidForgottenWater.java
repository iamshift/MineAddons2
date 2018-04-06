package com.iamshift.mineaddons.fluids;

import com.iamshift.mineaddons.core.Refs;

import net.minecraft.item.EnumRarity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class FluidForgottenWater extends Fluid
{
	public FluidForgottenWater()
	{
		super("forgotten_water", new ResourceLocation(Refs.ID, "fluids/forgotten_water_still"), new ResourceLocation(Refs.ID, "fluids/forgotten_water_flow"));
		FluidRegistry.registerFluid(this);
		FluidRegistry.addBucketForFluid(this);
		setTemperature(0);
		setViscosity(3000);
		setDensity(2000);
		setRarity(EnumRarity.EPIC);
	}
	
	@Override
	public boolean doesVaporize(FluidStack fluidStack)
	{
		return false;
	}
	
}
