package com.iamshift.mineaddons.entities.boss;

import java.util.ArrayList;
import java.util.List;

import com.iamshift.mineaddons.init.ModLoot;
import com.iamshift.mineaddons.interfaces.IUncapturable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityWitherBlaze extends EntityBoss implements IUncapturable
{
	private static final DataParameter<Float> SCALE = EntityDataManager.<Float>createKey(EntityGhostRider.class, DataSerializers.FLOAT);

	private float scale = 3F;

	private float heightOffset = 0.5F;
	private int heightOffsetUpdateTime;

	private boolean grow1 = false;
	private boolean grow2 = false;
	private boolean grow3 = false;

	private List<EntityBlaze> blazes = new ArrayList<EntityBlaze>(); 
	
	public EntityWitherBlaze(World worldIn) 
	{
		super(worldIn);
		this.setSize(3F, 5.4F);
		this.setPathPriority(PathNodeType.WATER, -1.0F);
		this.setPathPriority(PathNodeType.LAVA, 8.0F);
		this.setPathPriority(PathNodeType.DANGER_FIRE, 0.0F);
		this.setPathPriority(PathNodeType.DAMAGE_FIRE, 0.0F);
		this.isImmuneToFire = true;
		this.experienceValue = 100;

	}

	@Override
	protected void initEntityAI() 
	{
		this.tasks.addTask(1, new EntityWitherBlaze.AICastingSpell(this));
		this.tasks.addTask(4, new EntityWitherBlaze.AIFireballAttack(this));
        this.tasks.addTask(5, new EntityWitherBlaze.AISummonSpell(this));
		this.tasks.addTask(6, new EntityWitherBlaze.AITeleport(this));
		this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
		this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0D, 0.0F));
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		this.setInvulTime(compound.getInteger("Invul"));

		this.grow1 = compound.getBoolean("Grow1");
		this.grow2 = compound.getBoolean("Grow2");
		this.grow3 = compound.getBoolean("Grow3");

		this.setScale(compound.getFloat("Scale"));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		compound.setInteger("Invul", this.getInvulTime());

		compound.setBoolean("Grow1", this.grow1);
		compound.setBoolean("Grow2", this.grow2);
		compound.setBoolean("Grow3", this.grow3);

		compound.setFloat("Scale", this.getScale());
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(SCALE, Float.valueOf(3F));
	}

	@Override
	protected void applyEntityAttributes() 
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(10.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(450.0F);
	}
	
	@Override
	protected SoundEvent getAmbientSound()
	{
		return SoundEvents.ENTITY_BLAZE_AMBIENT;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return SoundEvents.ENTITY_BLAZE_HURT;
	}
	
	@Override
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ENTITY_BLAZE_DEATH;
	}
	
	@Override
	protected SoundEvent getSpellSound()
	{
		return SoundEvents.EVOCATION_ILLAGER_CAST_SPELL;
	}

	@Override
	protected float getSoundPitch() 
	{
		return (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.5F;
	}

	@Override
	protected ResourceLocation getLootTable()
	{
		return ModLoot.WITHER_BLAZE;
	}

	@Override
	public boolean canRenderOnFire()
	{
		return false;
	}

	@Override
	public boolean isBurning()
	{
		return false;
	}

	public float getScale()
	{
		return ((Float)this.dataManager.get(SCALE)).floatValue();
	}

	public void setScale(float s)
	{
		this.dataManager.set(SCALE, Float.valueOf(s));
	}

	public void decrScale()
	{
		float scale = getScale();
		scale -= .005F;
		setScale(scale);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if(this.isEntityInvulnerable(source))
			return false;
		else if(source.getTrueSource() instanceof EntityBlaze)
			return false;
		else if(source != DamageSource.DROWN)
		{
			if(getStage() == 2)
				if((this.getHealth() - amount) < 150F)
				{
					this.setHealth(150f);
					amount = 0;
					this.setInvulTime(60);
					updateBars();
					return false;
				}

			if(getStage() == 1)
				if((this.getHealth() - amount) < 300F)
				{
					this.setHealth(300f);
					amount = 0;
					this.setInvulTime(60);
					updateBars();
					return false;
				}

			if(this.getIsInvulnerable() && source != DamageSource.OUT_OF_WORLD)
				return false;
		}

		return super.attackEntityFrom(source, amount);
	}

	@Override
	public void onLivingUpdate()
	{
		if (!this.onGround && this.motionY < 0.0D)
        {
            this.motionY *= 0.6D;
        }
		
		if(getStage() == 1 && !grow1) //450
		{
			this.setHealth(450F);

			this.grow1 = true;
		}
		else if(getStage() == 2 && !grow2) //300
		{
			this.setHealth(300F);

			this.grow2 = true;
		}
		else if(getStage() == 3 && !grow3) //150
		{
			this.setHealth(150F);
			this.setSize(1.5F, 2.7F);

			this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(20.0D);
			this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.50D);

			this.grow3 = true;
		}

		if(!this.world.isRemote)
		{
			this.destroyBlocksInAABB(this.getEntityBoundingBox());
		}

		super.onLivingUpdate();
	}
	
	@Override
	protected void updateAITasks()
	{
		super.updateAITasks();

		if(!this.getIsInvulnerable())
		{
			if (this.isWet())
	            this.attackEntityFrom(DamageSource.DROWN, 1.0F);
			
			--this.heightOffsetUpdateTime;
			
			if (this.heightOffsetUpdateTime <= 0)
	        {
	            this.heightOffsetUpdateTime = 100;
	            this.heightOffset = 0.5F + (float)this.rand.nextGaussian() * 3.0F;
	        }

	        EntityLivingBase entitylivingbase = this.getAttackTarget();

	        if (entitylivingbase != null && entitylivingbase.posY + (double)entitylivingbase.getEyeHeight() > this.posY + (double)this.getEyeHeight() + (double)this.heightOffset)
	        {
	            this.motionY += (0.30000001192092896D - this.motionY) * 0.30000001192092896D;
	            this.isAirBorne = true;
	        }
		}
	}

	private void destroyBlocksInAABB(AxisAlignedBB aabb)
	{
		if (this.world.getGameRules().getBoolean("mobGriefing"))
			return;

		int i = MathHelper.floor(aabb.minX);
		int j = MathHelper.floor(aabb.minY);
		int k = MathHelper.floor(aabb.minZ);
		int l = MathHelper.floor(aabb.maxX);
		int i1 = MathHelper.floor(aabb.maxY);
		int j1 = MathHelper.floor(aabb.maxZ);

		boolean flag1 = false;

		for (int k1 = i; k1 <= l; ++k1)
		{
			for (int l1 = j; l1 <= i1; ++l1)
			{
				for (int i2 = k; i2 <= j1; ++i2)
				{
					BlockPos blockpos = new BlockPos(k1, l1, i2);
					IBlockState iblockstate = this.world.getBlockState(blockpos);
					Block block = iblockstate.getBlock();

					if (!block.isAir(iblockstate, this.world, blockpos) && iblockstate.getMaterial() != Material.FIRE)
						if (block.canEntityDestroy(iblockstate, this.world, blockpos, this) && net.minecraftforge.event.ForgeEventFactory.onEntityDestroyBlock(this, blockpos, iblockstate))
							if (block != Blocks.COMMAND_BLOCK && block != Blocks.REPEATING_COMMAND_BLOCK && block != Blocks.CHAIN_COMMAND_BLOCK && block != Blocks.BEDROCK)
								flag1 = this.world.setBlockToAir(blockpos) || flag1;
				}
			}
		}

		if (flag1)
		{
			double d0 = aabb.minX + (aabb.maxX - aabb.minX) * (double)this.rand.nextFloat();
			double d1 = aabb.minY + (aabb.maxY - aabb.minY) * (double)this.rand.nextFloat();
			double d2 = aabb.minZ + (aabb.maxZ - aabb.minZ) * (double)this.rand.nextFloat();
			this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender()
	{
		return 15728880;
	}

	@Override
	public float getBrightness()
	{
		return 1.0F;
	}

	@Override
	public void fall(float distance, float damageMultiplier) {}

	@Override
	protected boolean isValidLightLevel()
	{
		return true;
	}
	
	class AICastingSpell extends EntityBoss.AICastingSpell
	{
		private final EntityWitherBlaze blaze;

		private AICastingSpell(EntityWitherBlaze blaze)
		{
			super();
			this.blaze = blaze;
		}

		@Override
		public void updateTask()
		{
			if(blaze.getAttackTarget() != null)
				blaze.getLookHelper().setLookPositionWithEntity(blaze.getAttackTarget(), (float)blaze.getHorizontalFaceSpeed(), (float)blaze.getVerticalFaceSpeed());
		}
	}

	static class AIFireballAttack extends EntityAIBase
	{
		private final EntityWitherBlaze blaze;
		private int attackStep;
		private int attackTime;

		public AIFireballAttack(EntityWitherBlaze blazeIn)
		{
			this.blaze = blazeIn;
			this.setMutexBits(3);
		}

		public boolean shouldExecute()
		{
			EntityLivingBase entitylivingbase = this.blaze.getAttackTarget();
			return entitylivingbase != null && entitylivingbase.isEntityAlive();
		}

		public void startExecuting()
		{
			this.attackStep = 0;
		}

		public void updateTask()
		{
			--this.attackTime;
			EntityLivingBase entitylivingbase = this.blaze.getAttackTarget();
			double d0 = this.blaze.getDistanceSq(entitylivingbase);

			if (d0 < 4.0D)
			{
				if (this.attackTime <= 0)
				{
					this.attackTime = 20;
					this.blaze.attackEntityAsMob(entitylivingbase);
				}

				this.blaze.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
			}
			else if (d0 < this.getFollowDistance() * this.getFollowDistance())
			{
				double d1 = entitylivingbase.posX - this.blaze.posX;
				double d2 = entitylivingbase.getEntityBoundingBox().minY + (double)(entitylivingbase.height / 2.0F) - (this.blaze.posY + (double)(this.blaze.height / 2.0F));
				double d3 = entitylivingbase.posZ - this.blaze.posZ;

				if (this.attackTime <= 0)
				{
					++this.attackStep;

					if (this.attackStep == 1)
					{
						this.attackTime = 60;
					}
					else if (this.attackStep <= 4)
					{
						this.attackTime = 6;
					}
					else
					{
						this.attackTime = 100;
						this.attackStep = 0;
					}

					if (this.attackStep > 1)
					{
						float f = MathHelper.sqrt(MathHelper.sqrt(d0)) * 0.5F;
						this.blaze.world.playEvent((EntityPlayer)null, 1018, new BlockPos((int)this.blaze.posX, (int)this.blaze.posY, (int)this.blaze.posZ), 0);

						for (int i = 0; i < 1; ++i)
						{
							EntityLargeFireball fireball = new EntityLargeFireball(this.blaze.world, this.blaze, d1 + this.blaze.getRNG().nextGaussian() * (double)f, d2, d3 + this.blaze.getRNG().nextGaussian() * (double)f);
							fireball.posY = this.blaze.posY + (double)(this.blaze.height / 2.0F) + 0.5D;
							this.blaze.world.spawnEntity(fireball);
						}
					}
				}

				this.blaze.getLookHelper().setLookPositionWithEntity(entitylivingbase, 10.0F, 10.0F);
			}
			else
			{
				this.blaze.getNavigator().clearPath();
				this.blaze.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
			}

			super.updateTask();
		}

		private double getFollowDistance()
		{
			IAttributeInstance iattributeinstance = this.blaze.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
			return iattributeinstance == null ? 16.0D : iattributeinstance.getAttributeValue();
		}
	}
	
	class AISummonSpell extends EntityBoss.AIUseSpell
	{
		private final EntityWitherBlaze blaze;

		private AISummonSpell(EntityWitherBlaze blaze)
		{
			super();
			this.blaze = blaze;
		}

		@Override
		public boolean shouldExecute()
		{
			if(!super.shouldExecute())
				return false;
			else if(blaze.getStage() != 2)
				return false;
			else if(blaze.getIsInvulnerable())
				return false;
			else
			{
				int i = blaze.world.getEntitiesWithinAABB(EntityBlaze.class, blaze.getEntityBoundingBox().grow(16.0D)).size();
				return blaze.rand.nextInt(8) + 1 > i;
			}
		}

		@Override
		protected int getCastingTime()
		{
			return 0;
		}

		@Override
		protected int getCastingInterval()
		{
			return blaze.rand.nextInt(200) + 100;
		}

		@Override
		protected SoundEvent getSpellPrepareSound()
		{
			return SoundEvents.EVOCATION_ILLAGER_PREPARE_SUMMON;
		}

		@Override
		protected EntityBoss.SpellType getSpellType()
		{
			return EntityBoss.SpellType.SUMMON;
		}

		@Override
		protected void castSpell()
		{
			for(int i = 0; i < 2; ++i)
			{
				BlockPos blockpos = (new BlockPos(blaze)).add(-2 + blaze.rand.nextInt(5), 1, -2 + blaze.rand.nextInt(5));
				EntityBlaze b = new EntityBlaze(blaze.world);
				b.moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);
				b.onInitialSpawn(blaze.world.getDifficultyForLocation(blockpos), (IEntityLivingData)null);
				blaze.world.spawnEntity(b);
				
				blaze.blazes.add(b);
			}
		}
	}
	
	class AITeleport extends EntityBoss.AIUseSpell
	{
		private final EntityWitherBlaze blaze;

		public AITeleport(EntityWitherBlaze blaze)
		{
			super();
			this.blaze = blaze;
		}

		@Override
		public boolean shouldExecute()
		{
			if(!super.shouldExecute())
				return false;
			else if(blaze.getStage() != 3)
				return false;
			else if(blaze.getIsInvulnerable())
				return false;
			else
				return true;
		}
		
		@Override
		protected void castSpell()
		{
			double d0 = blaze.posX + (blaze.rand.nextDouble() - 0.5D) * 3.0D;
			double d1 = blaze.posY;
			double d2 = blaze.posZ + (blaze.rand.nextDouble() - 0.5D) * 3.0D;

			boolean flag = blaze.attemptTeleport(d0, d1, d2);
			
			if(flag)
				blaze.world.playSound((EntityPlayer)null, blaze.prevPosX, blaze.prevPosY, blaze.prevPosZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.NEUTRAL, 100.0F, 1.0F);
		}

		@Override
		protected int getCastingInterval()
		{
			return blaze.rand.nextInt(200) + 100;
		}

		@Override
		protected EntityBoss.SpellType getSpellType()
		{
			return EntityBoss.SpellType.SPELL;
		}

		@Override
		protected int getCastingTime()
		{
			return 0;
		}

		@Override
		protected SoundEvent getSpellPrepareSound()
		{
			return null;
		}
	}
}
