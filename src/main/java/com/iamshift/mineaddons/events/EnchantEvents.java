package com.iamshift.mineaddons.events;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.iamshift.mineaddons.core.Config;
import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.init.ModEnchants;
import com.iamshift.mineaddons.init.ModNetwork;
import com.iamshift.mineaddons.init.ModPotions;
import com.iamshift.mineaddons.items.ItemWings;
import com.iamshift.mineaddons.items.armors.ItemUltimateArmor;
import com.iamshift.mineaddons.network.PacketToggle;
import com.iamshift.mineaddons.proxy.ClientProxy;
import com.iamshift.mineaddons.utils.ForgottenAnvilHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
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
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
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
	public static void hungryEnchantApply(LivingEntityUseItemEvent.Tick event)
	{
		if(!(event.getEntityLiving() instanceof EntityPlayer))
			return;

		ItemStack head = event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.HEAD);
		ItemStack food = event.getItem();

		if(head.isEmpty() || food.isEmpty())
			return;

		if(!(food.getItem() instanceof ItemFood))
			return;

		if(!head.isItemEnchanted() || !EnchantmentHelper.getEnchantments(head).containsKey(ModEnchants.hungry))
			return;

		event.setDuration(0);
	}

	@SubscribeEvent
	public static void enchantsDrops(LivingDeathEvent event)
	{
		if(event.getEntity().world.isRemote)
			return;

		if(!event.getEntity().world.getGameRules().getBoolean("doMobLoot"))
			return;

		Entity entity = event.getEntity();
		Random rand = new Random();
		ItemStack stack;

		if(Config.RocketEnchantDropRate > 0 && entity instanceof EntityWither)
		{
			if(rand.nextInt(Config.RocketEnchantDropRate) == 0)
			{
				stack = ItemEnchantedBook.getEnchantedItemStack(new EnchantmentData(ModEnchants.rocket, 1));
				entity.entityDropItem(stack, 0F);
			}
		}

		if(Config.ElytraEnchantDropRate > 0 && entity instanceof EntityDragon)
		{
			if(rand.nextInt(Config.ElytraEnchantDropRate) == 0)
			{
				stack = ItemEnchantedBook.getEnchantedItemStack(new EnchantmentData(ModEnchants.elytra, 1));
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
						ModNetwork.INSTANCE.sendToServer(new PacketToggle(flag, 0));
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

			if(player.isPotionActive(ModPotions.PotionRocket))
			{
				ItemStack stack = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
				if(!stack.isEmpty() && stack.getItem() instanceof ItemArmor && ((ItemArmor)stack.getItem()).armorType == EntityEquipmentSlot.FEET)
				{
					if(!stack.isItemEnchanted() || !stack.getTagCompound().hasKey("Rocket"))
					{
						player.removePotionEffect(ModPotions.PotionRocket);
					}
				}
				else if(!stack.isEmpty() && (!(stack.getItem() instanceof ItemArmor) || ((ItemArmor)stack.getItem()).armorType != EntityEquipmentSlot.FEET))
				{
					player.removePotionEffect(ModPotions.PotionRocket);
				}
				else if(stack.isEmpty())
				{
					player.removePotionEffect(ModPotions.PotionRocket);
				}
			}

			if(player.isPotionActive(ModPotions.PotionRocket))
			{
				if(player.world.isRemote)
				{
					PotionEffect effect = player.getActivePotionEffect(ModPotions.PotionRocket);
					effect.setPotionDurationMax(true);
					player.addPotionEffect(effect);
				}
			}
			else
			{
				ItemStack stack = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
				if(!stack.isEmpty() && stack.getItem() instanceof ItemArmor && ((ItemArmor)stack.getItem()).armorType == EntityEquipmentSlot.FEET)
				{
					if(stack.hasTagCompound() && stack.getTagCompound().hasKey("Rocket") && stack.getSubCompound("Rocket").getInteger("Active") == 1)
					{
						if(!player.isPotionActive(ModPotions.PotionRocket))
						{
							player.addPotionEffect(new PotionEffect(ModPotions.PotionRocket, 999999, 0));
						}
					}
				}
			}

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
						else
							stack.getOrCreateSubCompound("Rocket").setInteger("Active", 0);
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
												player.motionX += look.x * 0.1D + (look.x * Config.RocketBoost - player.motionX) * 0.5D;//(double) Config.RocketBoost;
												player.motionY += look.y * 0.1D + (look.y * Config.RocketBoost - player.motionY) * 0.5D;//(double) Config.RocketBoost;
												player.motionZ += look.z * 0.1D + (look.z * Config.RocketBoost - player.motionZ) * 0.5D;//(double) Config.RocketBoost;
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

	private static String ECOLOR = TextFormatting.DARK_PURPLE + "" + TextFormatting.ITALIC + "";
	private static String FCOLOR = TextFormatting.DARK_GREEN + "" + TextFormatting.ITALIC + "";

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onTooltipHandler(ItemTooltipEvent event)
	{
		ItemStack stack = event.getItemStack();

		// ENCHANT COLORING
		if(stack.isItemEnchanted() || (stack.getItem() == Items.ENCHANTED_BOOK && !ItemEnchantedBook.getEnchantments(stack).hasNoTags()))
		{
			int id, lvl, index;
			String ename, level, replacer;
			NBTTagList item;

			if(stack.getItem() == Items.ENCHANTED_BOOK)
				item = ItemEnchantedBook.getEnchantments(stack);
			else
				item = stack.getEnchantmentTagList();

			for(int i = 0; i < item.tagCount(); i++)
			{
				id = item.getCompoundTagAt(i).getInteger("id");
				lvl = item.getCompoundTagAt(i).getInteger("lvl");
				ename = Enchantment.getEnchantmentByID(id).getTranslatedName(lvl);

				index = event.getToolTip().indexOf(ename);
				level = I18n.translateToLocal("enchantment.level." + item.getCompoundTagAt(i).getInteger("lvl"));

				if(ForgottenAnvilHelper.enchantments.containsKey(Enchantment.getEnchantmentByID(id)) && ForgottenAnvilHelper.enchantments.get(Enchantment.getEnchantmentByID(id)) == lvl)
				{
					replacer = FCOLOR + ename;
					replacer = replacer.replaceAll(level, TextFormatting.RED + "F");
				}
				else
				{
					replacer = ECOLOR + ename;

					if(lvl >= 1 && Enchantment.getEnchantmentByID(id).getMaxLevel() > 1)
						replacer = replacer.replaceAll(level, TextFormatting.WHITE + "" + level);
				}

				if(event.getToolTip().contains(ename))
					event.getToolTip().set(index, replacer);
			}

			if(stack.getItem() instanceof ItemUltimateArmor)
				event.getToolTip().add("");
		}

		// FORGE COLOR REMOVER
		if(stack.getItem() instanceof ItemUltimateArmor)
		{
			if(stack.hasTagCompound())
			{
				if(stack.getTagCompound().hasKey("display", 10))
				{
					NBTTagCompound tag = stack.getTagCompound().getCompoundTag("display");

					if(tag.hasKey("color", 3))
					{
						String toRemove;
						int index;
						if(event.getFlags().isAdvanced())
						{
							toRemove = I18n.translateToLocalFormatted("item.color", String.format("#%06X", tag.getInteger("color")));
							index = event.getToolTip().indexOf(toRemove);

							event.getToolTip().remove(index);
						}
						else
						{
							toRemove = TextFormatting.ITALIC + I18n.translateToLocal("item.dyed");
							index = event.getToolTip().indexOf(toRemove);

							event.getToolTip().remove(index);
						}
					}
				}
			}
		}
	}

	private static String getRocketStatus(boolean flag)
	{
		String name = ECOLOR + "Rocket Power";

		if(flag)
			return name + " " + TextFormatting.RED + "OFF";

		return name + " " + TextFormatting.GREEN + "ON";
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void rocketEnchant(AnvilUpdateEvent event)
	{
		if(isFeet(event.getLeft()))
		{
			if(!hasRocketEnchant(event.getLeft()))
			{
				ItemStack out = event.getLeft().copy();

				if(isRocketEnchant(event.getRight()))
				{
					out.addEnchantment(ModEnchants.rocket, 1);
					out.getOrCreateSubCompound("Rocket").setInteger("Active", 0);
					event.setOutput(out);
					event.setMaterialCost(2);
					event.setCost(20);
				}
			}
			else if(isRocketEnchant(event.getRight()))
				event.setCanceled(true);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void elytraEnchant(AnvilUpdateEvent event)
	{
		if(isChest(event.getLeft()))
		{
			if(!hasElytraEnchant(event.getLeft()))
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
			else if(isElytraEnchant(event.getRight()) || isWingsEnchant(event.getRight()))
				event.setCanceled(true);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void wingsEnchant(AnvilUpdateEvent event)
	{
		if(isChest(event.getLeft()))
		{
			if(!hasWingsEnchant(event.getLeft()))
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
			else if(isWingsEnchant(event.getRight()) || isElytraEnchant(event.getRight()))
				event.setCanceled(true);
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

	private static boolean hasElytraEnchant(ItemStack stack)
	{
		if(!isChest(stack))
			return false;

		return EnchantmentHelper.getEnchantments(stack).containsKey(ModEnchants.elytra);
	}

	private static boolean hasWingsEnchant(ItemStack stack)
	{
		if(!isChest(stack))
			return false;

		return EnchantmentHelper.getEnchantments(stack).containsKey(ModEnchants.wings);
	}

	private static boolean hasRocketEnchant(ItemStack stack)
	{
		if(!isFeet(stack))
			return false;

		return EnchantmentHelper.getEnchantments(stack).containsKey(ModEnchants.rocket);
	}
}
