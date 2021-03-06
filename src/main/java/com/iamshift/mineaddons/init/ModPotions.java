package com.iamshift.mineaddons.init;

import java.util.ArrayList;

import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.potions.PotionDoubleHealth;
import com.iamshift.mineaddons.potions.PotionFlight;
import com.iamshift.mineaddons.potions.PotionMobChanger;
import com.iamshift.mineaddons.potions.PotionRocket;
import com.iamshift.mineaddons.potions.PotionWitherProof;

import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class ModPotions
{
	public static ResourceLocation icon = new ResourceLocation(Refs.ID, "textures/potions/potions.png");
	
	public static ArrayList<Potion> potions = new ArrayList<Potion>();
	
	public static Potion PotionMobChanger;
	
	public static Potion PotionDoubleHealth;
	public static Potion PotionWitherProof;
	public static Potion PotionFlight;
	public static Potion PotionRocket;
	
	public static void init()
	{
		PotionMobChanger = new PotionMobChanger("mob_changer");
	
		PotionDoubleHealth = new PotionDoubleHealth("double_health");
		PotionWitherProof = new PotionWitherProof("wither_proof");
		PotionFlight = new PotionFlight("flight");
		PotionRocket = new PotionRocket("rocket");
	}
}
