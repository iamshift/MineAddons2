package com.iamshift.mineaddons.events;

import java.util.Random;

import com.iamshift.mineaddons.core.Config;
import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.entities.EntityAncientCarp;
import com.iamshift.mineaddons.entities.EntityBrainlessShulker;
import com.iamshift.mineaddons.entities.EntityHellhound;
import com.iamshift.mineaddons.entities.boss.EntityBoss;
import com.iamshift.mineaddons.entities.boss.EntityDeadHorse;
import com.iamshift.mineaddons.init.ModEntities;
import com.iamshift.mineaddons.init.ModItems;
import com.iamshift.mineaddons.interfaces.IUncapturable;
import com.iamshift.mineaddons.utils.NoTargetHelper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.biome.BiomeHell;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

@Mod.EventBusSubscriber(modid = Refs.ID)
public class EntityEvents
{
	@SubscribeEvent
	public static void onLootTableLoad(LootTableLoadEvent event)
	{
		if (event.getName().equals(LootTableList.ENTITIES_WITHER_SKELETON)) 
		{
			final LootPool pool2 = event.getTable().getPool("pool2");

			if (pool2 != null)
				pool2.addEntry(new LootEntryItem(ModItems.WitherDust, 5, 0, new LootFunction[0], new LootCondition[0], "loottable:witherdust"));
		}

		if (event.getName().equals(LootTableList.ENTITIES_GUARDIAN)) 
		{
			final LootPool pool2 = event.getTable().getPool("pool2");

			if (pool2 != null)
				pool2.addEntry(new LootEntryItem(ModItems.RainbowBottle, 5, 0, new LootFunction[0], new LootCondition[0], "loottable:rainbowbottle"));
		}
	}

	@SubscribeEvent
	public static void onEntityDie(LivingDeathEvent event)
	{
		if(event.getEntity().world.isRemote)
			return;

		if(!event.getEntity().world.getGameRules().getBoolean("doMobLoot"))
			return;

		Entity entity = event.getEntity();
		Random rand = new Random();

		if(entity instanceof EntityWolf)
		{
			if(rand.nextInt(15) == 0)
			{
				entity.dropItem(Items.FEATHER, 1);
				return;
			}

			if(!(entity instanceof EntityHellhound))
			{
				if(rand.nextInt(15) == 0)
				{
					Item wool = Item.getItemFromBlock(Blocks.WOOL);
					ItemStack stack = new ItemStack(wool, 1, rand.nextInt(16));
					entity.entityDropItem(stack, 0.0F);
					return;
				}
			}
			else
			{
				if(rand.nextInt(15) == 0)
					entity.dropItem(Item.getItemFromBlock(Blocks.MAGMA), 1);

				return;
			}
		}

		if(entity instanceof EntityBrainlessShulker)
		{
			EntityBrainlessShulker e = (EntityBrainlessShulker) entity;

			ItemStack drop = new ItemStack(ModItems.BrainlessShulkerEgg, 1, e.getColor().getDyeDamage());
			e.entityDropItem(drop, 0.0F);
			return;
		}

		if(entity.getEntityWorld().provider instanceof WorldProviderHell && entity.getEntityWorld().getBiome(entity.getPosition()) instanceof BiomeHell)
		{
			ItemStack drop = new ItemStack(ModItems.Soul, 1);
			entity.entityDropItem(drop, 0.0F);
		}
	}

	@SubscribeEvent
	public static void onEntityJoin(EntityJoinWorldEvent event)
	{
		if(event.getWorld().isRemote)
			return;

		if(event.getEntity() instanceof EntityAncientCarp)
		{
			if(ModEntities.ANCIENT_LIMIT < Config.MaxAncientCarps)
				ModEntities.ANCIENT_LIMIT++;
			else
				event.setCanceled(true);
		}

		if(event.getEntity() instanceof EntityLiving && !(event.getEntity() instanceof EntityWither) && !(event.getEntity() instanceof EntityDragon) && !(event.getEntity() instanceof IUncapturable))
		{
			EntityLiving l = (EntityLiving) event.getEntity();
			if(l.targetTasks.taskEntries.size() > 0)
			{
				if(l.getTags().contains("notarget"))
				{
					NoTargetHelper.removeTargetTasks(l);
				}
			}
		}
	}

	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public static void onRightClick(EntityInteract event)
	{
		Item item = event.getItemStack().getItem();
		Entity target = event.getTarget();

		if(target instanceof IUncapturable)
		{
			if(Config.isCaptureItem(item.getRegistryName().getResourceDomain() + ":" + item.getRegistryName().getResourcePath()))
			{
				event.setCanceled(true);
				return;
			}
		}

		if(target instanceof EntityAncientCarp)
		{
			if(Config.CaptureAncientCarps)
			{
				if(Config.isCaptureItem(item.getRegistryName().getResourceDomain() + ":" + item.getRegistryName().getResourcePath()))
				{
					event.setCanceled(true);
					return;
				}
			}
		}
	}

	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public static void onLeftClick(AttackEntityEvent event)
	{
		EntityPlayer player = event.getEntityPlayer();
		Entity target = event.getTarget();
		Item item = player.getHeldItem(EnumHand.MAIN_HAND).getItem();

		if(target instanceof IUncapturable)
		{
			if(Config.isCaptureItem(item.getRegistryName().getResourceDomain() + ":" + item.getRegistryName().getResourcePath()))
			{
				event.setCanceled(true);
				return;
			}
		}

		if(target instanceof EntityAncientCarp)
		{
			if(Config.CaptureAncientCarps)
			{
				if(Config.isCaptureItem(item.getRegistryName().getResourceDomain() + ":" + item.getRegistryName().getResourcePath()))
				{
					event.setCanceled(true);
					return;
				}
			}
		}

		if(target instanceof EntityBoss || target instanceof EntityDeadHorse)
		{
			if(Config.isAntiBoss(item.getRegistryName().getResourceDomain() + ":" + item.getRegistryName().getResourcePath()))
			{
				if(!player.world.isRemote)
					player.sendMessage(new TextComponentTranslation("text.antiboss.msg", player.getHeldItem(EnumHand.MAIN_HAND).getDisplayName()));

				event.setCanceled(true);
				return;
			}
		}
	}

	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public static void onEntityCollide(ProjectileImpactEvent.Throwable event)
	{
		Entity target = event.getRayTraceResult().entityHit;
		EntityEntry entity = EntityRegistry.getEntry(event.getThrowable().getClass());

		if(entity == null)
			return;

		if(target instanceof IUncapturable)
		{
			if(Config.isCaptureEntity(entity.getRegistryName().getResourceDomain() + ":" + entity.getRegistryName().getResourcePath()))
			{
				event.setCanceled(true);
				return;
			}
		}

		if(target instanceof EntityAncientCarp)
		{
			if(Config.CaptureAncientCarps)
			{
				if(Config.isCaptureEntity(entity.getRegistryName().getResourceDomain() + ":" + entity.getRegistryName().getResourcePath()))
				{
					event.setCanceled(true);
					return;
				}
			}
		}
	}
}
