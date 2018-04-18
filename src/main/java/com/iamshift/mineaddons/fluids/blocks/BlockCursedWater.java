package com.iamshift.mineaddons.fluids.blocks;

import java.util.Random;

import com.iamshift.mineaddons.api.IMobChanger;
import com.iamshift.mineaddons.core.Config;
import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.entities.EntityHellhound;
import com.iamshift.mineaddons.entities.EntityZlama;
import com.iamshift.mineaddons.entities.EntityiSheep;
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
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
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

public class BlockCursedWater extends BlockFluidClassic implements IHasModel
{

	public BlockCursedWater(String name)
	{
		super(ModFluids.CursedWater, Material.WATER);
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
			ParticleUtils.spawn(EnumParticles.CURSED_CLOUD, worldIn, pos.getX() + rand.nextFloat(), pos.getY() + 1.0F, pos.getZ() + rand.nextFloat(), 0.0D, 0.0D, 0.0D);
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
			if(block instanceof BlockSacredWater)
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
				eItem.setDead();
				InventoryHelper.spawnItemStack(world, eItem.posX, eItem.posY, eItem.posZ, new ItemStack(ModItems.Lapis, eItem.getItem().getCount(), 1));
			}

			return;
		}

		if(!(entity instanceof EntityLivingBase))
			return;

		if(entity.isDead)
			return;

		if (entity instanceof EntityPlayer)
		{
			((EntityLivingBase)entity).addPotionEffect(new PotionEffect(MobEffects.WITHER, 100));
			return;
		}

		if(!Config.MobConvertion)
			return;
		
		if(((EntityLivingBase)entity).isPotionActive(ModPotions.PotionMobChanger) && ((EntityLivingBase)entity).getActivePotionEffect(ModPotions.PotionMobChanger).getDuration() <= 0)
			((EntityLivingBase)entity).removeActivePotionEffect(ModPotions.PotionMobChanger);

		if(!(((EntityLivingBase)entity).isPotionActive(ModPotions.PotionMobChanger)) && tryConvertMob(world, pos, state, entity))
			((EntityLivingBase)entity).addPotionEffect(new PotionEffect(ModPotions.PotionMobChanger, 6000));
	}

	private boolean tryConvertMob(World world, BlockPos pos, IBlockState state, Entity entity) 
	{
		if(entity instanceof IMobChanger)
		{
			((IMobChanger) entity).cursedWaterEffect();
			return true;
		}

		if (entity instanceof EntitySkeleton) 
		{
			EntitySkeleton skeleton = (EntitySkeleton) entity;
			skeleton.setDead();

			EntityWitherSkeleton witherSkeleton = new EntityWitherSkeleton(world);
			witherSkeleton.setLocationAndAngles(skeleton.posX, skeleton.posY, skeleton.posZ, skeleton.rotationYaw, skeleton.rotationPitch);
			witherSkeleton.renderYawOffset = skeleton.renderYawOffset;
			witherSkeleton.setHealth(witherSkeleton.getMaxHealth());

			world.spawnEntity(witherSkeleton);

			return true;
		}

		if (entity instanceof EntityCreeper) 
		{
			EntityCreeper creeper = (EntityCreeper) entity;

			if (!creeper.getPowered()) 
			{
				creeper.onStruckByLightning(null);
				creeper.setHealth(creeper.getMaxHealth());

				return true;
			}
		}

		if (entity instanceof EntitySpider && !(entity instanceof EntityCaveSpider)) 
		{
			EntitySpider spider = (EntitySpider) entity;
			spider.setDead();

			EntityCaveSpider caveSpider = new EntityCaveSpider(world);
			caveSpider.setLocationAndAngles(spider.posX, spider.posY, spider.posZ, spider.rotationYaw, spider.rotationPitch);
			caveSpider.renderYawOffset = spider.renderYawOffset;
			caveSpider.setHealth(caveSpider.getMaxHealth());

			world.spawnEntity(caveSpider);

			return true;
		}

		if (entity instanceof EntitySquid) 
		{
			EntitySquid squid = (EntitySquid) entity;
			squid.setDead();

			EntityGhast ghast = new EntityGhast(world);
			ghast.setLocationAndAngles(squid.posX, squid.posY, squid.posZ, squid.rotationYaw, squid.rotationPitch);
			ghast.renderYawOffset = squid.renderYawOffset;
			ghast.setHealth(ghast.getMaxHealth());

			world.spawnEntity(ghast);

			return true;
		}

		if (entity instanceof EntitySilverfish) 
		{
			EntitySilverfish silverfish = (EntitySilverfish) entity;
			silverfish.setDead();

			EntityEndermite endermite = new EntityEndermite(world);
			endermite.setLocationAndAngles(silverfish.posX, silverfish.posY, silverfish.posZ, silverfish.rotationYaw, silverfish.rotationPitch);
			endermite.renderYawOffset = silverfish.renderYawOffset;
			endermite.setHealth(endermite.getMaxHealth());

			world.spawnEntity(endermite);

			return true;
		}

		if (entity instanceof EntityVillager) 
		{
			EntityVillager villager = (EntityVillager) entity;
			villager.setDead();

			EntityWitch witch = new EntityWitch(world);
			witch.setLocationAndAngles(villager.posX, villager.posY, villager.posZ, villager.rotationYaw, villager.rotationPitch);
			witch.renderYawOffset = villager.renderYawOffset;
			witch.setHealth(witch.getMaxHealth());

			world.spawnEntity(witch);

			return true;
		}

		if (entity instanceof EntityGuardian) 
		{
			EntityGuardian guardian = (EntityGuardian) entity;
			guardian.setDead();

			EntityElderGuardian elderGuardian = new EntityElderGuardian(world);
			elderGuardian.setLocationAndAngles(guardian.posX, guardian.posY, guardian.posZ, guardian.rotationYaw, guardian.rotationPitch);
			elderGuardian.renderYawOffset = guardian.renderYawOffset;
			elderGuardian.setHealth(elderGuardian.getMaxHealth());

			world.spawnEntity(elderGuardian);

			return true;
		}

		if (entity instanceof EntityBat) 
		{
			EntityBat bat = (EntityBat) entity;
			bat.setDead();

			EntityBlaze blaze = new EntityBlaze(world);
			blaze.setLocationAndAngles(bat.posX, bat.posY, bat.posZ, bat.rotationYaw, bat.rotationPitch);
			blaze.renderYawOffset = bat.renderYawOffset;
			blaze.setHealth(blaze.getMaxHealth());

			world.spawnEntity(blaze);

			return true;
		}

		if (entity instanceof EntityHorse) 
		{
			EntityHorse horse = (EntityHorse) entity;
			horse.setDead();

			EntityZombieHorse zombieHorse = new EntityZombieHorse(world);
			zombieHorse.setLocationAndAngles(horse.posX, horse.posY, horse.posZ, horse.rotationYaw, horse.rotationPitch);
			zombieHorse.renderYawOffset = horse.renderYawOffset;
			zombieHorse.setHealth(zombieHorse.getMaxHealth());

			world.spawnEntity(zombieHorse);

			return true;
		}

		if (entity instanceof EntityRabbit) 
		{
			EntityRabbit rabbit = (EntityRabbit) entity;

			if (rabbit.getRabbitType() != 99) 
			{
				rabbit.setRabbitType(99);
				rabbit.setHealth(rabbit.getMaxHealth());

				return true;
			}
		}

		if (entity instanceof EntityZombie && !(entity instanceof EntityPigZombie)) 
		{
			EntityZombie zombie = (EntityZombie) entity;
			zombie.setDead();

			EntityPigZombie pigzombie = new EntityPigZombie(world);
			pigzombie.setLocationAndAngles(zombie.posX, zombie.posY, zombie.posZ, zombie.rotationYaw, zombie.rotationPitch);
			pigzombie.renderYawOffset = zombie.renderYawOffset;
			pigzombie.setHealth(pigzombie.getMaxHealth());

			world.spawnEntity(pigzombie);

			return true;
		}

		if(entity instanceof EntityWolf)
		{
			EntityWolf wolf = (EntityWolf) entity;
			wolf.setDead();

			EntityHellhound hellhound = new EntityHellhound(world);
			hellhound.setLocationAndAngles(wolf.posX, wolf.posY, wolf.posZ, wolf.rotationYaw, wolf.rotationPitch);
			hellhound.renderYawOffset = wolf.renderYawOffset;
			hellhound.setHealth(hellhound.getMaxHealth());

			world.spawnEntity(hellhound);

			return true;
		}

		if(entity instanceof EntitySheep)
		{
			EntitySheep sheep = (EntitySheep) entity;
			sheep.setDead();

			EntityiSheep isheep = new EntityiSheep(world);
			isheep.setLocationAndAngles(sheep.posX, sheep.posY, sheep.posZ, sheep.rotationYaw, sheep.rotationPitch);
			isheep.renderYawOffset = sheep.renderYawOffset;
			isheep.setHealth(isheep.getMaxHealth());

			world.spawnEntity(isheep);

			return true;
		}

		if(entity instanceof EntityLlama)
		{
			EntityLlama llama = (EntityLlama) entity;
			llama.setDead();

			EntityZlama zlama = new EntityZlama(world);
			zlama.setLocationAndAngles(llama.posX, llama.posY, llama.posZ, llama.rotationYaw, llama.rotationPitch);
			zlama.renderYawOffset = llama.renderYawOffset;
			zlama.setHealth(zlama.getMaxHealth());

			world.spawnEntity(zlama);

			return true;
		}

		return false;
	}
}
