package com.iamshift.mineaddons.particles;

import net.minecraft.client.particle.Particle;

import net.minecraft.client.particle.ParticleSplash;
import net.minecraft.world.World;

public class ParticleLavaSplash extends ParticleSplash
{
	public ParticleLavaSplash(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
	{
		super(world, x, y, z, xSpeed, ySpeed, zSpeed);
		this.particleRed = 1.0F;
        this.particleGreen = 0.25F;
        this.particleBlue = 0F;
	}
}
