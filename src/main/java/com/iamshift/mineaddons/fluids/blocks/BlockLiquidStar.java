package com.iamshift.mineaddons.fluids.blocks;

import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.init.ModBlocks;
import com.iamshift.mineaddons.init.ModFluids;
import com.iamshift.mineaddons.interfaces.IHasModel;
import com.iamshift.mineaddons.materials.Materials;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLiquidStar extends BlockFluidClassic implements IHasModel
{
	public BlockLiquidStar(String name)
	{
		super(ModFluids.LiquidStar, Materials.LIQUID_STAR);
		setUnlocalizedName(name);
		setRegistryName(new ResourceLocation(Refs.ID, name));

		setHardness(100.0F);
		setLightOpacity(3);
		disableStats();

		setDensity(1);

		ModBlocks.BLOCKS.add(this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels()
	{
		ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(LEVEL).build());
	}
	
	@Override
	public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, SpawnPlacementType type)
	{
		return false;
	}
	
	@Override
	public boolean displaceIfPossible(World world, BlockPos pos)
	{
		if (world.isAirBlock(pos)) return true;

		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		if (block == this) return false;

		if (displacements.containsKey(block))
		{
			if (displacements.get(block))
			{
				if (state.getBlock() != Blocks.SNOW_LAYER) //Forge: Vanilla has a 'bug' where snowballs don't drop like every other block. So special case because ewww...
					block.dropBlockAsItem(world, pos, state, 0);
				return true;
			}
			return false;
		}

		Material material = state.getMaterial();
		if (material.blocksMovement() || material == Material.PORTAL) return false;

		world.setBlockToAir(pos);
		return false;
	}
}
