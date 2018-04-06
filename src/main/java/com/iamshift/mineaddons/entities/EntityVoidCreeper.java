package com.iamshift.mineaddons.entities;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.iamshift.mineaddons.utils.CustomExplosion;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAICreeperSwell;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DimensionType;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EntityVoidCreeper extends EntityCreeper
{
	private int lastActiveTime;
	private int timeSinceIgnited;
	private int fuseTime = 15;
	private int explosionSize = 3;
	private int explosionStrenght = 1;

	public EntityVoidCreeper(World worldIn) 
	{
		super(worldIn);
	}

	@Override
	protected void initEntityAI() 
	{
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAICreeperSwell(this));
		this.tasks.addTask(3, new EntityAIAvoidEntity(this, EntityOcelot.class, 6.0F, 1.0D, 1.2D));
		this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0D, false));
		this.tasks.addTask(5, new EntityAIWander(this, 0.8D));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 16.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityVoidCreeper.AIFindPlayer(this));
		this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false, new Class[0]));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 10, true, false, new Predicate<EntityPlayer>()
		{
			public boolean apply(@Nullable EntityPlayer p_apply_1_)
			{
				return !p_apply_1_.capabilities.isCreativeMode;
			}
		}));
	}

	@Override
	protected void applyEntityAttributes() 
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16D);
	}

	@Override
	public void onStruckByLightning(EntityLightningBolt lightningBolt) 
	{
		return;
	}

	@Override
	public boolean getCanSpawnHere() 
	{
		if(this.world.provider.getDimensionType() == DimensionType.THE_END && this.rand.nextInt(10) == 0)
		{
			int i = MathHelper.floor(this.posX);
			int j = MathHelper.floor(this.getEntityBoundingBox().minY);
			int k = MathHelper.floor(this.posZ);
			BlockPos blockpos = new BlockPos(i, j, k);

			return blockpos.getDistance(0, 0, 0) < 500 ? false : this.world.getBlockState(blockpos.down()).getBlock() == Blocks.END_STONE;
		}

		return false;
	}

	@Override
	public void onUpdate() 
	{
		if (this.isEntityAlive())
		{
			this.lastActiveTime = this.timeSinceIgnited;

			if (this.hasIgnited())
			{
				this.setCreeperState(1);
			}

			int i = this.getCreeperState();

			if (i > 0 && this.timeSinceIgnited == 0)
			{
				this.playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0F, 0.5F);
			}

			this.timeSinceIgnited += i;

			if (this.timeSinceIgnited < 0)
			{
				this.timeSinceIgnited = 0;
			}

			if (this.timeSinceIgnited >= this.fuseTime)
			{
				this.timeSinceIgnited = this.fuseTime;
				this.explode();
			}
		}

		super.onUpdate();
	}

	@Override
	public void onLivingUpdate() 
	{
		super.onLivingUpdate();

		if (this.world.isRemote)
		{
			for (int i = 0; i < 2; ++i)
			{
				this.world.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height - 0.25D, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D, new int[0]);
			}
		}
	}

	private void explode()
	{
		if (!this.world.isRemote)
		{
			boolean flag = this.world.getGameRules().getBoolean("mobGriefing");
			this.dead = true;
			this.newExplosion(this, this.posX, this.posY, this.posZ, (float)this.explosionSize, (float)this.explosionStrenght, false, flag);
			this.setDead();

			if(this.rand.nextInt(5) == 0)
			{
				EntityEndermite endermite = new EntityEndermite(this.world);
				endermite.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
				endermite.renderYawOffset = this.renderYawOffset;
				endermite.setHealth(endermite.getMaxHealth());

				this.world.spawnEntity(endermite);
			}
		}
	}

	private Explosion newExplosion(@Nullable Entity entityIn, double x, double y, double z, float size, float strength, boolean isFlaming, boolean isSmoking)
	{
		CustomExplosion explosion = new CustomExplosion(this.world, entityIn, x, y, z, size, strength, isFlaming, isSmoking);
		if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.world, explosion)) return explosion;
		explosion.doExplosionA();
		explosion.doExplosionB(true);
		return explosion;
	}

	private boolean shouldAttackPlayer(EntityPlayer player)
	{
		return player.canEntityBeSeen(this);
	}

	protected boolean teleportToEntity(Entity target)
	{
		double d1 = this.rand.nextBoolean() ? target.posX + 0.5D : target.posX - 0.5D;
		double d2 = target.posY + 0.5D;
		double d3 = this.rand.nextBoolean() ? target.posZ + 0.5D : target.posZ - 0.5D;
		return this.teleportTo(d1, d2, d3);
	}

	private boolean teleportTo(double x, double y, double z)
	{
		net.minecraftforge.event.entity.living.EnderTeleportEvent event = new net.minecraftforge.event.entity.living.EnderTeleportEvent(this, x, y, z, 0);
		if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) return false;
		boolean flag = this.attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ());

		if (flag)
		{
			this.world.playSound((EntityPlayer)null, this.prevPosX, this.prevPosY, this.prevPosZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
			this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
		}

		return flag;
	}

	static class AIFindPlayer extends EntityAINearestAttackableTarget<EntityPlayer>
	{
		private final EntityVoidCreeper voidcreeper;
		private EntityPlayer player;
		private int aggroTime;
		private int teleportTime;

		public AIFindPlayer(EntityVoidCreeper creeper)
		{
			super(creeper, EntityPlayer.class, false);
			this.voidcreeper = creeper;
		}

		@Override
		public boolean shouldExecute()
		{
			double d0 = this.getTargetDistance();
			this.player = this.voidcreeper.world.getNearestAttackablePlayer(this.voidcreeper.posX, this.voidcreeper.posY, this.voidcreeper.posZ, d0, d0, (Function<EntityPlayer, Double>)null, new Predicate<EntityPlayer>()
			{
				public boolean apply(@Nullable EntityPlayer p_apply_1_)
				{
					return p_apply_1_ != null && AIFindPlayer.this.voidcreeper.shouldAttackPlayer(p_apply_1_);
				}
			});
			return this.player != null;
		}

		@Override
		public void startExecuting()
		{
			this.aggroTime = 5;
			this.teleportTime = 0;
		}

		@Override
		public void resetTask()
		{
			this.player = null;
			super.resetTask();
		}

		@Override
		public boolean shouldContinueExecuting()
		{
			if (this.player != null)
			{
				if (!this.voidcreeper.shouldAttackPlayer(this.player))
				{
					return false;
				}
				else
				{
					this.voidcreeper.faceEntity(this.player, 10.0F, 10.0F);
					return true;
				}
			}
			else
			{
				return this.targetEntity != null && ((EntityPlayer)this.targetEntity).isEntityAlive() ? true : super.shouldContinueExecuting();
			}
		}

		@Override
		public void updateTask()
		{
			if (this.player != null)
			{
				if (--this.aggroTime <= 0)
				{
					this.targetEntity = this.player;
					this.player = null;
					super.startExecuting();
				}
			}
			else
			{
				if (this.targetEntity != null)
				{
					if (this.teleportTime++ >= 15 && this.voidcreeper.teleportToEntity(this.targetEntity))
					{
						this.teleportTime = 0;
					}
				}

				super.updateTask();
			}
		}
	}
}
