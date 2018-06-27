package com.iamshift.mineaddons.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.init.ModEnchants;
import com.iamshift.mineaddons.init.ModNetwork;
import com.iamshift.mineaddons.init.ModPotions;
import com.iamshift.mineaddons.items.armors.ItemFiberglassArmor;
import com.iamshift.mineaddons.items.armors.ItemUltimateArmor;
import com.iamshift.mineaddons.network.PacketToggle;
import com.iamshift.mineaddons.proxy.ClientProxy;

import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = Refs.ID)
public class ArmorEvents 
{
	public static HashMap<UUID, Integer> setItems = new HashMap<UUID, Integer>();
	private static HashMap<UUID, Integer> oldCount = new HashMap<>();

	public static final HashMap<EntityEquipmentSlot, PotionEffect> armorEffects = new HashMap<>();

	private static List<EntityPlayer> flyOn = new ArrayList<EntityPlayer>();
	
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent event)
	{
		if(event.phase == TickEvent.Phase.START)
		{
			EntityPlayer player = event.player;
			List<PotionEffect> potions = new ArrayList<PotionEffect>(armorEffects.values());
			int i = 0;
			int count = 0;
			for (ItemStack stack : player.inventory.armorInventory) 
			{
				if(stack.getItem() instanceof ItemUltimateArmor)
					i++;

				if(stack.getItem() instanceof ItemFiberglassArmor || stack.getItem() instanceof ItemUltimateArmor)
				{
					potions.remove(armorEffects.get(((ItemArmor)stack.getItem()).armorType));
					count++;
				}
			}

			if(event.side == Side.CLIENT)
				setItems.put(player.getUniqueID(), i);
			else
			{
				if (oldCount.containsKey(player.getUniqueID()) && count == oldCount.get(player.getUniqueID()))
					return;
				
				for(PotionEffect effect : potions)
				{
					if(player.isPotionActive(effect.getPotion()) && player.getActivePotionEffect(effect.getPotion()).getAmplifier() == 0 && player.getActivePotionEffect(effect.getPotion()).getDuration() >= 900000)
					{
						player.removePotionEffect(effect.getPotion());
						player.addPotionEffect(new PotionEffect(effect.getPotion(), 5, effect.getAmplifier(), effect.getIsAmbient(), effect.doesShowParticles()));
					}
				}
				
				if(i < 2)
				{
					if(player.isPotionActive(ModPotions.PotionDoubleHealth))
						player.removePotionEffect(ModPotions.PotionDoubleHealth);

					if(player.isPotionActive(ModPotions.PotionWitherProof))
						player.removePotionEffect(ModPotions.PotionWitherProof);

					if(player.isPotionActive(ModPotions.PotionFlight))
						player.removePotionEffect(ModPotions.PotionFlight);
				}

				if(i >= 2)
				{
					//double hp
					if(!player.isPotionActive(ModPotions.PotionDoubleHealth) || (player.isPotionActive(ModPotions.PotionDoubleHealth) && player.getActivePotionEffect(ModPotions.PotionDoubleHealth).getDuration() <= 900005))
					{
						PotionEffect effect = new PotionEffect(ModPotions.PotionDoubleHealth, 999999, 0);
						effect.setCurativeItems(new ArrayList<>());

						if (player.world.isRemote)
							effect.setPotionDurationMax(true);

						player.addPotionEffect(effect);
					}

					if(i < 3)
						if(player.isPotionActive(ModPotions.PotionWitherProof))
							player.removePotionEffect(ModPotions.PotionWitherProof);

					if(i < 4)
						if(player.isPotionActive(ModPotions.PotionFlight))
							player.removePotionEffect(ModPotions.PotionFlight);
				}

				if(i >= 3)
				{
					//wither proof
					if(!player.isPotionActive(ModPotions.PotionWitherProof) || (player.isPotionActive(ModPotions.PotionWitherProof) && player.getActivePotionEffect(ModPotions.PotionWitherProof).getDuration() <= 900005))
					{
						PotionEffect effect = new PotionEffect(ModPotions.PotionWitherProof, 999999, 0);
						effect.setCurativeItems(new ArrayList<>());

						if (player.world.isRemote)
							effect.setPotionDurationMax(true);

						player.addPotionEffect(effect);
					}

					if(i < 4)
						if(player.isPotionActive(ModPotions.PotionFlight))
							player.removePotionEffect(ModPotions.PotionFlight);
				}

				if(i == 4)
				{
					//fly
					if(!player.isPotionActive(ModPotions.PotionFlight) || (player.isPotionActive(ModPotions.PotionFlight) && player.getActivePotionEffect(ModPotions.PotionFlight).getDuration() <= 900005))
					{
						PotionEffect effect = new PotionEffect(ModPotions.PotionFlight, 999999, 0);
						effect.setCurativeItems(new ArrayList<>());

						if (player.world.isRemote)
							effect.setPotionDurationMax(true);

						player.addPotionEffect(effect);
					}
				}
				
				oldCount.put(player.getUniqueID(), count);
			}
		}
	}

	@SubscribeEvent
	public static void onLivingUpdate(LivingUpdateEvent event)
	{
		if(!(event.getEntityLiving() instanceof EntityPlayer))
			return;
		
		EntityPlayer player = (EntityPlayer) event.getEntityLiving();

		if(player.isPotionActive(ModPotions.PotionWitherProof))
		{
			if(player.isPotionActive(MobEffects.WITHER))
				player.removePotionEffect(MobEffects.WITHER);
		}

		if(!player.isCreative() && !player.isSpectator())
		{
			if(player.isPotionActive(ModPotions.PotionFlight))
			{
				if(!player.capabilities.allowFlying)
					player.capabilities.allowFlying = true;

				if(player.getPosition().getY() < 0)
				{
					player.capabilities.isFlying = true;
					player.setLocationAndAngles(player.posX, 1, player.posZ, player.cameraYaw, player.cameraPitch);
				}

				if(!flyOn.contains(player))
					flyOn.add(player);
			}
			else if(flyOn.contains(player))
			{
				if(player.world.isRemote)
				{
					player.capabilities.allowFlying = false;

					player.capabilities.isFlying = false;

					flyOn.remove(player);
				}
			}
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void armorToggle(KeyInputEvent event)
	{
		if(ClientProxy.toggleVision.isPressed())
		{
			ItemStack stack = Minecraft.getMinecraft().player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);

			if(!stack.isEmpty())
			{
				if(stack.hasTagCompound() && stack.getTagCompound().hasKey("ArmorEffect"))
				{
					int flag = stack.getSubCompound("ArmorEffect").getInteger("Active");
					ModNetwork.INSTANCE.sendToServer(new PacketToggle(flag, 1));
				}
			}
		}
	}
}
