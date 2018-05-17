package com.iamshift.mineaddons.init;

import java.util.ArrayList;
import java.util.List;

import com.iamshift.mineaddons.blocks.BlockColorableTorch;
import com.iamshift.mineaddons.blocks.BlockForgottenAnvil;
import com.iamshift.mineaddons.blocks.BlockFrozenforgottenWater;
import com.iamshift.mineaddons.blocks.BlockInvLight;
import com.iamshift.mineaddons.blocks.BlockLavaSponge;
import com.iamshift.mineaddons.blocks.BlockScaffolding;
import com.iamshift.mineaddons.blocks.BlockSoul;
import com.iamshift.mineaddons.core.Config;
import com.iamshift.mineaddons.fluids.blocks.BlockAlloyFiberStar;
import com.iamshift.mineaddons.fluids.blocks.BlockAlloyIDiamond;
import com.iamshift.mineaddons.fluids.blocks.BlockCursedWater;
import com.iamshift.mineaddons.fluids.blocks.BlockForgottenWater;
import com.iamshift.mineaddons.fluids.blocks.BlockLiquidFiberglass;
import com.iamshift.mineaddons.fluids.blocks.BlockLiquidStar;
import com.iamshift.mineaddons.fluids.blocks.BlockSacredWater;

import net.minecraft.block.Block;

public class ModBlocks 
{
	public static final List<Block> BLOCKS = new ArrayList<Block>();

	public static Block LavaSponge;

	public static Block ColorableTorch_white;
	public static Block ColorableTorch_orange;
	public static Block ColorableTorch_magenta;
	public static Block ColorableTorch_lightblue;
	public static Block ColorableTorch_yellow;
	public static Block ColorableTorch_lime;
	public static Block ColorableTorch_pink;
	public static Block ColorableTorch_gray;
	public static Block ColorableTorch_silver;
	public static Block ColorableTorch_cyan;
	public static Block ColorableTorch_purple;
	public static Block ColorableTorch_blue;
	public static Block ColorableTorch_brown;
	public static Block ColorableTorch_green;
	public static Block ColorableTorch_red;
	public static Block ColorableTorch_black;

	public static Block Scaffolding;
	public static Block SoulBlock;
	public static Block InfusedSoulBlock;
	public static Block FrozenForgottenWater;

	public static Block SacredWater;
	public static Block CursedWater;
	public static Block ForgottenWater;
	public static Block Fiberglass;
	public static Block LiquidStar;
	public static Block AlloyIDiamond;
	public static Block AlloyFiberstar;

	public static Block InvLight;

	public static Block ForgottenAnvil;

	public static void init()
	{
		ColorableTorch_white = new BlockColorableTorch("colorable_torch_white", 0);
		ColorableTorch_orange = new BlockColorableTorch("colorable_torch_orange", 1);
		ColorableTorch_magenta = new BlockColorableTorch("colorable_torch_magenta", 2);
		ColorableTorch_lightblue = new BlockColorableTorch("colorable_torch_lightblue", 3);
		ColorableTorch_yellow = new BlockColorableTorch("colorable_torch_yellow", 4);
		ColorableTorch_lime = new BlockColorableTorch("colorable_torch_lime", 5);
		ColorableTorch_pink = new BlockColorableTorch("colorable_torch_pink", 6);
		ColorableTorch_gray = new BlockColorableTorch("colorable_torch_gray", 7);
		ColorableTorch_silver = new BlockColorableTorch("colorable_torch_silver", 8);
		ColorableTorch_cyan = new BlockColorableTorch("colorable_torch_cyan", 9);
		ColorableTorch_purple = new BlockColorableTorch("colorable_torch_purple", 10);
		ColorableTorch_blue = new BlockColorableTorch("colorable_torch_blue", 11);
		ColorableTorch_brown = new BlockColorableTorch("colorable_torch_brown", 12);
		ColorableTorch_green = new BlockColorableTorch("colorable_torch_green", 13);
		ColorableTorch_red = new BlockColorableTorch("colorable_torch_red", 14);
		ColorableTorch_black = new BlockColorableTorch("colorable_torch_black", 15);

		Scaffolding = new BlockScaffolding("scaffolding");

		LavaSponge = new BlockLavaSponge("lava_sponge");

		if(Config.Tinker || Config.Foundry)
		{
			Fiberglass = new BlockLiquidFiberglass("liquid_fiberglass");
			LiquidStar = new BlockLiquidStar("liquid_star");
			InfusedSoulBlock = new BlockSoul("infused_soul_block");
		}
		
		if(Config.Foundry)
		{
			AlloyIDiamond = new BlockAlloyIDiamond("alloy_idiamond");
			AlloyFiberstar = new BlockAlloyFiberStar("alloy_fiberstar");
		}

		SoulBlock = new BlockSoul("soul_block");
		InvLight = new BlockInvLight("invlight");

		SacredWater = new BlockSacredWater("sacred_water");
		CursedWater = new BlockCursedWater("cursed_water");
		FrozenForgottenWater = new BlockFrozenforgottenWater("frozen_forgotten_water");
		ForgottenWater = new BlockForgottenWater("forgotten_water");

		ForgottenAnvil = new BlockForgottenAnvil("forgotten_anvil");
	}
}
