package com.iamshift.mineaddons.entities.boss;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.iamshift.mineaddons.entities.items.EntitySpiritBomb;
import com.iamshift.mineaddons.entities.items.EntityVoidball;
import com.iamshift.mineaddons.init.ModItems;
import com.iamshift.mineaddons.init.ModLoot;
import com.iamshift.mineaddons.init.ModSounds;
import com.iamshift.mineaddons.interfaces.IUncapturable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityVoix extends EntityBoss implements IUncapturable
{
	private static final DataParameter<Boolean> ATTACKING = EntityDataManager.<Boolean>createKey(EntityVoix.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> CHARGING = EntityDataManager.<Boolean>createKey(EntityVoix.class, DataSerializers.BOOLEAN);

	@Nullable
	private BlockPos boundOrigin;

	private boolean attChanged = false;

	private List<EntityVex> vex = new ArrayList<EntityVex>();
	private EntitySpiritBomb bomb = null;

	public EntityVoix(World worldIn)
	{
		super(worldIn);
		this.setSize(0.6F, 1.8F);
		this.experienceValue = 100;
		this.isImmuneToFire = true;
		this.moveHelper = new EntityVoix.AIMoveControl(this);

		this.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(ModItems.Wings));
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		this.setNoGravity(true);
	}

	@Override
	protected void initEntityAI()
	{
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityVoix.AICastingSpell(this));
		this.tasks.addTask(4, new EntityVoix.AIFireballAttack(this));
		this.tasks.addTask(5, new EntityVoix.AISummonSpell(this));
		this.tasks.addTask(6, new EntityVoix.AISpiritBomb(this));
		this.tasks.addTask(8, new EntityVoix.AIMoveRandom());
		this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
		this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(ATTACKING, Boolean.valueOf(false));
		this.dataManager.register(CHARGING, Boolean.valueOf(false));
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		this.attChanged = compound.getBoolean("AttChanged");

		if (compound.hasKey("BoundX"))
			this.boundOrigin = new BlockPos(compound.getInteger("BoundX"), compound.getInteger("BoundY"), compound.getInteger("BoundZ"));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		compound.setBoolean("AttChanged", this.attChanged);

		if (this.boundOrigin != null)
		{
			compound.setInteger("BoundX", this.boundOrigin.getX());
			compound.setInteger("BoundY", this.boundOrigin.getY());
			compound.setInteger("BoundZ", this.boundOrigin.getZ());
		}
	}

	@SideOnly(Side.CLIENT)
	public boolean isAttacking()
	{
		return ((Boolean)this.dataManager.get(ATTACKING)).booleanValue();
	}

	public void setAttacking(boolean flag)
	{
		this.dataManager.set(ATTACKING, Boolean.valueOf(flag));
	}

	public boolean isCharging()
	{
		return ((Boolean)this.dataManager.get(CHARGING)).booleanValue();
	}

	public void setCharging(boolean flag)
	{
		this.dataManager.set(CHARGING, Boolean.valueOf(flag));
		this.setNoGravity(flag);
	}

	@Nullable
	public BlockPos getBoundOrigin()
	{
		return this.boundOrigin;
	}

	public void setBoundOrigin(@Nullable BlockPos boundOriginIn)
	{
		this.boundOrigin = boundOriginIn;
	}

	@Override
	protected ResourceLocation getLootTable()
	{
		return ModLoot.VOIX;
	}

	@Override
	protected SoundEvent getAmbientSound()
	{
		return SoundEvents.ENTITY_VEX_AMBIENT;
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ENTITY_VEX_DEATH;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return SoundEvents.ENTITY_VEX_HURT;
	}

	@Override
	protected SoundEvent getSpellSound()
	{
		return SoundEvents.EVOCATION_ILLAGER_CAST_SPELL;
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
	protected float getSoundPitch()
	{
		return (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.5F;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if(this.isEntityInvulnerable(source))
			return false;
		else if(source.getTrueSource() instanceof EntityVex || source.getImmediateSource() instanceof EntitySpiritBomb)
			return false;
		else if(source == DamageSource.FALL)
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
	public boolean canExplosionDestroyBlock(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn, float p_174816_5_)
	{
		return false;
	}

	@Override
	public void onLivingUpdate()
	{
		if(getStage() == 3 && !attChanged)
		{
			this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(20.0D);
			this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.50D);
			attChanged = true;
		}

		super.onLivingUpdate();
	}

	@Override
	public void setDead()
	{
		for(EntityVex v : vex)
		{
			if(v != null)
				v.setDead();
		}
		
		if(this.bomb != null)
			this.bomb.setDead();

		super.setDead();
	}

	class AIMoveControl extends EntityMoveHelper
	{
		public AIMoveControl(EntityVoix voix)
		{
			super(voix);
		}

		public void onUpdateMoveHelper()
		{
			if (this.action == EntityMoveHelper.Action.MOVE_TO)
			{
				double d0 = this.posX - EntityVoix.this.posX;
				double d1 = this.posY - EntityVoix.this.posY;
				double d2 = this.posZ - EntityVoix.this.posZ;
				double d3 = d0 * d0 + d1 * d1 + d2 * d2;
				d3 = (double)MathHelper.sqrt(d3);

				if (d3 < EntityVoix.this.getEntityBoundingBox().getAverageEdgeLength())
				{
					this.action = EntityMoveHelper.Action.WAIT;
					EntityVoix.this.motionX *= 0.5D;
					EntityVoix.this.motionY *= 0.5D;
					EntityVoix.this.motionZ *= 0.5D;
				}
				else
				{
					EntityVoix.this.motionX += d0 / d3 * 0.05D * this.speed;
					EntityVoix.this.motionY += d1 / d3 * 0.05D * this.speed;
					EntityVoix.this.motionZ += d2 / d3 * 0.05D * this.speed;

					if (EntityVoix.this.getAttackTarget() == null)
					{
						EntityVoix.this.rotationYaw = -((float)MathHelper.atan2(EntityVoix.this.motionX, EntityVoix.this.motionZ)) * (180F / (float)Math.PI);
						EntityVoix.this.renderYawOffset = EntityVoix.this.rotationYaw;
					}
					else
					{
						double d4 = EntityVoix.this.getAttackTarget().posX - EntityVoix.this.posX;
						double d5 = EntityVoix.this.getAttackTarget().posZ - EntityVoix.this.posZ;
						EntityVoix.this.rotationYaw = -((float)MathHelper.atan2(d4, d5)) * (180F / (float)Math.PI);
						EntityVoix.this.renderYawOffset = EntityVoix.this.rotationYaw;
					}
				}
			}
		}
	}

	class AIMoveRandom extends EntityAIBase
	{
		public AIMoveRandom()
		{
			this.setMutexBits(1);
		}

		/**
		 * Returns whether the EntityAIBase should begin execution.
		 */
		public boolean shouldExecute()
		{
			return !EntityVoix.this.getMoveHelper().isUpdating() && EntityVoix.this.rand.nextInt(7) == 0 && !EntityVoix.this.getIsInvulnerable() && !EntityVoix.this.isCharging();
		}

		/**
		 * Returns whether an in-progress EntityAIBase should continue executing
		 */
		public boolean shouldContinueExecuting()
		{
			return false;
		}

		/**
		 * Keep ticking a continuous task that has already been started
		 */
		public void updateTask()
		{
			BlockPos blockpos = EntityVoix.this.getBoundOrigin();

			if (blockpos == null)
			{
				blockpos = new BlockPos(EntityVoix.this);
			}

			for (int i = 0; i < 3; ++i)
			{
				BlockPos blockpos1 = blockpos.add(EntityVoix.this.rand.nextInt(15) - 7, EntityVoix.this.rand.nextInt(11) - 5, EntityVoix.this.rand.nextInt(15) - 7);

				if (EntityVoix.this.world.isAirBlock(blockpos1))
				{
					EntityVoix.this.moveHelper.setMoveTo((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 0.25D);

					if (EntityVoix.this.getAttackTarget() == null)
					{
						EntityVoix.this.getLookHelper().setLookPosition((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
					}

					break;
				}
			}
		}
	}

	static class AIFireballAttack extends EntityAIBase
	{
		private final EntityVoix voix;
		private int attackTime;

		public AIFireballAttack(EntityVoix voixIn)
		{
			this.voix = voixIn;
		}

		public boolean shouldExecute()
		{
			return voix.getAttackTarget() != null && !voix.isSpellcasting() && !voix.isCharging() && !voix.getIsInvulnerable();
		}

		public void startExecuting()
		{
			this.attackTime = 0;
		}

		public void resetTask()
		{
			voix.setAttacking(false);
		}

		public void updateTask()
		{
			EntityLivingBase target = voix.getAttackTarget();
			double d0 = 64.0D;

			if(target.getDistanceSq(voix) < 4096.0D && voix.canEntityBeSeen(target))
			{
				World world = voix.world;
				++this.attackTime;

				if(this.attackTime == 20)
				{
					double d1 = 4.0D;
					Vec3d vec3d = voix.getLook(1.0F);
					double d2 = target.posX - (voix.posX + vec3d.x * 4.0D);
					double d3 = target.posY - (voix.posY + vec3d.y * 4.0D);
					double d4 = target.posZ - (voix.posZ + vec3d.z * 4.0D);
					voix.playSound(ModSounds.voidball, voix.getSoundVolume(), voix.getSoundPitch());

					EntityVoidball voidball = new EntityVoidball(world, voix, d2, d3, d4);
					voidball.posX = voix.posX + vec3d.x * 4.0D;
					voidball.posY = voix.posY + (double)(voix.height / 2.0F) + 0.5D;
					voidball.posZ = voix.posZ + vec3d.z * 4.0D;
					world.spawnEntity(voidball);
					this.attackTime = -40;
				}
			}
			else if(this.attackTime > 0)
				--this.attackTime;

			voix.setAttacking(this.attackTime > 10);
		}
	}

	class AICastingSpell extends EntityBoss.AICastingSpell
	{
		private final EntityVoix voix;

		private AICastingSpell(EntityVoix voix)
		{
			super();
			this.voix = voix;
		}

		@Override
		public void updateTask()
		{
			if(voix.getAttackTarget() != null)
				voix.getLookHelper().setLookPositionWithEntity(voix.getAttackTarget(), (float)voix.getHorizontalFaceSpeed(), (float)voix.getVerticalFaceSpeed());
		}
	}

	class AISummonSpell extends EntityBoss.AIUseSpell
	{
		private final EntityVoix voix;

		private AISummonSpell(EntityVoix voix)
		{
			super();
			this.voix = voix;
		}

		@Override
		public boolean shouldExecute()
		{
			if(!super.shouldExecute())
				return false;
			else if(voix.getStage() == 1)
				return false;
			else if(voix.isCharging())
				return false;
			else if(voix.getIsInvulnerable())
				return false;
			else
			{
				int i = voix.world.getEntitiesWithinAABB(EntityVex.class, voix.getEntityBoundingBox().grow(16.0D)).size();
				return voix.rand.nextInt(8) + 1 > i;
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
			return voix.rand.nextInt(200) + 100;
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
			for(int i = 0; i < 5; ++i)
			{
				BlockPos blockpos = (new BlockPos(voix)).add(-2 + voix.rand.nextInt(5), 1, -2 + voix.rand.nextInt(5));
				EntityVex entityvex = new EntityVex(voix.world);
				entityvex.moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);
				entityvex.onInitialSpawn(voix.world.getDifficultyForLocation(blockpos), (IEntityLivingData)null);
				entityvex.setOwner(voix);
				entityvex.setBoundOrigin(blockpos);
				entityvex.setLimitedLife(20 * (30 + voix.rand.nextInt(90)));
				voix.world.spawnEntity(entityvex);

				voix.vex.add(entityvex);
			}
		}
	}

	class AISpiritBomb extends EntityBoss.AIUseSpell
	{
		private final EntityVoix voix;
		private final double range = 16.0D;

		public AISpiritBomb(EntityVoix voix)
		{
			super();
			this.voix = voix;
		}

		@Override
		public boolean shouldExecute()
		{
			if(!super.shouldExecute())
				return false;
			else if(voix.getStage() < 3)
				return false;
			else if(voix.getIsInvulnerable())
				return false;
			else
				return true;
		}
		
		@Override
		public void startExecuting()
		{
			super.startExecuting();

			voix.setCharging(true);

			voix.bomb = new EntitySpiritBomb(voix.world);
			voix.bomb.setPositionAndUpdate(voix.posX, voix.posY + 5, voix.posZ);

			voix.world.spawnEntity(voix.bomb);

			voix.world.playSound((EntityPlayer)null, voix.posX, voix.posY, voix.posZ, ModSounds.spiritbomb, SoundCategory.HOSTILE, 100.0F, 1.0F);
		}

		@Override
		protected void castSpell()
		{
			voix.setCharging(false);

			EntityLivingBase target = voix.getAttackTarget();
			voix.bomb.setDead();

			if(target instanceof EntityPlayer)
			{
				if(!((EntityPlayer) target).capabilities.isCreativeMode)
				{
					double d1 = 4.0D;
					Vec3d vec3d = voix.getLook(1.0F);
					double d2 = target.posX - (voix.posX + vec3d.x * 4.0D);
					double d3 = target.getEntityBoundingBox().minY + (double)(target.height / 2.0F) - (0.5D + (voix.posY + 5) + (double)(voix.height / 2.0F));
					double d4 = target.posZ - (voix.posZ + vec3d.z * 4.0D);

					EntitySpiritBomb newbomb = new EntitySpiritBomb(voix.world, voix, d2, d3, d4, voix.bomb.getScale());
					newbomb.setPositionAndUpdate(voix.bomb.posX, voix.bomb.posY, voix.bomb.posZ);
					voix.world.spawnEntity(newbomb);
					newbomb.shoot();
				}
			}
		}

		@Override
		public void updateTask()
		{
			super.updateTask();

			if(this.spellWarmup > 0)
			{
				voix.setCharging(true);
				voix.bomb.setScale(voix.bomb.getScale() + .1F);

				voix.bomb.setPositionAndUpdate(voix.posX, voix.posY + 5, voix.posZ);
			}
		}

		@Override
		protected int getCastWarmupTime()
		{
			return 100;
		}

		@Override
		protected int getCastingTime()
		{
			return 0;
		}

		@Override
		protected int getCastingInterval()
		{
			return voix.rand.nextInt(200) + 100;
		}

		@Override
		protected SoundEvent getSpellPrepareSound()
		{
			return null;
		}

		@Override
		protected EntityBoss.SpellType getSpellType()
		{
			return EntityBoss.SpellType.SPELL;
		}
	}
}
