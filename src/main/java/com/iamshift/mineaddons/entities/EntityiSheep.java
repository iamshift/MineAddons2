package com.iamshift.mineaddons.entities;

import java.util.List;

import com.iamshift.mineaddons.core.Config;
import com.iamshift.mineaddons.entities.ais.EntityAIWanderAvoidLava;
import com.iamshift.mineaddons.init.ModLoot;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.DimensionType;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityiSheep extends EntityAnimal implements IShearable
{
	private static final DataParameter<Boolean> SHEARED = EntityDataManager.<Boolean>createKey(EntityiSheep.class, DataSerializers.BOOLEAN);
	private int sheepTimer;

	public EntityiSheep(World worldIn)
	{
		super(worldIn);
		this.setSize(0.9F, 1.3F);
		this.isImmuneToFire = true;
	}

	@Override
	protected void initEntityAI()
	{
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityHellhound.class, 6.0F, 1.0D, 1.2D));
		this.tasks.addTask(2, new EntityAITempt(this, 1.1D, Items.WHEAT, false));
		this.tasks.addTask(3, new EntityAIAttackMelee(this, 1.0D, false));
		this.tasks.addTask(4, new EntityAIWanderAvoidLava(this, 1.0D));
		this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		this.tasks.addTask(5, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
		this.targetTasks.addTask(0, new EntityAIHurtByTarget(this, false, new Class[0]));
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(12.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(SHEARED, Boolean.valueOf(false));
	}

	@Override
	protected void updateAITasks()
	{
		this.sheepTimer = 40;
		super.updateAITasks();
	}

	@Override
	public void onLivingUpdate()
	{
		if(this.world.isRemote)
			this.sheepTimer = Math.max(0, this.sheepTimer - 1);

		super.onLivingUpdate();
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
	public void handleStatusUpdate(byte id)
	{
		if(id == 10)
			this.sheepTimer = 40;
		else
			super.handleStatusUpdate(id);
	}

	@SideOnly(Side.CLIENT)
	public float getHeadRotationPointY(float p_70894_1_)
	{
		if (this.sheepTimer <= 0)
		{
			return 0.0F;
		}
		else if (this.sheepTimer >= 4 && this.sheepTimer <= 36)
		{
			return 1.0F;
		}
		else
		{
			return this.sheepTimer < 4 ? ((float)this.sheepTimer - p_70894_1_) / 4.0F : -((float)(this.sheepTimer - 40) - p_70894_1_) / 4.0F;
		}
	}

	@SideOnly(Side.CLIENT)
	public float getHeadRotationAngleX(float p_70890_1_)
	{
		if (this.sheepTimer > 4 && this.sheepTimer <= 36)
		{
			float f = ((float)(this.sheepTimer - 4) - p_70890_1_) / 32.0F;
			return ((float)Math.PI / 5F) + ((float)Math.PI * 7F / 100F) * MathHelper.sin(f * 28.7F);
		}
		else
		{
			return this.sheepTimer > 0 ? ((float)Math.PI / 5F) : this.rotationPitch * 0.017453292F;
		}
	}

	@Override
	protected ResourceLocation getLootTable()
	{
		if (this.isSheared())
			return LootTableList.ENTITIES_SHEEP;
		else
			return ModLoot.ISHEEP;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		compound.setBoolean("Sheared", this.isSheared());
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.setSheared(compound.getBoolean("Sheared"));
	}

	@Override
	protected SoundEvent getAmbientSound()
	{
		return SoundEvents.ENTITY_SHEEP_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return SoundEvents.ENTITY_SHEEP_HURT;
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ENTITY_SHEEP_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, Block blockIn)
	{
		this.playSound(SoundEvents.ENTITY_SHEEP_STEP, 0.15F, 1.0F);
	}

	@Override
	protected float getSoundPitch()
	{
		return 0.35F;
	}

	public boolean isSheared()
	{
		return ((Boolean)this.dataManager.get(SHEARED)).booleanValue();	
	}

	public void setSheared(boolean sheared)
	{
		this.dataManager.set(SHEARED, Boolean.valueOf(sheared));
	}

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
	{
		livingdata = super.onInitialSpawn(difficulty, livingdata);
		return livingdata;
	}

	@Override public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos){ return !this.isSheared() && !this.isChild(); }
	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
	{
		this.setSheared(true);
		int i = 1 + this.rand.nextInt(3);

		List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
		for (int j = 0; j < i; ++j)
			ret.add(new ItemStack(Item.getItemFromBlock(Blocks.MAGMA), 1, 0));

		this.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);
		return ret;
	}

	@Override
	public float getEyeHeight()
	{
		return 0.95F * this.height;
	}

	@Override
	public int getMaxFallHeight()
	{
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn)
	{
		boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float)((int)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));

		if(flag)
			this.applyEnchantments(this, entityIn);

		return flag;
	}

	@Override
	protected void setOnFireFromLava() {}

	@Override
	public void setFire(int seconds) {}

	@Override
	protected void dealFireDamage(int amount) {}

	@Override
	public boolean getCanSpawnHere()
	{
		if(Config.iSheepSpawnRate > 0)
		{
			if(this.world.getDifficulty() != EnumDifficulty.PEACEFUL && this.world.provider.getDimensionType() == DimensionType.NETHER && !isEntityInsideOpaqueBlock())
			{
				if(this.rand.nextInt(Config.iSheepSpawnRate) == 0)
					return true;
			}
		}

		return false;
	}

	@Override
	public EntityAgeable createChild(EntityAgeable ageable)
	{
		return null;
	}
}
