package com.iamshift.mineaddons.fluids.blocks;

import java.util.Random;

import com.iamshift.mineaddons.api.IMobChanger;
import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.entities.EntityTrueCreeper;
import com.iamshift.mineaddons.entities.EntityVoidCreeper;
import com.iamshift.mineaddons.init.ModBlocks;
import com.iamshift.mineaddons.init.ModFluids;
import com.iamshift.mineaddons.init.ModPotions;
import com.iamshift.mineaddons.interfaces.IHasModel;
import com.iamshift.mineaddons.materials.Materials;
import com.iamshift.mineaddons.particles.ParticleUtils;
import com.iamshift.mineaddons.particles.ParticleUtils.EnumParticles;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.init.Blocks;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockForgottenWater extends BlockFluidClassic implements IHasModel
{
	public BlockForgottenWater(String name)
	{
		super(ModFluids.ForgottenWater, Materials.WITHERED);
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
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		super.randomDisplayTick(stateIn, worldIn, pos, rand);
		if (rand.nextInt(50)==0)
			ParticleUtils.spawn(EnumParticles.FORGOTTEN_CLOUD, worldIn, pos.getX() + rand.nextFloat(), pos.getY() + 1.0F, pos.getZ() + rand.nextFloat(), 0.0D, 0.0D, 0.0D);
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

		if(material == Material.WATER)
		{
			if(block instanceof BlockSacredWater || block instanceof BlockCursedWater)
			{
				world.setBlockState(pos, Blocks.MYCELIUM.getDefaultState());
				return false;
			}
		}

		return false;
	}
	
	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity)
	{
		if(world.isRemote)
			return;
		
		if(!(entity instanceof EntityLivingBase))
			return;

		if(entity.isDead)
			return;

		if(((EntityLivingBase)entity).isPotionActive(ModPotions.PotionMobChanger) && ((EntityLivingBase)entity).getActivePotionEffect(ModPotions.PotionMobChanger).getDuration() <= 0)
			((EntityLivingBase)entity).removeActivePotionEffect(ModPotions.PotionMobChanger);

		if(!(((EntityLivingBase)entity).isPotionActive(ModPotions.PotionMobChanger)) && tryConvertMob(world, pos, state, entity))
			((EntityLivingBase)entity).addPotionEffect(new PotionEffect(ModPotions.PotionMobChanger, 6000));
	}
	
	private boolean tryConvertMob(World world, BlockPos pos, IBlockState state, Entity entity) 
	{
		if (entity instanceof EntityCreeper && !(entity instanceof EntityVoidCreeper)) 
		{
			EntityCreeper creeper = (EntityCreeper) entity;
			creeper.setDead();
			
			EntityVoidCreeper voidcreeper = new EntityVoidCreeper(world);
			voidcreeper.onInitialSpawn(world.getDifficultyForLocation(pos), (IEntityLivingData)null);
			voidcreeper.setLocationAndAngles(creeper.posX, creeper.posY, creeper.posZ, creeper.rotationYaw, creeper.rotationPitch);
			voidcreeper.renderYawOffset = creeper.renderYawOffset;
			voidcreeper.setHealth(voidcreeper.getMaxHealth());

			world.spawnEntity(voidcreeper);

			return true;
		}
		
		if(entity instanceof IMobChanger)
		{
			((IMobChanger) entity).forgottenWaterEffect();
			return true;
		}
		
		return false;
	}
}
