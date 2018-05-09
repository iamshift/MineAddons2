package com.iamshift.mineaddons.particles;

import net.minecraft.client.particle.ParticleEnchantmentTable;
import net.minecraft.world.World;

public class ParticleAnvilSpell extends ParticleEnchantmentTable
{

	protected ParticleAnvilSpell(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
	{
		super(world, x, y, z, xSpeed, ySpeed, zSpeed);
        this.particleRed = .05F;
        this.particleGreen = .75F;
        this.particleBlue = .05F;
	}

}
