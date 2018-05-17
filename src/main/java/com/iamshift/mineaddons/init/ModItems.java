package com.iamshift.mineaddons.init;

import java.util.ArrayList;
import java.util.List;

import com.iamshift.mineaddons.core.Config;
import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.events.ArmorEvents;
import com.iamshift.mineaddons.items.ItemAncientEssence;
import com.iamshift.mineaddons.items.ItemBase;
import com.iamshift.mineaddons.items.ItemBlazelier;
import com.iamshift.mineaddons.items.ItemBrainlessShulkerEgg;
import com.iamshift.mineaddons.items.ItemCellulose;
import com.iamshift.mineaddons.items.ItemDeadHorseEgg;
import com.iamshift.mineaddons.items.ItemEnchanted;
import com.iamshift.mineaddons.items.ItemFiberglass;
import com.iamshift.mineaddons.items.ItemLapis;
import com.iamshift.mineaddons.items.ItemO2Bottle;
import com.iamshift.mineaddons.items.ItemRainbowBottle;
import com.iamshift.mineaddons.items.ItemRespirator;
import com.iamshift.mineaddons.items.ItemRib;
import com.iamshift.mineaddons.items.ItemSoul;
import com.iamshift.mineaddons.items.ItemSuperNameTag;
import com.iamshift.mineaddons.items.ItemSushi;
import com.iamshift.mineaddons.items.ItemTimeSkipClock;
import com.iamshift.mineaddons.items.ItemWings;
import com.iamshift.mineaddons.items.ItemWitherDust;
import com.iamshift.mineaddons.items.armors.ItemFiberglassArmor;
import com.iamshift.mineaddons.items.armors.ItemUltimateArmor;
import com.iamshift.mineaddons.items.tools.ItemBreaker;
import com.iamshift.mineaddons.items.tools.ItemFiberAxe;
import com.iamshift.mineaddons.items.tools.ItemFiberPickaxe;
import com.iamshift.mineaddons.items.tools.ItemFiberShovel;

import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Loader;

public class ModItems 
{
	public static final List<Item> ITEMS = new ArrayList<Item>();

	public static Item AncientEssence;
	public static Item Cellulose;
	public static Item BrainlessShulkerEgg;
	public static Item Sushi;
	public static Item SuperNameTag;
	public static Item RainbowBottle;
	public static Item WitherDust;
	public static Item TimeSkipClock;

	public static Item Rib;
	public static Item Soul;
	public static Item Breaker;
	public static Item EnchantedItems;
	public static Item Lapis;
	public static Item GlassPile;
	public static Item Fiberglass;
	public static Item FiberglassIngot;
	public static Item FiberHelmet;
	public static Item FiberChestplate;
	public static Item FiberLeggings;
	public static Item FiberBoots;
	public static Item UltimateHelmet;
	public static Item UltimateChestplate;
	public static Item UltimateLeggings;
	public static Item UltimateBoots;

	public static Item Voidball;
	public static Item Blazelier;
	public static Item DarkCore;
	public static Item Wings;

	public static Item Respirator;
	public static Item O2Bottle;

	public static Item DeadHorseEgg;

	public static Item FiberPickaxe;
	public static Item FiberAxe;
	public static Item FiberShovel;

	public static final ArmorMaterial ARMOR_FIBERGLASS = EnumHelper.addArmorMaterial("armor_fiberglass", Refs.ID + ":fiberglass", 40, new int[]{4, 7, 9, 4}, 25, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 4.0F);
	public static final ArmorMaterial ARMOR_ULTIMATE = EnumHelper.addArmorMaterial("armor_ultimate", Refs.ID + ":ultimate", 80, new int[]{6, 9, 11, 6}, 50, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 8.0F);

	public static void init()
	{
		SuperNameTag = new ItemSuperNameTag("super_name_tag");

		TimeSkipClock = new ItemTimeSkipClock("time_skip_clock");

		Cellulose = new ItemCellulose("cellulose");

		GlassPile = new ItemBase("glass_pile");
		Fiberglass = new ItemFiberglass("fiberglass");
		FiberglassIngot = new ItemFiberglass("fiberglass_ingot");

		ArmorEvents.armorEffects.put(EntityEquipmentSlot.HEAD, new PotionEffect(MobEffects.NIGHT_VISION, 999999, 0, false, false));
		ArmorEvents.armorEffects.put(EntityEquipmentSlot.CHEST, new PotionEffect(MobEffects.HASTE, 999999, 0, false, false));
		ArmorEvents.armorEffects.put(EntityEquipmentSlot.LEGS, new PotionEffect(MobEffects.SPEED, 999999, 0, false, false));
		ArmorEvents.armorEffects.put(EntityEquipmentSlot.FEET, new PotionEffect(MobEffects.JUMP_BOOST, 999999, 0, false, false));

		if(Config.FiberTools) 
		{
			FiberPickaxe = new ItemFiberPickaxe("fiber_pickaxe");
			FiberAxe = new ItemFiberAxe("fiber_axe");
			FiberShovel = new ItemFiberShovel("fiber_shovel");
		}

		if(Config.FiberArmor)
		{
			FiberHelmet = new ItemFiberglassArmor("fiberglass_helmet", ARMOR_FIBERGLASS, 1, EntityEquipmentSlot.HEAD);
			FiberChestplate = new ItemFiberglassArmor("fiberglass_chestplate", ARMOR_FIBERGLASS, 1, EntityEquipmentSlot.CHEST);
			FiberLeggings = new ItemFiberglassArmor("fiberglass_leggings", ARMOR_FIBERGLASS, 1, EntityEquipmentSlot.LEGS);
			FiberBoots = new ItemFiberglassArmor("fiberglass_boots", ARMOR_FIBERGLASS, 1, EntityEquipmentSlot.FEET);
		}

		if(Config.UltiArmor)
		{
			UltimateHelmet = new ItemUltimateArmor("ultimate_helmet", ARMOR_ULTIMATE, 1, EntityEquipmentSlot.HEAD);
			UltimateChestplate = new ItemUltimateArmor("ultimate_chestplate", ARMOR_ULTIMATE, 1, EntityEquipmentSlot.CHEST);
			UltimateLeggings = new ItemUltimateArmor("ultimate_leggings", ARMOR_ULTIMATE, 1, EntityEquipmentSlot.LEGS);
			UltimateBoots = new ItemUltimateArmor("ultimate_boots", ARMOR_ULTIMATE, 1, EntityEquipmentSlot.FEET);
		}


		AncientEssence = new ItemAncientEssence("ancient_essence");
		BrainlessShulkerEgg = new ItemBrainlessShulkerEgg("brainless_shulker_egg");
		Sushi = new ItemSushi("sushi");


		Rib = new ItemRib("rib");
		Soul = new ItemSoul("soul");
		Voidball = new ItemBase("voidball");
		Blazelier = new ItemBlazelier("blazelier");
		DarkCore = new ItemBase("dark_core");
		Wings = new ItemWings("wings");


		Respirator = new ItemRespirator("respirator");
		O2Bottle = new ItemO2Bottle("o2bottle");

		RainbowBottle = new ItemRainbowBottle("rainbow_bottle");
		WitherDust = new ItemWitherDust("wither_dust");
		Breaker = new ItemBreaker("breaker");
		EnchantedItems = new ItemEnchanted("enchanted");
		Lapis = new ItemLapis("lapis");
		DeadHorseEgg = new ItemDeadHorseEgg("dead_horse_egg");
	}
}
