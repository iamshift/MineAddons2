package com.iamshift.mineaddons.particles;

import net.minecraft.client.particle.ParticleCloud;
import net.minecraft.world.World;

public class ParticleWitherCloud extends ParticleCloud
{
	public ParticleWitherCloud(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
	{
		super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        this.particleRed = .05F;
        this.particleGreen = .05F;
        this.particleBlue = .05F;
	}

}
