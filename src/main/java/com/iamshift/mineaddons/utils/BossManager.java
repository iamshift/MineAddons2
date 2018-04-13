package com.iamshift.mineaddons.utils;

import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.entities.boss.EntityDeadHorse;
import com.iamshift.mineaddons.entities.boss.EntityGhostRider;
import com.iamshift.mineaddons.entities.boss.EntityVoix;
import com.iamshift.mineaddons.entities.boss.EntityWitherBlaze;
import com.iamshift.mineaddons.init.ModBlocks;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.pattern.BlockMaterialMatcher;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.WorldProviderSurface;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Refs.ID)
public class BossManager
{
	private static final Predicate<BlockWorldState> WITHER_SKELETON = new Predicate<BlockWorldState>()
	{
		public boolean apply(@Nullable BlockWorldState input) 
		{
			return input.getBlockState() != null && 
				   input.getBlockState().getBlock() == Blocks.SKULL && 
				   input.getTileEntity() instanceof TileEntitySkull && 
				   ((TileEntitySkull)input.getTileEntity()).getSkullType() == 1;		
			};
	};
	
	private static BlockPattern ghostRiderPattern = FactoryBlockPattern.start()
			.aisle("DDDDD", "DGGGD", "DGGGD", "DGGGD", "DDDDD")
			.where('D', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.DIAMOND_BLOCK)))
			.where('G', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.GOLD_BLOCK)))
			.aisle("~~F~~", "~~~~~", "F~?~F", "~~~~~", "~~F~~")
			.where('F', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.NETHER_BRICK_FENCE)))
			.where('~', BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR)))
			.where('?', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.FIRE)))
			.aisle("~~S~~", "~~~~~", "S~~~S", "~~~~~", "~~S~~")
			.where('S', WITHER_SKELETON)
			.where('~', BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR)))
			.build();
	
	private static BlockPattern witherblazePattern = FactoryBlockPattern.start()
			.aisle("DDDDD", "DGGGD", "DGGGD", "DGGGD", "DDDDD")
			.where('D', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.DIAMOND_BLOCK)))
			.where('G', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.GOLD_BLOCK)))
			.aisle("~~~~~", "~~~~~", "~~T~~", "~~~~~", "~~~~~")
			.where('T', BlockWorldState.hasState(BlockStateMatcher.forBlock(ModBlocks.SoulBlock)))
			.where('~', BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR)))
			.aisle("~~~~~", "~~~~~", "~~T~~", "~~~~~", "~~~~~")
			.where('T', BlockWorldState.hasState(BlockStateMatcher.forBlock(ModBlocks.SoulBlock)))
			.where('~', BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR)))
			.aisle("~~~~~", "~~~~~", "~~S~~", "~~~~~", "~~~~~")
			.where('S', WITHER_SKELETON)
			.where('~', BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR)))
			.build();
	
	private static BlockPattern voixPattern = FactoryBlockPattern.start()
			.aisle("DDDDD", "DGGGD", "DGGGD", "DGGGD", "DDDDD")
			.where('D', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.DIAMOND_BLOCK)))
			.where('G', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.GOLD_BLOCK)))
			.aisle("R~~~R", "~~~~~", "~~T~~", "~~~~~", "R~~~R")
			.where('R', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.END_ROD).where(BlockDirectional.FACING, Predicates.equalTo(EnumFacing.UP))))
			.where('T', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.PURPUR_BLOCK)))
			.where('~', BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR)))
			.aisle("~~~~~", "~~~~~", "~~S~~", "~~~~~", "~~~~~")
			.where('S', WITHER_SKELETON)
			.where('~', BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR)))
			.build();

	
	@SubscribeEvent
	public static void ghostRiderCreation(BlockEvent.PlaceEvent event)
	{
		BlockPos pos = event.getPos();
		World world = event.getWorld();
		
		if(pos.getY() >= 10 && world.getDifficulty() != EnumDifficulty.PEACEFUL && !world.isRemote)
		{
			if(event.getPlacedBlock().getBlock() instanceof BlockFire && world.provider instanceof WorldProviderSurface) //&& world.provider instanceof WorldProviderSurface
			{
				BlockPattern.PatternHelper helper = ghostRiderPattern.match(world, pos.down());

				if(helper != null)
				{
					world.setBlockToAir(pos);
					
					world.setBlockToAir(pos.east(2));
					world.setBlockToAir(pos.west(2));
					world.setBlockToAir(pos.north(2));
					world.setBlockToAir(pos.south(2));
					
					world.setBlockToAir(pos.east(2).up());
					world.setBlockToAir(pos.west(2).up());
					world.setBlockToAir(pos.north(2).up());
					world.setBlockToAir(pos.south(2).up());
					
					EntityDeadHorse horse = new EntityDeadHorse(world);
					horse.onInitialSpawn(world.getDifficultyForLocation(pos), (IEntityLivingData)null);
					horse.setGhost();
					horse.setPosition(pos.getX(), pos.getY(), pos.getZ());
					horse.enablePersistence();
					horse.setHorseTamed(true);
					horse.setHorseSaddled(true);
					horse.setSaddle();
					horse.setGrowingAge(0);
				
					EntityGhostRider rider = new EntityGhostRider(world);
					rider.onInitialSpawn(world.getDifficultyForLocation(pos), (IEntityLivingData)null);
					rider.setPosition(pos.getX(), pos.getY(), pos.getZ());
					rider.enablePersistence();
					rider.setInvulTime(100);
					rider.setHealth(rider.getMaxHealth());
					
					world.spawnEntity(horse);
					world.spawnEntity(rider);
					rider.startRiding(horse);
					
					world.playSound((EntityPlayer)null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.WEATHER, 10000.0F, 0.8F + new Random().nextFloat() * 0.2F);
					world.playSound((EntityPlayer)null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_LIGHTNING_IMPACT, SoundCategory.WEATHER, 2.0F, 0.5F + new Random().nextFloat() * 0.2F);
				}
			}
		}
	}

	
	@SubscribeEvent
	public static void witherBlazeCreation(BlockEvent.PlaceEvent event)
	{
		BlockPos pos = event.getPos();
		World world = event.getWorld();
		
		if(pos.getY() >= 10 && world.getDifficulty() != EnumDifficulty.PEACEFUL && !world.isRemote)
		{
			if(event.getPlacedBlock().getBlock() instanceof BlockSkull && world.getTileEntity(pos) instanceof TileEntitySkull && ((TileEntitySkull)world.getTileEntity(pos)).getSkullType() == 1 && world.provider instanceof WorldProviderHell) //&& world.provider instanceof WorldProviderHell
			{
				BlockPattern.PatternHelper helper = witherblazePattern.match(world, pos.down(3));
				
				if(helper != null)
				{
					world.setBlockToAir(pos);
					world.setBlockToAir(pos.down());
					world.setBlockToAir(pos.down(2));
					
					EntityWitherBlaze blaze = new EntityWitherBlaze(world);
					blaze.onInitialSpawn(world.getDifficultyForLocation(pos), (IEntityLivingData)null);
					blaze.setPosition(pos.getX() - .5f, pos.getY() - 2, pos.getZ() - .5f);
					blaze.enablePersistence();
					blaze.setInvulTime(100);
					blaze.setHealth(blaze.getMaxHealth());
					
					world.spawnEntity(blaze);
					
					world.playSound((EntityPlayer)null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.WEATHER, 10000.0F, 0.8F + new Random().nextFloat() * 0.2F);
				}
			}
		}
	}
	
	
	@SubscribeEvent
	public static void voixCreation(BlockEvent.PlaceEvent event)
	{
		BlockPos pos = event.getPos();
		World world = event.getWorld();
	
		if(pos.getY() >= 10 && world.getDifficulty() != EnumDifficulty.PEACEFUL && !world.isRemote)
		{
			if(event.getPlacedBlock().getBlock() instanceof BlockSkull && world.getTileEntity(pos) instanceof TileEntitySkull && ((TileEntitySkull)world.getTileEntity(pos)).getSkullType() == 1 && world.provider instanceof WorldProviderEnd) //&& world.provider instanceof WorldProviderEnd
			{
				BlockPattern.PatternHelper helper = voixPattern.match(world, pos.down(2));

				if(helper != null)
				{
					world.setBlockToAir(pos);
					world.setBlockToAir(pos.down());
					
					world.setBlockToAir(pos.down().east(2).north(2));
					world.setBlockToAir(pos.down().east(2).south(2));
					world.setBlockToAir(pos.down().west(2).north(2));
					world.setBlockToAir(pos.down().west(2).south(2));
					
					EntityVoix voix = new EntityVoix(world);
					voix.onInitialSpawn(world.getDifficultyForLocation(pos), (IEntityLivingData)null);
					voix.setPosition(pos.getX() - .5f, pos.getY() + 2, pos.getZ() - .5f);
					voix.setBoundOrigin(pos.up(2));
					voix.enablePersistence();
					voix.setInvulTime(100);
					voix.setHealth(voix.getMaxHealth());
					
					world.spawnEntity(voix);
					
					world.playSound((EntityPlayer)null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.WEATHER, 10000.0F, 0.8F + new Random().nextFloat() * 0.2F);
					world.playSound((EntityPlayer)null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_LIGHTNING_IMPACT, SoundCategory.WEATHER, 2.0F, 0.5F + new Random().nextFloat() * 0.2F);
				}
			}
		}
	}
}
