package com.iamshift.mineaddons.init;

import com.iamshift.mineaddons.core.Config;
import com.iamshift.mineaddons.fluids.FluidCursedWater;
import com.iamshift.mineaddons.fluids.FluidFiberglass;
import com.iamshift.mineaddons.fluids.FluidForgottenWater;
import com.iamshift.mineaddons.fluids.FluidSacredWater;
import com.iamshift.mineaddons.fluids.FluidStar;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.Loader;

public class ModFluids
{
	public static Fluid SacredWater;
	public static Fluid CursedWater;
	public static Fluid ForgottenWater;

	public static Fluid Fiberglass;
	public static Fluid LiquidStar;

	public static void init()
	{
		if(Loader.isModLoaded("tconstruct") && Config.Tinker)
		{
			Fiberglass = new FluidFiberglass();
			LiquidStar = new FluidStar();
		}


		SacredWater = new FluidSacredWater();
		CursedWater = new FluidCursedWater();
		ForgottenWater = new FluidForgottenWater();
	}
}
