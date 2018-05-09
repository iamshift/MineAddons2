package com.iamshift.mineaddons.entities;

import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.iamshift.mineaddons.api.IMobChanger;
import com.iamshift.mineaddons.core.Config;
import com.iamshift.mineaddons.entities.ais.EntityAITargetTamed;
import com.iamshift.mineaddons.entities.ais.EntityAIWanderAvoidLava;
import com.iamshift.mineaddons.init.ModLoot;
import com.iamshift.mineaddons.items.ItemRib;
import com.iamshift.mineaddons.particles.ParticleUtils;
import com.iamshift.mineaddons.particles.ParticleUtils.EnumParticles;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITargetNonTamed;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DimensionType;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityHellhound extends EntityTameable implements IMobChanger
{
	private static final DataParameter<Float> DATA_HEALTH_ID = EntityDataManager.<Float>createKey(EntityWolf.class, DataSerializers.FLOAT);

	private float headRotationCourse;
	private float headRotationCourseOld;

	private boolean isWet;
	private boolean isShaking;
	private float timeWolfIsShaking;
	private float prevTimeWolfIsShaking;

	private boolean hidding;

	public EntityHellhound(World worldIn)
	{
		super(worldIn);
		this.setSize(0.9F, 1.275F);
		this.setTamed(false);
		this.isImmuneToFire = true;
		this.hidding = false;
	}

	@Override
	protected void initEntityAI()
	{
		this.aiSit = new EntityAISit(this);
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, this.aiSit);
		this.tasks.addTask(3, new EntityHellhound.AIAvoidEntity(this, EntityZlama.class, 24.0F, 1.5D, 1.5D));
		this.tasks.addTask(4, new EntityAILeapAtTarget(this, 0.4F));
		this.tasks.addTask(5, new EntityAIAttackMelee(this, 1.0D, true));
		this.tasks.addTask(6, new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
		this.tasks.addTask(7, new EntityAIMate(this, 1.0D));
		this.tasks.addTask(8, new EntityAIWanderAvoidLava(this, 1.0D));
		this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(9, new EntityAILookIdle(this));
		this.targetTasks.addTask(0, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
		this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
		this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
		this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true, new Class[0]));
		this.targetTasks.addTask(4, new EntityAITargetNonTamed(this, EntityAnimal.class, false, new Predicate<Entity>()
		{
			public boolean apply(@Nullable Entity p_apply_1_)
			{
				return p_apply_1_ instanceof EntityiSheep;
			}
		}));
		this.targetTasks.addTask(5, new EntityAITargetNonTamed(this, EntityAnimal.class, false, new Predicate<Entity>()
		{
			public boolean apply(@Nullable Entity p_apply_1_)
			{
				return p_apply_1_ instanceof EntityChicken;
			}
		}));
		this.targetTasks.addTask(6, new EntityAITargetTamed(this, EntityMob.class, false, new Predicate<Entity>()
		{
			public boolean apply(@Nullable Entity p_apply_1_)
			{
				return p_apply_1_ instanceof EntityWither;
			}
		}));
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);

		if (this.isTamed())
		{
			this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
		}
		else
		{
			this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
		}

		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
	}

	@Override
	public void setAttackTarget(EntityLivingBase entitylivingbaseIn)
	{
		super.setAttackTarget(entitylivingbaseIn);

		if (entitylivingbaseIn == null)
		{
			this.setAngry(false);
		}
		else if (!this.isTamed())
		{
			this.setAngry(true);
		}

		if(entitylivingbaseIn != null && entitylivingbaseIn instanceof EntityWither && this.isTamed())
			this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(15.0D);
		else
			if(this.isTamed())
				this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
			else
				this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);

	}

	@Override
	protected void updateAITasks()
	{
		this.dataManager.set(DATA_HEALTH_ID, Float.valueOf(this.getHealth()));
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(DATA_HEALTH_ID, Float.valueOf(this.getHealth()));
	}

	@Override
	protected void playStepSound(BlockPos pos, Block blockIn)
	{
		this.playSound(SoundEvents.ENTITY_WOLF_STEP, 0.15F, 1.0F);
	}

	@Override
	protected float getSoundPitch()
	{
		return 0.35F;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		compound.setBoolean("Angry", this.isAngry());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		this.setAngry(compound.getBoolean("Angry"));
	}

	@Override
	protected SoundEvent getAmbientSound()
	{
		if (this.isAngry())
		{
			return SoundEvents.ENTITY_WOLF_GROWL;
		}
		else if (this.rand.nextInt(3) == 0)
		{
			return this.isTamed() && ((Float)this.dataManager.get(DATA_HEALTH_ID)).floatValue() < 10.0F ? SoundEvents.ENTITY_WOLF_WHINE : SoundEvents.ENTITY_WOLF_PANT;
		}
		else
		{
			return SoundEvents.ENTITY_WOLF_AMBIENT;
		}
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return SoundEvents.ENTITY_WOLF_HURT;
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ENTITY_WOLF_DEATH;
	}

	@Override
	protected float getSoundVolume()
	{
		return 0.4F;
	}

	@Override
	protected ResourceLocation getLootTable()
	{
		return ModLoot.HELLHOUND;
	}

	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();

		if (!this.world.isRemote && this.isWet && !this.isShaking && !this.hasPath() && this.onGround)
		{
			this.isShaking = true;
			this.timeWolfIsShaking = 0.0F;
			this.prevTimeWolfIsShaking = 0.0F;
			this.world.setEntityState(this, (byte)8);
		}

		if (!this.world.isRemote && this.getAttackTarget() == null && this.isAngry())
		{
			this.setAngry(false);
		}

		if(this.world.isRemote && !this.isTamed())
		{
			if(this.rand.nextInt(15) == 0)
				for (int i = 0; i < 2; ++i)
					ParticleUtils.spawn(EnumParticles.WITHER_CLOUD, this.world,
							this.posX + (this.rand.nextDouble() - 0.5D) * ((double)this.width * 1.5D), 
							this.posY + this.rand.nextDouble() * ((double)this.height * 1.5D) - 0.25D, 
							this.posZ + (this.rand.nextDouble() - 0.5D) * ((double)this.width * 1.5D),
							(this.rand.nextDouble() - 0.5D) * 2.0D, 
							-this.rand.nextDouble(), 
							(this.rand.nextDouble() - 0.5D) * 2.0D);
		}
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		if (!this.world.isRemote && !this.isTamed() && this.world.getDifficulty() == EnumDifficulty.PEACEFUL)
		{
			this.setDead();
		}

		this.headRotationCourseOld = this.headRotationCourse;

		if (this.isWet())
		{
			this.isWet = true;
			this.isShaking = false;
			this.timeWolfIsShaking = 0.0F;
			this.prevTimeWolfIsShaking = 0.0F;
		}
		else if ((this.isWet || this.isShaking) && this.isShaking)
		{
			if (this.timeWolfIsShaking == 0.0F)
			{
				this.playSound(SoundEvents.ENTITY_WOLF_SHAKE, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			}

			this.prevTimeWolfIsShaking = this.timeWolfIsShaking;
			this.timeWolfIsShaking += 0.05F;

			if (this.prevTimeWolfIsShaking >= 2.0F)
			{
				this.isWet = false;
				this.isShaking = false;
				this.prevTimeWolfIsShaking = 0.0F;
				this.timeWolfIsShaking = 0.0F;
			}

			if (this.timeWolfIsShaking > 0.4F)
			{
				float f = (float)this.getEntityBoundingBox().minY;
				int i = (int)(MathHelper.sin((this.timeWolfIsShaking - 0.4F) * (float)Math.PI) * 7.0F);

				for (int j = 0; j < i; ++j)
				{
					float f1 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
					float f2 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;

					if(this.world.isRemote)
						ParticleUtils.spawn(EnumParticles.LAVA_SPLASH, world, this.posX + (double)f1, (double)(f + 0.8F), this.posZ + (double)f2, this.motionX, this.motionY, this.motionZ);
				}
			}
		}

		if(!this.world.isRemote && !this.isAIDisabled())
		{
			if(this.getAttackTarget() != null)
			{
				if(this.getPositionVector().distanceTo(this.getAttackTarget().getPositionVector()) < 10D)
				{
					if(this.hidding)
					{
						this.setHidden(false);
						this.world.setEntityState(this, (byte)9);
					}
				}
				else
				{
					this.setHidden(true);
					this.world.setEntityState(this, (byte)10);
				}
			}
			else
			{
				this.setHidden(true);
				this.world.setEntityState(this, (byte)10);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public boolean isWolfWet()
	{
		return this.isWet;
	}

	@SideOnly(Side.CLIENT)
	public float getShadingWhileWet(float p_70915_1_)
	{
		return 0.75F + (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * p_70915_1_) / 2.0F * 0.25F;
	}

	@SideOnly(Side.CLIENT)
	public float getShakeAngle(float p_70923_1_, float p_70923_2_)
	{
		float f = (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * p_70923_1_ + p_70923_2_) / 1.8F;

		if (f < 0.0F)
		{
			f = 0.0F;
		}
		else if (f > 1.0F)
		{
			f = 1.0F;
		}

		return MathHelper.sin(f * (float)Math.PI) * MathHelper.sin(f * (float)Math.PI * 11.0F) * 0.15F * (float)Math.PI;
	}

	@SideOnly(Side.CLIENT)
	public float getInterestedAngle(float p_70917_1_)
	{
		return (this.headRotationCourseOld + (this.headRotationCourse - this.headRotationCourseOld) * p_70917_1_) * 0.15F * (float)Math.PI;
	}

	@Override
	public float getEyeHeight()
	{
		return this.height * 0.8F;
	}

	@Override
	public int getVerticalFaceSpeed()
	{
		return this.isSitting() ? 20 : super.getVerticalFaceSpeed();
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (this.isEntityInvulnerable(source))
		{
			return false;
		}
		else
		{
			Entity entity = source.getTrueSource();

			if (this.aiSit != null)
			{
				this.aiSit.setSitting(false);
			}

			if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow))
			{
				amount = (amount + 1.0F) / 2.0F;
			}

			return super.attackEntityFrom(source, amount);
		}
	}

	@Override
	public boolean isEntityInvulnerable(DamageSource source)
	{
		return source.getTrueSource() instanceof EntityWither || super.isEntityInvulnerable(source);
	}

	@Override
	public boolean isPotionApplicable(PotionEffect potioneffectIn)
	{
		if(potioneffectIn.getPotion() == MobEffects.WITHER)
			return false;

		return super.isPotionApplicable(potioneffectIn);
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn)
	{
		boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float)((int)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));

		if (flag)
		{
			this.applyEnchantments(this, entityIn);
		}

		return flag;
	}

	@Override
	public void setTamed(boolean tamed)
	{
		super.setTamed(tamed);

		if (tamed)
		{
			this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
		}
		else
		{
			this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
		}

		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
	}

	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);

		if (this.isTamed())
		{
			if (!stack.isEmpty())
			{
				if (stack.getItem() instanceof ItemFood)
				{
					ItemFood itemfood = (ItemFood)stack.getItem();

					if (itemfood.isWolfsFavoriteMeat() && ((Float)this.dataManager.get(DATA_HEALTH_ID)).floatValue() < 20.0F)
					{
						if (!player.capabilities.isCreativeMode)
						{
							stack.shrink(1);
						}

						this.heal((float)itemfood.getHealAmount(stack));
						return true;
					}
				}

				if (this.isOwner(player) && !this.world.isRemote && !this.isBreedingItem(stack))
				{
					this.aiSit.setSitting(!this.isSitting());
					this.isJumping = false;
					this.navigator.clearPath();
					this.setAttackTarget((EntityLivingBase)null);
				}
			}
		}
		else if(stack.getItem() instanceof ItemRib && !this.isAngry())
		{
			if(!player.capabilities.isCreativeMode)
				stack.shrink(1);

			if(!this.world.isRemote)
			{
				if (this.rand.nextInt(3) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, player))
				{
					this.setTamedBy(player);
					this.navigator.clearPath();
					this.setAttackTarget((EntityLivingBase)null);
					this.aiSit.setSitting(true);
					this.setHealth(20.0F);
					this.playTameEffect(true);
					this.world.setEntityState(this, (byte)7);
				}
				else
				{
					this.playTameEffect(false);
					this.world.setEntityState(this, (byte)6);
				}
			}

			return true;
		}

		return super.processInteract(player, hand);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleStatusUpdate(byte id)
	{
		if (id == 8)
		{
			this.isShaking = true;
			this.timeWolfIsShaking = 0.0F;
			this.prevTimeWolfIsShaking = 0.0F;
		}
		else if(id == 9)
		{
			if(!this.isAIDisabled())
				this.hidding = false;
		}
		else if(id == 10)
		{
			if(!this.isAIDisabled())
				this.hidding = true;
		}
		else
		{
			super.handleStatusUpdate(id);
		}
	}

	@SideOnly(Side.CLIENT)
	public float getTailRotation()
	{
		if (this.isAngry())
		{
			return 1.5393804F;
		}
		else
		{
			return this.isTamed() ? (0.55F - (this.getMaxHealth() - ((Float)this.dataManager.get(DATA_HEALTH_ID)).floatValue()) * 0.02F) * (float)Math.PI : ((float)Math.PI / 5F);
		}
	}

	@Override
	public boolean isBreedingItem(ItemStack stack)
	{
		return stack.getItem() instanceof ItemFood && ((ItemFood)stack.getItem()).isWolfsFavoriteMeat();
	}

	@Override
	public int getMaxSpawnedInChunk()
	{
		return 3;
	}

	public boolean isAngry()
	{
		return (((Byte)this.dataManager.get(TAMED)).byteValue() & 2) != 0;
	}

	public void setAngry(boolean angry)
	{
		byte b0 = ((Byte)this.dataManager.get(TAMED)).byteValue();

		if (angry)
		{
			this.dataManager.set(TAMED, Byte.valueOf((byte)(b0 | 2)));
		}
		else
		{
			this.dataManager.set(TAMED, Byte.valueOf((byte)(b0 & -3)));
		}
	}

	@Override
	public EntityHellhound createChild(EntityAgeable ageable)
	{
		EntityHellhound entityhellhound = new EntityHellhound(this.world);
		UUID uuid = this.getOwnerId();

		if(uuid != null)
		{
			entityhellhound.setOwnerId(uuid);
			entityhellhound.setTamed(true);
		}

		return entityhellhound;
	}

	@Override
	public boolean canMateWith(EntityAnimal otherAnimal)
	{
		if (otherAnimal == this)
		{
			return false;
		}
		else if (!this.isTamed())
		{
			return false;
		}
		else if (!(otherAnimal instanceof EntityHellhound))
		{
			return false;
		}
		else
		{
			EntityHellhound entitywolf = (EntityHellhound)otherAnimal;

			if (!entitywolf.isTamed())
			{
				return false;
			}
			else if (entitywolf.isSitting())
			{
				return false;
			}
			else
			{
				return this.isInLove() && entitywolf.isInLove();
			}
		}
	}

	@Override
	public boolean shouldAttackEntity(EntityLivingBase target, EntityLivingBase owner)
	{
		if (!(target instanceof EntityCreeper) && !(target instanceof EntityGhast))
		{
			if (target instanceof EntityHellhound)
			{
				EntityHellhound entitywolf = (EntityHellhound)target;

				if (entitywolf.isTamed() && entitywolf.getOwner() == owner)
				{
					return false;
				}
			}

			if (target instanceof EntityPlayer && owner instanceof EntityPlayer && !((EntityPlayer)owner).canAttackPlayer((EntityPlayer)target))
			{
				return false;
			}
			else
			{
				return !(target instanceof AbstractHorse) || !((AbstractHorse)target).isTame();
			}
		}
		else
		{
			return false;
		}
	}

	@Override
	public boolean canBeLeashedTo(EntityPlayer player)
	{
		return false;
	}

	@Override
	public boolean isWet()
	{
		return isInLava();
	}

	@Override
	public boolean getCanSpawnHere()
	{
		if(Config.HellhoundSpawnRate > 0)
		{
			if(this.world.getDifficulty() != EnumDifficulty.PEACEFUL && this.world.provider.getDimensionType() == DimensionType.NETHER && !isEntityInsideOpaqueBlock())
			{
				if(this.rand.nextInt(Config.HellhoundSpawnRate) == 0)
					return true;
			}
		}

		return false;
	}

	@Override
	protected boolean canDespawn() 
	{
		if(!this.isTamed())
			return true;

		return false;
	}

	@Override
	protected void setOnFireFromLava() {}

	@Override
	public void setFire(int seconds) {}

	@Override
	protected void dealFireDamage(int amount) {}

	@Override
	public boolean isImmuneToExplosions()
	{
		return true;
	}

	public void setHidden(boolean hidden)
	{
		this.hidding = hidden;
	}

	public boolean isHidden()
	{
		return this.hidding;
	}

	@Override
	public void sacredWaterEffect()
	{
		if(!this.isTamed())
		{
			EntityWolf wolf = new EntityWolf(world);
			wolf.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
			wolf.renderYawOffset = this.renderYawOffset;
			wolf.setHealth(wolf.getMaxHealth());

			this.setDead();
			world.spawnEntity(wolf);
		}
	}

	class AIAvoidEntity<T extends Entity> extends EntityAIAvoidEntity<T>
	{
		private final EntityHellhound wolf;

		public AIAvoidEntity(EntityHellhound wolfIn, Class<T> p_i47251_3_, float p_i47251_4_, double p_i47251_5_, double p_i47251_7_)
		{
			super(wolfIn, p_i47251_3_, p_i47251_4_, p_i47251_5_, p_i47251_7_);
			this.wolf = wolfIn;
		}

		/**
		 * Returns whether the EntityAIBase should begin execution.
		 */
		public boolean shouldExecute()
		{
			if (super.shouldExecute() && this.closestLivingEntity instanceof EntityZlama)
			{
				return !this.wolf.isTamed() && this.avoidZlama((EntityZlama)this.closestLivingEntity);
			}
			else
			{
				return false;
			}
		}

		private boolean avoidZlama(EntityZlama p_190854_1_)
		{
			return p_190854_1_.getStrength() >= EntityHellhound.this.rand.nextInt(5);
		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		public void startExecuting()
		{
			EntityHellhound.this.setAttackTarget((EntityLivingBase)null);
			super.startExecuting();
		}

		/**
		 * Keep ticking a continuous task that has already been started
		 */
		public void updateTask()
		{
			EntityHellhound.this.setAttackTarget((EntityLivingBase)null);
			super.updateTask();
		}
	}
}
