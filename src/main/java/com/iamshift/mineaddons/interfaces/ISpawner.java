package com.iamshift.mineaddons.interfaces;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public interface ISpawner
{
	public default Entity spawnCreature(World worldIn, @Nullable ResourceLocation entityID, double x, double y, double z, boolean fixedRotation)
	{
		Entity entity = EntityList.createEntityByIDFromName(entityID, worldIn);

		if(entity instanceof EntityLiving)
		{
			EntityLiving entityliving = (EntityLiving)entity;

			if(fixedRotation)
				entity.setLocationAndAngles(x, y, z, 0.0F, 0.0F);
			else
				entity.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(worldIn.rand.nextFloat() * 360.0F), 0.0F);
			
			entityliving.rotationYawHead = entityliving.rotationYaw;
			entityliving.renderYawOffset = entityliving.rotationYaw;
			entityliving.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityliving)), (IEntityLivingData)null);
			worldIn.spawnEntity(entity);

			return entity;
		}

		return null;
	}
}
