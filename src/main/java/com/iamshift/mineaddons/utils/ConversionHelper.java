package com.iamshift.mineaddons.utils;

import java.util.ArrayList;

import com.iamshift.mineaddons.MineAddons;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ConversionHelper
{
	public static class Conversion
	{
		private final ResourceLocation input;
		private final ResourceLocation output;
		
		public Conversion(String input, String output)
		{
			this.input = new ResourceLocation(input);
			this.output = new ResourceLocation(output);
		}
		
		public ResourceLocation getInput()
		{
			return this.input;
		}
		
		public ResourceLocation getOutput()
		{
			return this.output;
		}
		
		@Override
		public boolean equals(Object obj)
		{
			if(obj instanceof Conversion)
			{
				Conversion c = (Conversion) obj;
				
				if(c.getInput().equals(this.input))
					return true;
			}
			
			return false;
		}
	}
	
	public static ArrayList<Conversion> sacredList = new ArrayList<>();
	public static ArrayList<Conversion> cursedList = new ArrayList<>();
	public static ArrayList<Conversion> forgottenList = new ArrayList<>();
	
	public static void Initialize()
	{
		cursedList.add(new Conversion("minecraft:skeleton", 	"minecraft:wither_skeleton"));
		cursedList.add(new Conversion("minecraft:spider", 		"minecraft:cave_spider"));
		cursedList.add(new Conversion("minecraft:squid", 		"minecraft:ghast"));
		cursedList.add(new Conversion("minecraft:silverfish", 	"minecraft:endermite"));
		cursedList.add(new Conversion("minecraft:villager", 	"minecraft:witch"));
		cursedList.add(new Conversion("minecraft:guardian", 	"minecraft:elder_guardian"));
		cursedList.add(new Conversion("minecraft:bat", 			"minecraft:blaze"));
		cursedList.add(new Conversion("minecraft:horse", 		"minecraft:zombie_horse"));
		cursedList.add(new Conversion("minecraft:zombie", 		"minecraft:zombie_pigman"));
		cursedList.add(new Conversion("minecraft:llama", 		"mineaddons:zlama"));
		cursedList.add(new Conversion("minecraft:sheep", 		"mineaddons:isheep"));
		cursedList.add(new Conversion("minecraft:wolf", 		"mineaddons:hellhound"));
		cursedList.add(new Conversion("minecraft:slime", 		"minecraft:magma_cube"));
		
		sacredList.add(new Conversion("minecraft:wither_skeleton", 	"minecraft:skeleton"));
		sacredList.add(new Conversion("minecraft:cave_spider", 		"minecraft:spider"));
		sacredList.add(new Conversion("minecraft:ghast", 			"minecraft:squid"));
		sacredList.add(new Conversion("minecraft:endermite", 		"minecraft:silverfish"));
		sacredList.add(new Conversion("minecraft:witch", 			"minecraft:villager"));
		sacredList.add(new Conversion("minecraft:elder_guardian", 	"minecraft:guardian"));
		sacredList.add(new Conversion("minecraft:blaze", 			"minecraft:bat"));
		sacredList.add(new Conversion("minecraft:zombie_horse", 	"minecraft:horse"));
		sacredList.add(new Conversion("minecraft:zombie_pigman", 	"minecraft:zombie"));
		sacredList.add(new Conversion("mineaddons:zlama", 			"minecraft:llama"));
		sacredList.add(new Conversion("mineaddons:isheep", 			"minecraft:sheep"));
		sacredList.add(new Conversion("mineaddons:hellhound", 		"minecraft:wolf"));
		sacredList.add(new Conversion("mineaddons:true_creeper", 	"mineaddons:peace_creeper"));
		sacredList.add(new Conversion("minecraft:magma_cube", 		"minecraft:slime"));
		
		forgottenList.add(new Conversion("minecraft:creeper", "mineaddons:void_creeper"));
	}
}
