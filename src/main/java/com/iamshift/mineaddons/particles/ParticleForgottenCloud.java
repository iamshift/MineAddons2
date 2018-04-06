package com.iamshift.mineaddons.particles;

import net.minecraft.client.particle.ParticleCloud;
import net.minecraft.world.World;

public class ParticleForgottenCloud extends ParticleCloud
{
	public ParticleForgottenCloud(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
	{
		super(world, x, y, z, 0.0D, 0.0D, 0.0D);
		this.particleRed = .0F;
		this.particleGreen = .4F;
		this.particleBlue = .0F;
	}
}
