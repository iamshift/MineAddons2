package com.iamshift.mineaddons.init;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.iamshift.mineaddons.MineAddons;
import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.entities.EntityAncientCarp;
import com.iamshift.mineaddons.entities.EntityBlazelier;
import com.iamshift.mineaddons.entities.EntityBrainlessShulker;
import com.iamshift.mineaddons.entities.EntityEnderCarp;
import com.iamshift.mineaddons.entities.EntityHellhound;
import com.iamshift.mineaddons.entities.EntityPeaceCreeper;
import com.iamshift.mineaddons.entities.EntityTrueCreeper;
import com.iamshift.mineaddons.entities.EntityVoidCreeper;
import com.iamshift.mineaddons.entities.EntityZlama;
import com.iamshift.mineaddons.entities.EntityiSheep;
import com.iamshift.mineaddons.entities.boss.EntityDeadHorse;
import com.iamshift.mineaddons.entities.boss.EntityGhostRider;
import com.iamshift.mineaddons.entities.boss.EntityVoix;
import com.iamshift.mineaddons.entities.boss.EntityWitherBlaze;
import com.iamshift.mineaddons.entities.items.EntitySpiritBomb;
import com.iamshift.mineaddons.entities.items.EntityVoidball;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ModEntities
{
	private static int ENTITY_ID = 0;

	public static int ANCIENT_LIMIT = 0;

	public static List<Biome> allBiomes = new ArrayList<Biome>();
	public static List<Biome> overworldBiomes = new ArrayList<Biome>();;
	public static List<Biome> netherBiomes = new ArrayList<Biome>();;
	public static List<Biome> endBiomes = new ArrayList<Biome>();;

	public static void init()
	{
		Setup();

		EntityRegistry.registerModEntity(new ResourceLocation(Refs.ID, "hellhound"), EntityHellhound.class, "hellhound", ++ENTITY_ID, MineAddons.instance, 128, 2, true, new Color(50, 50, 50).getRGB(), new Color(150, 0, 0).getRGB());
		EntityRegistry.addSpawn(EntityHellhound.class, 12, 2, 4, EnumCreatureType.MONSTER, netherBiomes.toArray(new Biome[0]));

		EntityRegistry.registerModEntity(new ResourceLocation(Refs.ID, "isheep"), EntityiSheep.class, "isheep", ++ENTITY_ID, MineAddons.instance, 128, 2, true, new Color(50, 0, 50).getRGB(), new Color(150, 0, 25).getRGB());
		EntityRegistry.addSpawn(EntityiSheep.class, 12, 3, 4, EnumCreatureType.MONSTER, netherBiomes.toArray(new Biome[0]));

		EntityRegistry.registerModEntity(new ResourceLocation(Refs.ID, "zlama"), EntityZlama.class, "zlama", ++ENTITY_ID, MineAddons.instance, 128, 2, true, new Color(50, 50, 0).getRGB(), new Color(150, 25, 0).getRGB());
		EntityRegistry.addSpawn(EntityZlama.class, 12, 2, 4, EnumCreatureType.MONSTER, netherBiomes.toArray(new Biome[0]));

		EntityRegistry.registerModEntity(new ResourceLocation(Refs.ID, "wither_blaze"), EntityWitherBlaze.class, "wither_blaze", ++ENTITY_ID, MineAddons.instance, 128, 2, true);

		EntityRegistry.registerModEntity(new ResourceLocation(Refs.ID, "voidball"), EntityVoidball.class, "voidball", ++ENTITY_ID, MineAddons.instance, 128, 2, true);

		EntityRegistry.registerModEntity(new ResourceLocation(Refs.ID, "blazelier"), EntityBlazelier.class, "blazelier", ++ENTITY_ID, MineAddons.instance, 128, 2, true);


		EntityRegistry.registerModEntity(new ResourceLocation(Refs.ID, "ender_carp"), EntityEnderCarp.class, "ender_carp", ++ENTITY_ID, MineAddons.instance, 128, 2, true, new Color(150, 150, 150).getRGB(), new Color(255, 192, 0).getRGB());
		EntityRegistry.addSpawn(EntityEnderCarp.class, 8, 2, 4, EnumCreatureType.MONSTER, endBiomes.toArray(new Biome[0]));

		EntityRegistry.registerModEntity(new ResourceLocation(Refs.ID, "ancient_carp"), EntityAncientCarp.class, "ancient_carp", ++ENTITY_ID, MineAddons.instance, 128, 2, true, new Color(150, 150, 150).getRGB(), new Color(255, 255, 255).getRGB());
		EntityRegistry.addSpawn(EntityAncientCarp.class, 3, 1, 1, EnumCreatureType.MONSTER, endBiomes.toArray(new Biome[0]));

		EntityRegistry.registerModEntity(new ResourceLocation(Refs.ID, "void_creeper"), EntityVoidCreeper.class, "void_creeper", ++ENTITY_ID, MineAddons.instance, 128, 2, true, new Color(25, 25, 25).getRGB(), new Color(125, 0, 125).getRGB());
		EntityRegistry.addSpawn(EntityVoidCreeper.class, 8, 1, 2, EnumCreatureType.MONSTER, endBiomes.toArray(new Biome[0]));

		EntityRegistry.registerModEntity(new ResourceLocation(Refs.ID, "brainless_shulker"), EntityBrainlessShulker.class, "brainless_shulker", ++ENTITY_ID, MineAddons.instance, 128, 2, true);

		EntityRegistry.registerModEntity(new ResourceLocation(Refs.ID, "voix"), EntityVoix.class, "voix", ++ENTITY_ID, MineAddons.instance, 128, 2, true);
		
		EntityRegistry.registerModEntity(new ResourceLocation(Refs.ID, "voidball"), EntitySpiritBomb.class, "spiritbomb", ++ENTITY_ID, MineAddons.instance, 128, 2, true);
		

		EntityRegistry.registerModEntity(new ResourceLocation(Refs.ID, "true_creeper"), EntityTrueCreeper.class, "true_creeper", ++ENTITY_ID, MineAddons.instance, 128, 2, true, new Color(50, 50, 50).getRGB(), new Color(255, 192, 0).getRGB());
		EntityRegistry.addSpawn(EntityTrueCreeper.class, 8, 1, 3, EnumCreatureType.MONSTER, overworldBiomes.toArray(new Biome[0]));

		EntityRegistry.registerModEntity(new ResourceLocation(Refs.ID, "peace_creeper"), EntityPeaceCreeper.class, "peace_creeper", ++ENTITY_ID, MineAddons.instance, 128, 2, true, new Color(255, 0, 125).getRGB(), new Color(255, 0, 75).getRGB());

		EntityRegistry.registerModEntity(new ResourceLocation(Refs.ID, "dead_horse"), EntityDeadHorse.class, "dead_horse", ++ENTITY_ID, MineAddons.instance, 128, 2, true);

		EntityRegistry.registerModEntity(new ResourceLocation(Refs.ID, "ghost_rider"), EntityGhostRider.class, "ghost_rider", ++ENTITY_ID, MineAddons.instance, 128, 2, true);
	}

	public static void Setup()
	{
		Type[] biomeType = new Type[] {Type.BEACH, Type.COLD, Type.CONIFEROUS, Type.DEAD, Type.DENSE, Type.DRY, Type.END, Type.FOREST, Type.HILLS, Type.HOT,
				Type.JUNGLE, Type.LUSH, Type.MAGICAL, Type.MESA, Type.MOUNTAIN, Type.MUSHROOM, Type.NETHER, Type.OCEAN, Type.PLAINS, Type.RARE, Type.RIVER, Type.SANDY,
				Type.SAVANNA, Type.SNOWY, Type.SWAMP, Type.SPARSE, Type.SPOOKY, Type.VOID, Type.WASTELAND, Type.WATER, Type.WET};

		for (Type type : biomeType)
		{
			for (Biome biome : BiomeDictionary.getBiomes(type))
			{
				if (!allBiomes.contains(biome))
					allBiomes.add(biome);
			}
		}

		overworldBiomes.addAll(allBiomes);
		overworldBiomes.removeAll(BiomeDictionary.getBiomes(Type.NETHER));
		overworldBiomes.removeAll(BiomeDictionary.getBiomes(Type.END));
		netherBiomes.addAll(BiomeDictionary.getBiomes(Type.NETHER));
		endBiomes.addAll(BiomeDictionary.getBiomes(Type.END));
	}
}
