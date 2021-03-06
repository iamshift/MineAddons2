package com.iamshift.mineaddons.entities;

import java.util.Random;

import com.iamshift.mineaddons.core.Config;
import com.iamshift.mineaddons.init.ModEntities;
import com.iamshift.mineaddons.init.ModLoot;
import com.iamshift.mineaddons.init.ModSounds;

import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityAncientCarp extends EntityFlying
{
	private static final DataParameter<Byte> ANCIENT_STATUS = EntityDataManager.<Byte>createKey(EntityAncientCarp.class, DataSerializers.BYTE);
	private float clientSideTailAnimation;
	private float clientSideTailAnimationO;
	private float clientSideTailAnimationSpeed;

	public EntityAncientCarp(World worldIn) 
	{
		super(worldIn);
		this.experienceValue = 50;
		this.setSize(3F, 2F);
		this.enablePersistence();

		this.moveHelper = new EntityAncientCarp.CarpMoveHelper(this);
		this.clientSideTailAnimation = this.rand.nextFloat();
		this.clientSideTailAnimationO = this.clientSideTailAnimation;
	}

	@Override
	protected void applyEntityAttributes() 
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
	}

	@Override
	protected void entityInit() 
	{
		super.entityInit();
		this.dataManager.register(ANCIENT_STATUS, Byte.valueOf((byte)0));
	}

	@Override
	protected void initEntityAI() 
	{
		this.tasks.addTask(1, new EntityAncientCarp.AIRandomFly(this));
		this.tasks.addTask(2, new EntityAncientCarp.AILookAround(this));
	}

	private boolean isSyncedFlagSet(int flagId)
	{
		return (((Byte)this.dataManager.get(ANCIENT_STATUS)).byteValue() & flagId) != 0;
	}

	private void setSyncedFlag(int flagId, boolean state)
	{
		byte b0 = ((Byte)this.dataManager.get(ANCIENT_STATUS)).byteValue();

		if (state)
		{
			this.dataManager.set(ANCIENT_STATUS, Byte.valueOf((byte)(b0 | flagId)));
		}
		else
		{
			this.dataManager.set(ANCIENT_STATUS, Byte.valueOf((byte)(b0 & ~flagId)));
		}
	}

	private boolean isMoving()
	{
		return this.isSyncedFlagSet(2);
	}

	private void setMoving(boolean moving)
	{
		this.setSyncedFlag(2, moving);
	}

	@Override
	public boolean getCanSpawnHere() 
	{
		if(Config.AncientCarpSpawnRate > 0)
		{
			if(this.world.provider.getDimensionType() == DimensionType.THE_END && ModEntities.HasDragonBeenKilled(this.world) && !isEntityInsideOpaqueBlock() && ModEntities.ANCIENT_LIMIT < Config.MaxAncientCarps)
			{
				if(this.rand.nextInt(Config.AncientCarpSpawnRate) == 0)
				{
					int i = MathHelper.floor(this.posX);
					int j = MathHelper.floor(this.getEntityBoundingBox().minY);
					int k = MathHelper.floor(this.posZ);
					BlockPos blockpos = new BlockPos(i, j, k);

					if(ModEntities.ANCIENT_LIMIT > Config.MaxAncientCarps)
						return false;

					return blockpos.getDistance(0, 0, 0) < 500 ? false : this.world.getBlockState(blockpos.down()).getBlock() == Blocks.END_STONE;
				}
			}
		}

		return false;
	}

	@Override
	public float getEyeHeight() 
	{
		return this.height * 0.5F;
	}

	@Override
	public int getMaxSpawnedInChunk() 
	{
		return 1;
	}

	@Override
	public boolean isEntityInvulnerable(DamageSource source) 
	{
		if(source.getTrueSource() != null)
			if(((EntityPlayer) source.getTrueSource()).getHeldItem(EnumHand.MAIN_HAND).getItem().getRegistryName().getResourceDomain().toLowerCase().contains("woot"))
				return true;

		return super.isEntityInvulnerable(source);
	}

	@Override
	protected SoundEvent getAmbientSound() 
	{
		return ModSounds.carp_ambient4;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource) 
	{
		return ModSounds.carp_hurt;
	}

	@SideOnly(Side.CLIENT)
	public float getTailAnimation(float p_175471_1_)
	{
		return this.clientSideTailAnimationO + (this.clientSideTailAnimation - this.clientSideTailAnimationO) * p_175471_1_;
	}

	@Override
	protected ResourceLocation getLootTable() 
	{
		return ModLoot.ANCIENT_CARP;
	}

	@Override
	public void onLivingUpdate() 
	{
		if(this.world.isRemote)
		{
			if(this.rand.nextInt(10) == 0)
			{
				for (int i = 0; i < 4; ++i)
					this.world.spawnParticle(EnumParticleTypes.PORTAL, 
							this.posX + (this.rand.nextDouble() - 0.5D) * ((double)this.width * 1.5D), 
							this.posY + this.rand.nextDouble() * ((double)this.height * 1.5D) - 0.25D, 
							this.posZ + (this.rand.nextDouble() - 0.5D) * ((double)this.width * 1.5D), 
							(this.rand.nextDouble() - 0.5D) * 2.0D, 
							-this.rand.nextDouble(), 
							(this.rand.nextDouble() - 0.5D) * 2.0D, 
							new int[0]);
			}

			this.clientSideTailAnimationO = this.clientSideTailAnimation;

			if(this.isMoving())
			{
				if(this.clientSideTailAnimationSpeed < 0.5F)
					this.clientSideTailAnimationSpeed = 4F;
				else
					this.clientSideTailAnimationSpeed += (.5F - this.clientSideTailAnimationSpeed) * .1F;
			}
			else
				this.clientSideTailAnimationSpeed += (0.125F - this.clientSideTailAnimationSpeed) * .2F;

			this.clientSideTailAnimation += this.clientSideTailAnimationSpeed;
		}

		super.onLivingUpdate();
	}

	@Override
	public void setDead()
	{
		super.setDead();

		ModEntities.ANCIENT_LIMIT--;
	}

	static class AILookAround extends EntityAIBase
	{
		private final EntityAncientCarp parentEntity;

		public AILookAround(EntityAncientCarp carp) 
		{
			this.parentEntity = carp;
			this.setMutexBits(2);
		}

		@Override
		public boolean shouldExecute() 
		{
			return true;
		}

		@Override
		public void updateTask()
		{
			if (this.parentEntity.getAttackTarget() == null)
			{
				this.parentEntity.rotationYaw = -((float)MathHelper.atan2(this.parentEntity.motionX, this.parentEntity.motionZ)) * (180F / (float)Math.PI);
				this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw;
			}
			else
			{
				EntityLivingBase entitylivingbase = this.parentEntity.getAttackTarget();
				double d0 = 64.0D;

				if (entitylivingbase.getDistanceSq(this.parentEntity) < 4096.0D)
				{
					double d1 = entitylivingbase.posX - this.parentEntity.posX;
					double d2 = entitylivingbase.posZ - this.parentEntity.posZ;
					this.parentEntity.rotationYaw = -((float)MathHelper.atan2(d1, d2)) * (180F / (float)Math.PI);
					this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw;
				}
			}
		}
	}

	static class AIRandomFly extends EntityAIBase
	{
		private final EntityAncientCarp parentEntity;

		public AIRandomFly(EntityAncientCarp carp)
		{
			this.parentEntity = carp;
			this.setMutexBits(1);
		}

		@Override
		public boolean shouldExecute()
		{
			EntityMoveHelper entitymovehelper = this.parentEntity.getMoveHelper();

			if (!entitymovehelper.isUpdating())
			{
				return true;
			}
			else
			{
				double d0 = entitymovehelper.getX() - this.parentEntity.posX;
				double d1 = entitymovehelper.getY() - this.parentEntity.posY;
				double d2 = entitymovehelper.getZ() - this.parentEntity.posZ;
				double d3 = d0 * d0 + d1 * d1 + d2 * d2;
				return d3 < 1.0D || d3 > 3600.0D;
			}
		}

		@Override
		public boolean shouldContinueExecuting()
		{
			return false;
		}

		@Override
		public void startExecuting()
		{
			Random random = this.parentEntity.getRNG();
			double d0 = this.parentEntity.posX + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
			double d1 = this.parentEntity.posY + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
			double d2 = this.parentEntity.posZ + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
			this.parentEntity.getMoveHelper().setMoveTo(d0, d1, d2, 2.0D);
		}
	}

	static class CarpMoveHelper extends EntityMoveHelper
	{
		private final EntityAncientCarp parentEntity;
		private int courseChangeCooldown;

		public CarpMoveHelper(EntityAncientCarp carp)
		{
			super(carp);
			this.parentEntity = carp;
		}

		@Override
		public void onUpdateMoveHelper()
		{
			if (this.action == EntityMoveHelper.Action.MOVE_TO)
			{
				double d0 = this.posX - this.parentEntity.posX;
				double d1 = this.posY - this.parentEntity.posY;
				double d2 = this.posZ - this.parentEntity.posZ;
				double d3 = d0 * d0 + d1 * d1 + d2 * d2;

				if (this.courseChangeCooldown-- <= 0)
				{
					this.courseChangeCooldown += this.parentEntity.getRNG().nextInt(5) + 2;
					d3 = (double)MathHelper.sqrt(d3);

					if (this.isNotColliding(this.posX, this.posY, this.posZ, d3))
					{
						this.parentEntity.motionX += d0 / d3 * 0.1D;
						this.parentEntity.motionY += d1 / d3 * 0.1D;
						this.parentEntity.motionZ += d2 / d3 * 0.1D;
						this.parentEntity.setMoving(true);
					}
					else
					{
						this.action = EntityMoveHelper.Action.WAIT;
						this.parentEntity.setMoving(false);
					}
				}
			}
		}

		private boolean isNotColliding(double x, double y, double z, double p_179926_7_)
		{
			double d0 = (x - this.parentEntity.posX) / p_179926_7_;
			double d1 = (y - this.parentEntity.posY) / p_179926_7_;
			double d2 = (z - this.parentEntity.posZ) / p_179926_7_;
			AxisAlignedBB axisalignedbb = this.parentEntity.getEntityBoundingBox();

			for (int i = 1; (double)i < p_179926_7_; ++i)
			{
				axisalignedbb = axisalignedbb.offset(d0, d1, d2);

				if (!this.parentEntity.world.getCollisionBoxes(this.parentEntity, axisalignedbb).isEmpty())
				{
					return false;
				}
			}

			return true;
		}
	}
}
