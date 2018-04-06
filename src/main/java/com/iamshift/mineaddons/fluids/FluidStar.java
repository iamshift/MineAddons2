package com.iamshift.mineaddons.fluids;

import com.iamshift.mineaddons.core.Refs;

import net.minecraft.item.EnumRarity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;

public class FluidStar extends Fluid
{

	public FluidStar()
	{
		super("liquid_star", new ResourceLocation(Refs.ID, "fluids/liquid_star_still"), new ResourceLocation(Refs.ID, "fluids/liquid_star_flow"));
		FluidRegistry.registerFluid(this);
		FluidRegistry.addBucketForFluid(this);
		
		if(Loader.isModLoaded("thermalfoundation") && Loader.isModLoaded("plentifluids"))
			setTemperature(3000);
		else
			setTemperature(1000);
		
		setViscosity(1000);
		setDensity(100);
		setRarity(EnumRarity.EPIC);
	} 

}
