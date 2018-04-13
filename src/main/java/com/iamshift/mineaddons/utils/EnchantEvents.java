package com.iamshift.mineaddons.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.entities.boss.EntityBoss;
import com.iamshift.mineaddons.init.ModEnchants;
import com.iamshift.mineaddons.init.ModNetwork;
import com.iamshift.mineaddons.items.ItemWings;
import com.iamshift.mineaddons.network.PacketToggle;
import com.iamshift.mineaddons.proxy.ClientProxy;

import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = Refs.ID)
public class EnchantEvents
{
	@SubscribeEvent
	public static void enchantsDrops(LivingDeathEvent event)
	{
		if(event.getEntity().world.isRemote)
			return;
		
		if(!event.getEntity().world.getGameRules().getBoolean("doMobLoot"))
			return;
		
		Entity entity = event.getEntity();
		Random rand = new Random();
		
		if(entity instanceof EntityBoss || entity instanceof EntityWither || entity instanceof EntityDragon)
		{
			if(rand.nextInt(1) == 0)
			{
				ItemStack stack;
				if(rand.nextBoolean())
					stack = ItemEnchantedBook.getEnchantedItemStack(new EnchantmentData(ModEnchants.elytra, 1));
				else
					stack = ItemEnchantedBook.getEnchantedItemStack(new EnchantmentData(ModEnchants.rocket, 1));
				
				entity.entityDropItem(stack, 0F);
			}
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void rocketToggle(KeyInputEvent event)
	{
		if(ClientProxy.toggleRocket.isPressed())
		{
			ItemStack stack = Minecraft.getMinecraft().player.getItemStackFromSlot(EntityEquipmentSlot.FEET);

			if(!stack.isEmpty())
			{
				if(EnchantmentHelper.getEnchantments(stack).containsKey(ModEnchants.rocket))
				{
					if(stack.hasTagCompound() && stack.getTagCompound().hasKey("Rocket"))
					{
						int flag = stack.getSubCompound("Rocket").getInteger("Active");
						ModNetwork.INSTANCE.sendToServer(new PacketToggle(flag));
					}
				}
			}
		}
	}

	private static Map<EntityPlayer, Integer> launch = new HashMap<EntityPlayer, Integer>();
	private static final int LTimer = 60;

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent event)
	{
		if(event.phase == Phase.END)
		{
			EntityPlayer player = event.player;

			if(player.isSneaking() && player.onGround)
			{
				ItemStack stack = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
				if(!stack.isEmpty() && stack.getItem() instanceof ItemArmor && ((ItemArmor)stack.getItem()).armorType == EntityEquipmentSlot.FEET)
				{
					if(EnchantmentHelper.getEnchantments(stack).containsKey(ModEnchants.rocket))
					{
						int t = 0;
						if(stack.hasTagCompound() && stack.getTagCompound().hasKey("Rocket") && stack.getSubCompound("Rocket").getInteger("Active") == 1)
						{
							if(launch.containsKey(player))
							{
								t = launch.get(player);
								if(t < LTimer)
									++t;
							}

							Random r = new Random();
							double mX = ((double)r.nextFloat() - 0.5D) * 0.5D;;
							double mY = ((double)r.nextFloat() - 0.5D) * 0.5D;;
							double mZ = ((double)r.nextFloat() - 0.5D) * 0.5D;;
							int j = r.nextInt(2) - 1;

							if(r.nextInt(2) == 0)
								mX = (double)(r.nextFloat() * (float)j);
							else
								mZ = (double)(r.nextFloat() * (float)j);

							player.world.spawnParticle(EnumParticleTypes.CLOUD, player.posX, player.posY, player.posZ, mX, mY, mZ);

							launch.put(player, t);
						}
					}
				}
			}
			else
			{
				if(launch.containsKey(player))
				{
					double force = (launch.get(player) / 20);
					player.addVelocity(0D, force, 0D);
					player.playSound(SoundEvents.ENTITY_FIREWORK_LAUNCH, 1.0F, 1.0F);

					launch.remove(player);
				}
				else 
				{
					if(player.isElytraFlying())
					{
						ItemStack stack = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
						if(!stack.isEmpty() && stack.getItem() instanceof ItemArmor && ((ItemArmor)stack.getItem()).armorType == EntityEquipmentSlot.FEET)
						{
							if(EnchantmentHelper.getEnchantments(stack).containsKey(ModEnchants.rocket))
							{
								if(stack.hasTagCompound())
								{
									if(stack.getTagCompound().hasKey("Rocket") && stack.getSubCompound("Rocket").getInteger("Active") == 1)
									{
										if (player.world.isRemote)
										{
											Vec3d look = player.getLookVec();
											player.motionX += look.x * 0.1D + (look.x * 0.75D - player.motionX) * 0.6D;
											player.motionY += look.y * 0.1D + (look.y * 0.75D - player.motionY) * 0.6D;
											player.motionZ += look.z * 0.1D + (look.z * 0.75D - player.motionZ) * 0.6D;
										}
										
										player.world.spawnParticle(EnumParticleTypes.CLOUD, player.posX - player.getLookVec().x, player.posY - player.getLookVec().y, player.posZ - player.getLookVec().z, -player.getLookVec().x, -player.getLookVec().y, -player.getLookVec().z);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onFallDamage(LivingHurtEvent event)
	{
		if(event.getEntityLiving() == null)
			return;

		if(!(event.getEntityLiving() instanceof EntityPlayer))
			return;

		if(event.getSource() != DamageSource.FALL)
			return;

		EntityPlayer player = (EntityPlayer) event.getEntityLiving();
		ItemStack stack = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);

		if(stack != null && isFeet(stack) && isRocketEnchant(stack))
		{
			if(stack.getTagCompound().hasKey("Rocket") && stack.getSubCompound("Rocket").getInteger("Active") == 1)
				event.setCanceled(true);
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onTooltipHandler(ItemTooltipEvent event)
	{
		ItemStack stack = event.getItemStack();

		if(stack.getItem() instanceof ItemArmor)
		{
			if(event.getToolTip().contains("Rocket Power"))
			{
				if(stack.hasTagCompound())
				{
					if(stack.getTagCompound().hasKey("Rocket"))
					{
						NBTTagCompound compound = stack.getSubCompound("Rocket");

						if(compound.hasKey("Active"))
						{
							String nt = compound.getInteger("Active") == 0 ? "Rocket Power" + " " + TextFormatting.RED + "OFF" : "Rocket Power" + " " + TextFormatting.GREEN + "ON";
							event.getToolTip().set(event.getToolTip().indexOf("Rocket Power"), nt);
						}

					}
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void rocketEnchant(AnvilUpdateEvent event)
	{
		if(isFeet(event.getLeft()))
		{
			ItemStack out = event.getLeft().copy();

			if(isRocketEnchant(event.getRight()))
			{
				out.addEnchantment(ModEnchants.rocket, 1);
				out.getOrCreateSubCompound("Rocket").setInteger("Active", 0);
				event.setOutput(out);
				event.setMaterialCost(1);
				event.setCost(20);
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void elytraEnchant(AnvilUpdateEvent event)
	{
		if(isChest(event.getLeft()))
		{
			ItemStack out = event.getLeft().copy();

			if(isElytraEnchant(event.getRight()))
			{
				out.addEnchantment(ModEnchants.elytra, 1);
				event.setOutput(out);
				event.setMaterialCost(1);
				event.setCost(20);
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void wingsEnchant(AnvilUpdateEvent event)
	{
		if(isChest(event.getLeft()))
		{
			ItemStack out = event.getLeft().copy();

			if(isWingsEnchant(event.getRight()))
			{
				out.addEnchantment(ModEnchants.wings, 1);
				event.setOutput(out);
				event.setMaterialCost(1);
				event.setCost(20);
			}
		}
	}

	@SubscribeEvent
	public static void elytraTOwings(AnvilUpdateEvent event)
	{
		if(isElytraEnchant(event.getLeft()) && event.getRight().getItem() instanceof ItemWings)
		{
			ItemStack out = new ItemStack(Items.ENCHANTED_BOOK, 1, 0);

			ItemEnchantedBook.addEnchantment(out, new EnchantmentData(ModEnchants.wings, 1));
			event.setOutput(out);
			event.setMaterialCost(1);
			event.setCost(1);
		}
	}

	private static boolean isChest(ItemStack stack)
	{
		if(!(stack.getItem() instanceof ItemArmor))
			return false;

		return ((ItemArmor)stack.getItem()).armorType == EntityEquipmentSlot.CHEST;
	}

	private static boolean isFeet(ItemStack stack)
	{
		if(!(stack.getItem() instanceof ItemArmor))
			return false;

		return ((ItemArmor)stack.getItem()).armorType == EntityEquipmentSlot.FEET;
	}

	private static boolean isElytraEnchant(ItemStack stack)
	{
		if(!(stack.getItem() instanceof ItemEnchantedBook))
			return false;

		return EnchantmentHelper.getEnchantments(stack).containsKey(ModEnchants.elytra);
	}

	private static boolean isWingsEnchant(ItemStack stack)
	{
		if(!(stack.getItem() instanceof ItemEnchantedBook))
			return false;

		return EnchantmentHelper.getEnchantments(stack).containsKey(ModEnchants.wings);
	}

	private static boolean isRocketEnchant(ItemStack stack)
	{
		if(!(stack.getItem() instanceof ItemEnchantedBook))
			return false;

		return EnchantmentHelper.getEnchantments(stack).containsKey(ModEnchants.rocket);
	}
}
