package com.iamshift.mineaddons.init;

import com.iamshift.mineaddons.core.Config;
import com.iamshift.mineaddons.fluids.FluidCursedWater;
import com.iamshift.mineaddons.fluids.FluidFiberStar;
import com.iamshift.mineaddons.fluids.FluidFiberglass;
import com.iamshift.mineaddons.fluids.FluidForgottenWater;
import com.iamshift.mineaddons.fluids.FluidHarmonious;
import com.iamshift.mineaddons.fluids.FluidInfusedDiamond;
import com.iamshift.mineaddons.fluids.FluidSacredWater;
import com.iamshift.mineaddons.fluids.FluidStar;

import net.minecraftforge.fluids.Fluid;

public class ModFluids
{
	public static Fluid SacredWater;
	public static Fluid CursedWater;
	public static Fluid ForgottenWater;

	public static Fluid Fiberglass;
	public static Fluid LiquidStar;
	public static Fluid	Harmonious;
	
	public static Fluid InfusedDiamond;
	public static Fluid FiberStar;

	public static void init()
	{
		if(Config.Tinker || Config.Foundry)
		{
			Fiberglass = new FluidFiberglass();
			LiquidStar = new FluidStar();
			
			if(Config.Harmonious)
				Harmonious = new FluidHarmonious();
		}

		if(Config.Foundry)
		{
			InfusedDiamond = new FluidInfusedDiamond();
			FiberStar = new FluidFiberStar();
		}

		SacredWater = new FluidSacredWater();
		CursedWater = new FluidCursedWater();
		ForgottenWater = new FluidForgottenWater();
	}
}
