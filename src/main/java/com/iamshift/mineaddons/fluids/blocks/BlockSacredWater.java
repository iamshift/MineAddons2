package com.iamshift.mineaddons.fluids.blocks;

import java.util.Random;

import com.iamshift.mineaddons.api.IMobChanger;
import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.init.ModBlocks;
import com.iamshift.mineaddons.init.ModFluids;
import com.iamshift.mineaddons.init.ModItems;
import com.iamshift.mineaddons.init.ModPotions;
import com.iamshift.mineaddons.interfaces.IHasModel;
import com.iamshift.mineaddons.particles.ParticleUtils;
import com.iamshift.mineaddons.particles.ParticleUtils.EnumParticles;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityElderGuardian;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityZombieHorse;
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

			if(biome != Biomes.OCEAN && biome != Biomes.DEEP_OCEAN)
			{
				int level = block.getMetaFromState(state);
				world.setBlockState(pos, this.getDefaultState().withProperty(LEVEL, level));
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
				eItem.setDead();
				InventoryHelper.spawnItemStack(world, eItem.posX, eItem.posY, eItem.posZ, new ItemStack(ModItems.Lapis, eItem.getItem().getCount(), 0));
			}
			
			return;
		}
		
		if(!(entity instanceof EntityLivingBase))
			return;

		if(entity.isDead)
			return;
		
		if (entity instanceof EntityPlayer)
		{
			((EntityLivingBase)entity).addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 100));
			return;
		}

		if(((EntityLivingBase)entity).isPotionActive(ModPotions.PotionMobChanger) && ((EntityLivingBase)entity).getActivePotionEffect(ModPotions.PotionMobChanger).getDuration() <= 0)
			((EntityLivingBase)entity).removeActivePotionEffect(ModPotions.PotionMobChanger);

		if(!(((EntityLivingBase)entity).isPotionActive(ModPotions.PotionMobChanger)) && tryConvertMob(world, pos, state, entity))
			((EntityLivingBase)entity).addPotionEffect(new PotionEffect(ModPotions.PotionMobChanger, 6000));
	}

	private boolean tryConvertMob(World world, BlockPos pos, IBlockState state, Entity entity) 
	{
		if(entity instanceof IMobChanger)
		{
			((IMobChanger) entity).sacredWaterEffect();
			return true;
		}
		
		if(entity instanceof EntityWitherSkeleton)
		{
			EntityWitherSkeleton witherSkeleton = (EntityWitherSkeleton) entity;
			witherSkeleton.setDead();
			
			EntitySkeleton skeleton = new EntitySkeleton(world);
			skeleton.setLocationAndAngles(witherSkeleton.posX, witherSkeleton.posY, witherSkeleton.posZ, witherSkeleton.rotationYaw, witherSkeleton.rotationPitch);
			skeleton.renderYawOffset = witherSkeleton.renderYawOffset;
			skeleton.setHealth(skeleton.getMaxHealth());
			
			world.spawnEntity(skeleton);
			
			return true;
		}

		if (entity instanceof EntityCreeper) 
		{
			EntityCreeper creeper = (EntityCreeper) entity;

			if (creeper.getPowered()) 
			{
				creeper.setDead();

				EntityCreeper newCreeper = new EntityCreeper(world);
				newCreeper.setLocationAndAngles(creeper.posX, creeper.posY, creeper.posZ, creeper.rotationYaw, creeper.rotationPitch);
				newCreeper.renderYawOffset = creeper.renderYawOffset;
				newCreeper.setHealth(newCreeper.getMaxHealth());

				world.spawnEntity(newCreeper);

				return true;
			}
		}

		if (entity instanceof EntityCaveSpider) 
		{
			EntityCaveSpider cavespider = (EntityCaveSpider) entity;
			cavespider.setDead();

			EntitySpider spider = new EntitySpider(world);
			spider.setLocationAndAngles(cavespider.posX, cavespider.posY, cavespider.posZ, cavespider.rotationYaw, cavespider.rotationPitch);
			spider.renderYawOffset = cavespider.renderYawOffset;
			spider.setHealth(spider.getMaxHealth());

			world.spawnEntity(spider);

			return true;
		}

		if(entity instanceof EntityGhast)
		{
			EntityGhast ghast = (EntityGhast) entity;
			ghast.setDead();

			EntitySquid squid = new EntitySquid(world);
			squid.setLocationAndAngles(ghast.posX, ghast.posY, ghast.posZ, ghast.rotationYaw, ghast.rotationPitch);
			squid.renderYawOffset = ghast.renderYawOffset;
			squid.setHealth(squid.getMaxHealth());

			world.spawnEntity(squid);

			return true;
		}

		if(entity instanceof EntityEndermite)
		{
			EntityEndermite endermite = (EntityEndermite) entity;
			endermite.setDead();

			EntitySilverfish silverfish = new EntitySilverfish(world);
			silverfish.setLocationAndAngles(endermite.posX, endermite.posY, endermite.posZ, endermite.rotationYaw, endermite.rotationPitch);
			silverfish.renderYawOffset = endermite.renderYawOffset;
			silverfish.setHealth(silverfish.getMaxHealth());

			world.spawnEntity(silverfish);

			return true;
		}

		if(entity instanceof EntityWitch)
		{
			EntityWitch witch = (EntityWitch) entity;
			witch.setDead();

			EntityVillager villager = new EntityVillager(world);
			villager.setLocationAndAngles(witch.posX, witch.posY, witch.posZ, witch.rotationYaw, witch.rotationPitch);
			villager.renderYawOffset = witch.renderYawOffset;
			villager.setHealth(villager.getMaxHealth());

			world.spawnEntity(villager);

			return true;
		}

		if (entity instanceof EntityElderGuardian) 
		{
			EntityElderGuardian elderGuardian = (EntityElderGuardian) entity;
			elderGuardian.setDead();
			
			EntityGuardian guardian = new EntityGuardian(world);
			guardian.setLocationAndAngles(elderGuardian.posX, elderGuardian.posY, elderGuardian.posZ, elderGuardian.rotationYaw, elderGuardian.rotationPitch);
			guardian.renderYawOffset = elderGuardian.renderYawOffset;
			guardian.setHealth(guardian.getMaxHealth());

			world.spawnEntity(guardian);

			return true;
		}

		if(entity instanceof EntityBlaze)
		{
			EntityBlaze blaze = (EntityBlaze) entity;
			blaze.setDead();

			EntityBat bat = new EntityBat(world);
			bat.setLocationAndAngles(blaze.posX, blaze.posY, blaze.posZ, blaze.rotationYaw, blaze.rotationPitch);
			bat.renderYawOffset = blaze.renderYawOffset;
			bat.setHealth(bat.getMaxHealth());

			world.spawnEntity(bat);

			return true;
		}

		if (entity instanceof EntityZombieHorse) 
		{
			EntityZombieHorse zombieHorse = (EntityZombieHorse) entity;
			zombieHorse.setDead();
			
			int rand = new Random().nextInt(3);
			
			EntityHorse horse = new EntityHorse(world);
			horse.setHorseVariant(rand);
			horse.setLocationAndAngles(zombieHorse.posX, zombieHorse.posY, zombieHorse.posZ, zombieHorse.rotationYaw, zombieHorse.rotationPitch);
			horse.renderYawOffset = zombieHorse.renderYawOffset;
			horse.setHealth(horse.getMaxHealth());

			world.spawnEntity(horse);

			return true;
		}

		if (entity instanceof EntityRabbit) 
		{
			EntityRabbit rabbit = (EntityRabbit) entity;

			if (rabbit.getRabbitType() == 99) 
			{
				int rand = new Random().nextInt(6);

				rabbit.setRabbitType(rand);
				rabbit.setHealth(rabbit.getMaxHealth());

				return true;
			}
		}

		if(entity instanceof EntityPigZombie)
		{
			EntityPigZombie pigzombie = (EntityPigZombie) entity;
			pigzombie.setDead();

			EntityZombie zombie = new EntityZombie(world);
			zombie.setLocationAndAngles(pigzombie.posX, pigzombie.posY, pigzombie.posZ, pigzombie.rotationYaw, pigzombie.rotationPitch);
			zombie.renderYawOffset = pigzombie.renderYawOffset;
			zombie.setHealth(zombie.getMaxHealth());

			world.spawnEntity(zombie);

			return true;
		}
		
		return false;
	}
}
