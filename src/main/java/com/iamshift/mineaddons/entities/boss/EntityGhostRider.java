package com.iamshift.mineaddons.entities.boss;

import java.util.ArrayList;
import java.util.List;

import com.iamshift.mineaddons.core.Config;
import com.iamshift.mineaddons.init.ModLoot;
import com.iamshift.mineaddons.init.ModSounds;
import com.iamshift.mineaddons.interfaces.IUncapturable;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAttackRangedBow;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityGhostRider extends EntityBoss implements IUncapturable, IRangedAttackMob
{
	private static final DataParameter<Boolean> SWINGING_ARMS = EntityDataManager.<Boolean>createKey(EntityGhostRider.class, DataSerializers.BOOLEAN);
	private final EntityAIAttackRangedBow<EntityGhostRider> aiArrowAttack = new EntityAIAttackRangedBow<EntityGhostRider>(this, 1.0D, 20, 15.0F);
	private final EntityAIAttackMelee aiAttackOnCollide = new EntityAIAttackMelee(this, 1.2D, false)
	{
		public void resetTask() 
		{
			super.resetTask();
			EntityGhostRider.this.setSwingingArms(false);
		};

		public void startExecuting() 
		{
			super.startExecuting();
			EntityGhostRider.this.setSwingingArms(true);
		};
		
		public boolean shouldExecute() 
		{
			if(getIsInvulnerable())
				return false;
			
			return super.shouldExecute();
		};
		
	};
	
	private static final DataParameter<Float> SCALE = EntityDataManager.<Float>createKey(EntityGhostRider.class, DataSerializers.FLOAT);

	private boolean rider = false;
	private boolean bowman = false;
	private boolean melee = false;
	
	private List<AbstractSkeleton> skellys = new ArrayList<AbstractSkeleton>();

	public EntityGhostRider(World worldIn)
	{
		super(worldIn);
		this.setSize(1.0F, 3.1F);
		this.isImmuneToFire = true;
		this.experienceValue = 100;
		
		this.setCombatTask();
	}

	@Override
	protected void initEntityAI()
	{
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityGhostRider.AICastingSpell(this));
		this.tasks.addTask(2, new EntityGhostRider.AIPush(this));
		this.tasks.addTask(3, new EntityGhostRider.AIPull(this));
		this.tasks.addTask(4, new EntityGhostRider.AISummonSpell(this));
		this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0D));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(450.0D);
	}	

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		this.setScale(compound.getFloat("Scale"));
		this.melee = compound.getBoolean("Melee");
		this.bowman = compound.getBoolean("Bowman");
		this.rider = compound.getBoolean("Rider");
		this.setCombatTask();
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		compound.setFloat("Scale", this.getScale());
		compound.setBoolean("Melee", this.melee);
		compound.setBoolean("Bowman", this.bowman);
		compound.setBoolean("Rider", this.rider);
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(SWINGING_ARMS, Boolean.valueOf(false));
		this.dataManager.register(SCALE, Float.valueOf(1.5F));
	}

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
	{
		livingdata = super.onInitialSpawn(difficulty, livingdata);
		this.setEquipmentBasedOnDifficulty(difficulty);
		this.setEnchantmentBasedOnDifficulty(difficulty);
		this.setCombatTask();
		this.setCanPickUpLoot(false);

		return livingdata;
	}
	
	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty)
	{
		super.setEquipmentBasedOnDifficulty(difficulty);
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
	}
	
	public void setCombatTask()
	{
		if (this.world != null && !this.world.isRemote && !this.dead)
		{
			this.tasks.removeTask(this.aiAttackOnCollide);
			this.tasks.removeTask(this.aiArrowAttack);
			ItemStack itemstack = this.getHeldItemMainhand();

			if (itemstack.getItem() == Items.BOW)
			{
				int i = 20;

				if (this.world.getDifficulty() != EnumDifficulty.HARD)
				{
					i = 40;
				}

				this.aiArrowAttack.setAttackCooldown(i);
				this.tasks.addTask(4, this.aiArrowAttack);
			}
			else
			{
				this.tasks.addTask(4, this.aiAttackOnCollide);
			}
		}
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor)
	{
		EntityArrow entityarrow = this.getArrow(distanceFactor);
		double d0 = target.posX - this.posX;
		double d1 = target.getEntityBoundingBox().minY + (double)(target.height / 3.0F) - entityarrow.posY;
		double d2 = target.posZ - this.posZ;
		double d3 = (double)MathHelper.sqrt(d0 * d0 + d2 * d2);
		entityarrow.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (float)(14 - this.world.getDifficulty().getDifficultyId() * 4));
		this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
		this.world.spawnEntity(entityarrow);
	}

	@Override
	protected void playStepSound(BlockPos pos, Block blockIn)
	{
		this.playSound(this.getStepSound(), 0.15F, 1.0F);
	}

	@Override
	public EnumCreatureAttribute getCreatureAttribute()
	{
		return EnumCreatureAttribute.UNDEAD;
	}

	@Override
	public float getEyeHeight()
	{
		return 2.6F;
	}
	
	@Override
	public double getYOffset()
	{
		return -0.6D;
	}

	@Override
	protected boolean canEquipItem(ItemStack stack)
	{
		return false;
	}

	@Override
	public boolean canRenderOnFire()
	{
		return false;
	}

	@Override
	protected ResourceLocation getLootTable()
	{
		return ModLoot.GHOST_RIDER;
	}

	@Override
	protected SoundEvent getAmbientSound()
	{
		return SoundEvents.ENTITY_STRAY_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return SoundEvents.ENTITY_STRAY_HURT;
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ENTITY_STRAY_DEATH;
	}

	protected SoundEvent getStepSound()
	{
		return SoundEvents.ENTITY_STRAY_STEP;
	}

	@Override
	protected SoundEvent getSpellSound()
	{
		return SoundEvents.EVOCATION_ILLAGER_CAST_SPELL;
	}

	@Override
	protected float getSoundPitch()
	{
		return (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.75F;
	}

	protected EntityArrow getArrow(float p_190726_1_)
	{
		EntityTippedArrow entitytippedarrow = new EntityTippedArrow(this.world, this);
		entitytippedarrow.setEnchantmentEffectsFromEntity(this, p_190726_1_);

		if (entitytippedarrow instanceof EntityTippedArrow)
		{
			if(getStage() == 1)
				((EntityTippedArrow)entitytippedarrow).addEffect(new PotionEffect(MobEffects.SLOWNESS, 600));
			else
				((EntityTippedArrow)entitytippedarrow).addEffect(new PotionEffect(MobEffects.LEVITATION, 600));
		}

		return entitytippedarrow;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if(Config.FakePlayer && source.getTrueSource() instanceof FakePlayer)
			return false;
		
		if(this.isEntityInvulnerable(source) || getStage() == 1)
			return false;
		else if(source.getTrueSource() instanceof AbstractSkeleton)
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

			if(this.getIsInvulnerable() && source != DamageSource.OUT_OF_WORLD)
				return false;
		}

		return super.attackEntityFrom(source, amount);
	}

	public float getScale()
	{
		return ((Float)this.dataManager.get(SCALE)).floatValue();
	}
	
	public void setScale(float s)
	{
		this.dataManager.set(SCALE, Float.valueOf(s));
	}

	public void incScale()
	{
		float scale = getScale();
		scale += .005F;
		setScale(scale);
	}

	private boolean teleportRandomly(EntityPlayer target)
	{
		double d0 = this.posX + (this.rand.nextDouble() - 0.5D) * 64.0D;
		double d1 = this.posY + (double)(this.rand.nextInt(64) - 32);
		double d2 = this.posZ + (this.rand.nextDouble() - 0.5D) * 64.0D;

		return target.attemptTeleport(d0, d1, d2);
	}

	private boolean teleportTo(EntityPlayer target)
	{
		double d0 = this.posX + (this.rand.nextInt(2) == 0 ? -1 : 1);
		double d1 = this.posY;
		double d2 = this.posZ + (this.rand.nextInt(2) == 0 ? -1 : 1);

		return target.attemptTeleport(d0, d1, d2);
	}

	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		
		if(getStage() == 1 && !rider)
		{
			ItemStack bow = new ItemStack(Items.BOW);
			bow.addEnchantment(Enchantment.getEnchantmentByID(48), 2);
			bow.addEnchantment(Enchantment.getEnchantmentByID(51), 1);
			this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, bow);

			this.rider = true;
		}
		else if(getStage() == 2 && !bowman)
		{
			ItemStack bow = new ItemStack(Items.BOW);
			bow.addEnchantment(Enchantment.getEnchantmentByID(48), 4);
			bow.addEnchantment(Enchantment.getEnchantmentByID(51), 1);
			this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, bow);

			this.bowman = true;
		}
		else if(getStage() == 3 && !melee)
		{
			ItemStack bow = new ItemStack(Items.DIAMOND_SWORD);
			bow.addEnchantment(Enchantment.getEnchantmentByID(19), 3);
			bow.addEnchantment(Enchantment.getEnchantmentByID(16), 3);
			this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, bow);

			this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
			this.setSize(2.0F, 6.2F);

			this.setCombatTask();
			this.melee = true;
		}
	}

	@Override
	public void updateRidden()
	{
		super.updateRidden();

		if (this.getRidingEntity() instanceof EntityCreature)
		{
			EntityCreature entitycreature = (EntityCreature)this.getRidingEntity();
			this.renderYawOffset = entitycreature.renderYawOffset;
		}
	}
	
	@Override
	public void setDead()
	{
		for(AbstractSkeleton s : skellys)
		{
			if(s != null)
				s.setDead();
		}
		
		super.setDead();
	}

	@SideOnly(Side.CLIENT)
	public boolean isSwingingArms()
	{
		return ((Boolean)this.dataManager.get(SWINGING_ARMS)).booleanValue();
	}

	public void setSwingingArms(boolean swingingArms)
	{
		this.dataManager.set(SWINGING_ARMS, Boolean.valueOf(swingingArms));
	}

	public void setBossBar(float a)
	{
		setBossBar(a, 3);
	}

	class AICastingSpell extends EntityBoss.AICastingSpell
	{
		private final EntityGhostRider ghost;

		private AICastingSpell(EntityGhostRider ghost)
		{
			super();
			this.ghost = ghost;
		}

		@Override
		public void updateTask()
		{
			if(ghost.getAttackTarget() != null)
				ghost.getLookHelper().setLookPositionWithEntity(ghost.getAttackTarget(), (float)ghost.getHorizontalFaceSpeed(), (float)ghost.getVerticalFaceSpeed());
		}
	}

	class AISummonSpell extends EntityBoss.AIUseSpell
	{
		private final EntityGhostRider ghost;

		private AISummonSpell(EntityGhostRider ghost)
		{
			super();
			this.ghost = ghost;
		}

		@Override
		public boolean shouldExecute()
		{
			if(!super.shouldExecute())
				return false;
			else if(ghost.getStage() < 3)
				return false;
			else if(ghost.getIsInvulnerable())
				return false;

			int i = ghost.world.getEntitiesWithinAABB(AbstractSkeleton.class, ghost.getEntityBoundingBox().grow(16.0D)).size();
			return ghost.rand.nextInt(8) + 1 > 1;
		}

		@Override
		protected int getCastingTime()
		{
			return 0;
		}

		protected int getCastingInterval() 
		{
			return ghost.rand.nextInt(200) + 100;
		}

		@Override
		protected SoundEvent getSpellPrepareSound()
		{
			return SoundEvents.EVOCATION_ILLAGER_PREPARE_SUMMON;
		}

		@Override
		protected SpellType getSpellType()
		{
			return EntityBoss.SpellType.SUMMON;
		}

		@Override
		protected void castSpell()
		{
			for(int i = 0; i < 4; ++i)
			{
				BlockPos blockpos = (new BlockPos(ghost)).add(-2 + ghost.rand.nextInt(5), 1, -2 + ghost.rand.nextInt(5));
				AbstractSkeleton skelly = null;

				switch(ghost.rand.nextInt(3))
				{
					default:
					case 0:
						skelly = new EntitySkeleton(ghost.world);
						break;
					case 1:
						skelly = new EntityWitherSkeleton(ghost.world);
						break;
					case 2:
						skelly = new EntityStray(ghost.world);
						break;
				}

				skelly.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
				skelly.moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);
				skelly.onInitialSpawn(ghost.world.getDifficultyForLocation(blockpos), (IEntityLivingData)null);
				ghost.world.spawnEntity(skelly);
				
				ghost.skellys.add(skelly);
			}
		}
	}

	class AIPush extends EntityBoss.AIUseSpell
	{
		private final EntityGhostRider ghost;

		private AIPush(EntityGhostRider ghost)
		{
			super();
			this.ghost = ghost;
		}

		@Override
		public boolean shouldExecute()
		{
			if(!super.shouldExecute())
				return false;
			else if(ghost.getStage() != 2)
				return false;
			else if(ghost.getIsInvulnerable())
				return false;

			return true;
		}

		@Override
		protected int getCastingTime()
		{
			return 0;
		}

		@Override
		protected int getCastingInterval()
		{
			return ghost.rand.nextInt(80) + 20;
		}

		@Override
		protected void castSpell()
		{
			List<EntityPlayer> possibleTarget = ghost.world.getEntitiesWithinAABB(EntityPlayer.class, ghost.getEntityBoundingBox().grow(5.0D));

			if(!possibleTarget.isEmpty())
			{
				EntityPlayer target = possibleTarget.get(ghost.rand.nextInt(possibleTarget.size()));
				possibleTarget.remove(target);
				
				while(target.capabilities.isCreativeMode && !possibleTarget.isEmpty())
				{
					target = possibleTarget.get(ghost.rand.nextInt(possibleTarget.size()));
					possibleTarget.remove(target);
				}

				boolean flag = ghost.teleportRandomly(target);

				if(flag)
				{
					ghost.world.playSound((EntityPlayer)null, target.prevPosX, target.prevPosY, target.prevPosZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, ghost.getSoundCategory(), 1000.0F, 1.0F);

					ghost.world.playSound((EntityPlayer)null, target.prevPosX, target.prevPosY, target.prevPosZ, ModSounds.pull, ghost.getSoundCategory(), 1000.0F, 1.5F);
				}
			}
		}

		@Override
		protected SoundEvent getSpellPrepareSound()
		{
			return null;
		}

		@Override
		protected SpellType getSpellType()
		{
			return SpellType.SPELL;
		}
	}

	class AIPull extends EntityBoss.AIUseSpell
	{
		private final EntityGhostRider ghost;

		private AIPull(EntityGhostRider ghost)
		{
			super();
			this.ghost = ghost;
		}

		@Override
		public boolean shouldExecute()
		{
			if(!super.shouldExecute())
				return false;
			else if(ghost.getStage() != 3)
				return false;
			else if(ghost.getIsInvulnerable())
				return false;

			return true;
		}

		@Override
		protected int getCastingTime()
		{
			return 0;
		}

		@Override
		protected int getCastingInterval()
		{
			return ghost.rand.nextInt(80) + 20;
		}

		@Override
		protected void castSpell()
		{
			List<EntityPlayer> possibleTarget = ghost.world.getEntitiesWithinAABB(EntityPlayer.class, ghost.getEntityBoundingBox().grow(16.0D));

			if(!possibleTarget.isEmpty())
			{
				EntityPlayer target = possibleTarget.get(ghost.rand.nextInt(possibleTarget.size()));
				possibleTarget.remove(target);
				
				while(target.capabilities.isCreativeMode && !possibleTarget.isEmpty())
				{
					target = possibleTarget.get(ghost.rand.nextInt(possibleTarget.size()));
					possibleTarget.remove(target);
				}

				boolean flag = ghost.teleportTo(target);

				if(flag)
				{
					ghost.world.playSound((EntityPlayer)null, target.prevPosX, target.prevPosY, target.prevPosZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, ghost.getSoundCategory(), 1000.0F, 1.0F);

					ghost.world.playSound((EntityPlayer)null, target.prevPosX, target.prevPosY, target.prevPosZ, ModSounds.push, ghost.getSoundCategory(), 1000.0F, 2.0F);
				}
			}

		}

		@Override
		protected SoundEvent getSpellPrepareSound()
		{
			return null;
		}

		@Override
		protected SpellType getSpellType()
		{
			return SpellType.SPELL;
		}
	}
}
