package com.iamshift.mineaddons.entities;

import com.google.common.base.Predicate;
import com.iamshift.mineaddons.api.IMobChanger;
import com.iamshift.mineaddons.entities.ais.EntityAIWanderAvoidLava;
import com.iamshift.mineaddons.init.ModLoot;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAIRunAroundLikeCrazy;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.DimensionType;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityZlama extends EntityLlama implements IMobChanger
{
	private static final DataParameter<Integer> DATA_STRENGTH_ID = EntityDataManager.<Integer>createKey(EntityLlama.class, DataSerializers.VARINT);
	private boolean didSpit;

	public EntityZlama(World worldIn)
	{
		super(worldIn);
		isImmuneToFire = true;
	}

	@Override
	protected void initEntityAI()
	{
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIRunAroundLikeCrazy(this, 1.2D));
		this.tasks.addTask(3, new EntityAIAttackRanged(this, 1.25D, 40, 20.0F));
		this.tasks.addTask(3, new EntityAIPanic(this, 1.2D));
		this.tasks.addTask(6, new EntityAIWanderAvoidLava(this, 0.7D));
		this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		this.tasks.addTask(7, new EntityAILookIdle(this));
		this.targetTasks.addTask(0, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
		this.targetTasks.addTask(1, new EntityZlama.AIHurtByTarget(this));
		this.targetTasks.addTask(2, new EntityZlama.AIDefendTarget(this));
	}

	@Override
	protected int getInventorySize()
	{
		return 0;
	}

	private void setStrength(int strengthIn)
	{
		this.dataManager.set(DATA_STRENGTH_ID, Integer.valueOf(Math.max(1, Math.min(5, strengthIn))));
	}

	private void setRandomStrength()
	{
		int i = this.rand.nextFloat() < 0.04F ? 5 : 3;
		this.setStrength(1 + this.rand.nextInt(i));
	}

	public int getStrength()
	{
		return ((Integer)this.dataManager.get(DATA_STRENGTH_ID)).intValue();
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		compound.setInteger("Strength", this.getStrength());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		this.setStrength(compound.getInteger("Strength"));
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(DATA_STRENGTH_ID, Integer.valueOf(0));
	}

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
	{
		livingdata = super.onInitialSpawn(difficulty, livingdata);
		this.setRandomStrength();
		return livingdata;
	}

	@Override
	public void onUpdate()
	{
		if (!this.world.isRemote && this.world.getDifficulty() == EnumDifficulty.PEACEFUL)
		{
			this.setDead();
		}

		super.onUpdate();
	}

	@Override
	public boolean hasColor()
	{
		return false;
	}

	@Override
	public int getInventoryColumns()
	{
		return 0;
	}

	@Override
	public boolean wearsArmor()
	{
		return false;
	}

	@Override
	public boolean isArmor(ItemStack stack)
	{
		return false;
	}

	@Override
	public void joinCaravan(EntityLlama caravanHeadIn) {}

	@Override
	public void leaveCaravan() {}

	@Override
	public boolean hasCaravanTrail()
	{
		return false;
	}

	@Override
	public boolean inCaravan()
	{
		return false;
	}

	private void setDidSpit(boolean didSpitIn)
	{
		this.didSpit = didSpitIn;
	}

	@Override
	protected void setOnFireFromLava() {}

	@Override
	public void setFire(int seconds) {}

	@Override
	protected void dealFireDamage(int amount) {}

	@Override
	protected float getSoundPitch()
	{
		return 0.35F;
	}

	@Override
	public boolean getCanSpawnHere()
	{
		if(this.world.getDifficulty() != EnumDifficulty.PEACEFUL && this.world.provider.getDimensionType() == DimensionType.NETHER && this.rand.nextInt(15) == 0)
			return true;

		return false;
	}

	@Override
	public EntityLlama createChild(EntityAgeable ageable)
	{
		return null;
	}

	@Override
	protected ResourceLocation getLootTable()
	{
		return ModLoot.ZLAMA;
	}

	@Override
	public void sacredWaterEffect()
	{
		EntityLlama llama = new EntityLlama(world);
		llama.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
		llama.renderYawOffset = this.renderYawOffset;
		llama.setHealth(llama.getMaxHealth());

		this.setDead();
		world.spawnEntity(llama);
	}

	static class AIDefendTarget extends EntityAINearestAttackableTarget<EntityHellhound>
	{
		public AIDefendTarget(EntityZlama llama)
		{
			super(llama, EntityHellhound.class, 16, false, true, (Predicate)null);
		}

		/**
		 * Returns whether the EntityAIBase should begin execution.
		 */
		public boolean shouldExecute()
		{
			if (super.shouldExecute() && this.targetEntity != null && !((EntityHellhound)this.targetEntity).isTamed())
			{
				return true;
			}
			else
			{
				this.taskOwner.setAttackTarget((EntityLivingBase)null);
				return false;
			}
		}

		protected double getTargetDistance()
		{
			return super.getTargetDistance() * 0.25D;
		}
	}

	static class AIHurtByTarget extends EntityAIHurtByTarget
	{
		public AIHurtByTarget(EntityZlama llama)
		{
			super(llama, false);
		}

		/**
		 * Returns whether an in-progress EntityAIBase should continue executing
		 */
		public boolean shouldContinueExecuting()
		{
			if (this.taskOwner instanceof EntityZlama)
			{
				EntityZlama entityllama = (EntityZlama)this.taskOwner;

				if (entityllama.didSpit)
				{
					entityllama.setDidSpit(false);
					return false;
				}
			}

			return super.shouldContinueExecuting();
		}
	}
}
