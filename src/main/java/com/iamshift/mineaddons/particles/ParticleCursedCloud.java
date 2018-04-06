package com.iamshift.mineaddons.particles;

import net.minecraft.client.particle.ParticleCloud;
import net.minecraft.world.World;

public class ParticleCursedCloud extends ParticleCloud
{
	public ParticleCursedCloud(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
	{
		super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        this.particleRed = .09F;
        this.particleGreen = .0F;
        this.particleBlue = 0.19F;
	}
}
