package com.iamshift.mineaddons.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ParticleUtils
{
	
	public static void spawn(EnumParticles type, World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
	{
		if(world == null || FMLCommonHandler.instance().getSide().isServer())
			return;
		
		Particle particle = null;
		switch(type)
		{
			case SACRED_CLOUD:
				particle = new ParticleSacredCloud(world, x, y, z, xSpeed, ySpeed, zSpeed);
				break;
			case CURSED_CLOUD:
				particle = new ParticleCursedCloud(world, x, y, z, xSpeed, ySpeed, zSpeed);
				break;
			case FORGOTTEN_CLOUD:
				particle = new ParticleForgottenCloud(world, x, y, z, xSpeed, ySpeed, zSpeed);
				break;
			case LAVA_SPLASH:
				particle = new ParticleLavaSplash(world, x, y, z, xSpeed, ySpeed, zSpeed);
				break;
			case WITHER_CLOUD:
				particle = new ParticleWitherCloud(world, x, y, z, xSpeed, ySpeed, zSpeed);
				break;
			case ANVIL_SPELL:
				particle = new ParticleAnvilSpell(world, x, y, z, xSpeed, ySpeed, zSpeed);
			default:
				break;
		}
		
		Minecraft.getMinecraft().effectRenderer.addEffect(particle);
	}
	
	
	public static enum EnumParticles
	{
		SACRED_CLOUD, 
		CURSED_CLOUD,
		FORGOTTEN_CLOUD,
		LAVA_SPLASH,
		WITHER_CLOUD,
		ANVIL_SPELL;
	}
}
