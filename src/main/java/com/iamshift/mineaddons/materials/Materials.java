package com.iamshift.mineaddons.materials;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;

public class Materials extends Material
{
	public static final Material WITHERED = new MaterialLiquid(MapColor.WATER);
	public static final Material FIBERGLASS = new MaterialLiquid(MapColor.AIR);
	public static final Material LIQUID_STAR = new MaterialLiquid(MapColor.AIR);
	public static final Material LIQUID_DIAMOND = new MaterialLiquid(MapColor.AIR);
	public static final Material ALLOY_IDIAMOND = new MaterialLiquid(MapColor.AIR);
	public static final Material ALLOY_FIBERSTAR = new MaterialLiquid(MapColor.AIR);
	
	public Materials(MapColor color)
	{
		super(color);
	}
}
