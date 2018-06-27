package com.iamshift.mineaddons.fluids.blocks;

import java.util.Random;

import com.iamshift.mineaddons.api.IMobChanger;
import com.iamshift.mineaddons.core.Config;
import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.init.ModBlocks;
import com.iamshift.mineaddons.init.ModFluids;
import com.iamshift.mineaddons.init.ModItems;
import com.iamshift.mineaddons.init.ModPotions;
import com.iamshift.mineaddons.interfaces.IHasModel;
import com.iamshift.mineaddons.particles.ParticleUtils;
import com.iamshift.mineaddons.particles.ParticleUtils.EnumParticles;
import com.iamshift.mineaddons.utils.ConversionHelper;
import com.iamshift.mineaddons.utils.ConversionHelper.Conversion;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSacredWater extends BlockFluidClassic implements IHasModel
{
	private static final int MAXDELAY = 20;
	private int delay = 0;
	
	public BlockSacredWater(String name)
	{
		super(ModFluids.SacredWater, Material.WATER);
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
	protected boolean canFlowInto(IBlockAccess world, BlockPos pos)
	{
		if(!world.isAirBlock(pos)) return false;

		return true;
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
			ParticleUtils.spawn(EnumParticles.SACRED_CLOUD, worldIn, pos.getX() + rand.nextFloat(), pos.getY() + 1.0F, pos.getZ() + rand.nextFloat(), 0.0D, 0.0D, 0.0D);
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

		Biome biome = world.getBiome(pos);
		if(material == Material.WATER)
		{
			if(block instanceof BlockCursedWater)
			{
				world.setBlockState(pos, ModBlocks.FrozenForgottenWater.getDefaultState());
				return false;
			}

			if(Config.WaterSpread)
			{
				if(biome != Biomes.OCEAN && biome != Biomes.DEEP_OCEAN && biome != Biomes.RIVER && biome != Biomes.FROZEN_RIVER)
				{
					int level = block.getMetaFromState(state);
					world.setBlockState(pos, this.getDefaultState().withProperty(LEVEL, level));
					return false;
				}
			}
			else
			{
				world.setBlockState(pos, Blocks.COBBLESTONE.getDefaultState());
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

		if(entity instanceof EntityItem)
		{
			EntityItem eItem = (EntityItem) entity;

			if(eItem.getItem().getItem() instanceof ItemDye && eItem.getItem().getItemDamage() == 4)
			{
				eItem.getItem().shrink(1);
				
				if(eItem.getItem().getCount() <= 0)
					eItem.setDead();
				
				InventoryHelper.spawnItemStack(world, eItem.posX, eItem.posY, eItem.posZ, new ItemStack(ModItems.Lapis, 1, 0));
			}

			return;
		}

		if(!(entity instanceof EntityLivingBase))
			return;

		if(entity.isDead)
			return;

		if (entity instanceof EntityPlayer)
		{
			if(delay >= MAXDELAY)
			{
				((EntityLivingBase)entity).addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 100));
				delay = 0;
			}
			else
				++delay;
			
			return;
		}

		if(!Config.MobConvertion)
			return;

		if(((EntityLivingBase)entity).isPotionActive(ModPotions.PotionMobChanger) && ((EntityLivingBase)entity).getActivePotionEffect(ModPotions.PotionMobChanger).getDuration() <= 0)
			((EntityLivingBase)entity).removeActivePotionEffect(ModPotions.PotionMobChanger);

		if(!(((EntityLivingBase)entity).isPotionActive(ModPotions.PotionMobChanger)))
			tryConvert(world, pos, entity);
	}
	
	private void tryConvert(World world, BlockPos pos, Entity entity)
	{
		for(Conversion c : ConversionHelper.sacredList)
		{
			EntityLiving o = (EntityLiving) EntityList.createEntityByIDFromName(c.getOutput(), world);
			EntityLiving i = (EntityLiving) EntityList.createEntityByIDFromName(c.getInput(), world);
			
			if(o == null)
				return;
			
			if(c.getInput().equals(EntityList.getKey(entity)))
			{
				entity.setDead();
				
				o.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
				o.onInitialSpawn(world.getDifficultyForLocation(pos), (IEntityLivingData)null);
				
				o.setHealth(o.getMaxHealth());
				o.addPotionEffect(new PotionEffect(ModPotions.PotionMobChanger, 6000));
				world.spawnEntity(o);
				
				return;
			}
		}
		
		if (entity instanceof EntityCreeper) 
		{
			EntityCreeper creeper = (EntityCreeper) entity;

			if (creeper.getPowered()) 
			{
				creeper.setDead();

				EntityCreeper newCreeper = new EntityCreeper(world);
				newCreeper.onInitialSpawn(world.getDifficultyForLocation(pos), (IEntityLivingData)null);
				newCreeper.setLocationAndAngles(creeper.posX, creeper.posY, creeper.posZ, creeper.rotationYaw, creeper.rotationPitch);
				newCreeper.renderYawOffset = creeper.renderYawOffset;
				newCreeper.setHealth(newCreeper.getMaxHealth());

				world.spawnEntity(newCreeper);

				return;
			}
		}
		
		if (entity instanceof EntityRabbit) 
		{
			EntityRabbit rabbit = (EntityRabbit) entity;

			if (rabbit.getRabbitType() == 99) 
			{
				int rand = new Random().nextInt(6);

				rabbit.setRabbitType(rand);
				rabbit.setHealth(rabbit.getMaxHealth());

				return;
			}
		}
		
		if(entity instanceof IMobChanger)
		{
			((IMobChanger) entity).cursedWaterEffect();
			return;
		}
	}
}
