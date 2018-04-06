package com.iamshift.mineaddons.fluids;

import com.iamshift.mineaddons.core.Refs;

import net.minecraft.item.EnumRarity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidCursedWater extends Fluid
{
	public FluidCursedWater()
	{
		super("cursed_water", new ResourceLocation(Refs.ID, "fluids/cursed_water_still"), new ResourceLocation(Refs.ID, "fluids/cursed_water_flow"));
		FluidRegistry.registerFluid(this);
		FluidRegistry.addBucketForFluid(this);
		setTemperature(1450);
		setRarity(EnumRarity.RARE);
	}
}
