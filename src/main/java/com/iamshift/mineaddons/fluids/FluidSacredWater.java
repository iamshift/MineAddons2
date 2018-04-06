package com.iamshift.mineaddons.fluids;

import com.iamshift.mineaddons.core.Refs;

import net.minecraft.item.EnumRarity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidSacredWater extends Fluid
{
	public FluidSacredWater()
	{
		super("sacred_water", new ResourceLocation(Refs.ID, "fluids/sacred_water_still"), new ResourceLocation(Refs.ID, "fluids/sacred_water_flow"));
		FluidRegistry.registerFluid(this);
		FluidRegistry.addBucketForFluid(this);
		setTemperature(150);
		setRarity(EnumRarity.RARE);
	}
}
