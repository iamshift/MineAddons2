package com.iamshift.mineaddons.particles;

import net.minecraft.client.particle.ParticleCloud;
import net.minecraft.world.World;

public class ParticleSacredCloud extends ParticleCloud
{
	public ParticleSacredCloud(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
	{
		super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        this.particleRed = .0F;
        this.particleGreen = .90F;
        this.particleBlue = 1.0F;
	}
}
