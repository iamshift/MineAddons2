package com.iamshift.mineaddons.entities.boss;

import com.iamshift.mineaddons.interfaces.IUncapturable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityDeadHorse extends AbstractHorse implements IUncapturable
{
	private static final DataParameter<Boolean> GHOST = EntityDataManager.<Boolean>createKey(EntityDeadHorse.class, DataSerializers.BOOLEAN);

	public EntityDeadHorse(World worldIn)
	{
		super(worldIn);
		this.isImmuneToFire = true;
	}
	
	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(150.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
		this.getEntityAttribute(JUMP_STRENGTH).setBaseValue(this.getModifiedJumpStrength());
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(GHOST, Boolean.valueOf(false));
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		this.dataManager.set(GHOST, Boolean.valueOf(compound.getBoolean("GhostRider")));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		compound.setBoolean("GhostRider", this.isGhostHorse());
	}

	public void setGhost()
	{
		this.setGhostHorse(true);
	}

	protected void setGhostHorse(boolean flag)
	{
		this.dataManager.set(GHOST, Boolean.valueOf(flag));

		if(flag)
		{
			this.setSize(2.2F, 2.3F);
			this.setPosition(this.posX, this.posY, this.posZ);
		}
	}

	public boolean isGhostHorse()
	{
		return ((Boolean)this.dataManager.get(GHOST)).booleanValue();
	}

	@Override
	public void notifyDataManagerChange(DataParameter<?> key)
	{
		if(GHOST.equals(key))
		{
			if(this.isGhostHorse())
				this.setSize(2.2F, 2.3F);
			else
				this.setSize(1.3964844F, 1.6F);
		}

		super.notifyDataManagerChange(key);
	}

	@Override
	protected SoundEvent getAmbientSound()
	{
		super.getAmbientSound();
		return SoundEvents.ENTITY_SKELETON_HORSE_AMBIENT;
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		super.getDeathSound();
		return SoundEvents.ENTITY_SKELETON_HORSE_DEATH;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		super.getHurtSound(damageSourceIn);
		return SoundEvents.ENTITY_SKELETON_HORSE_HURT;
	}

	@Override
	public EnumCreatureAttribute getCreatureAttribute()
	{
		return EnumCreatureAttribute.UNDEAD;
	}

	@Override
	public double getMountedYOffset()
	{
		return super.getMountedYOffset() - 0.25D;
	}

	@Override
	protected ResourceLocation getLootTable()
	{
		return LootTableList.ENTITIES_SKELETON_HORSE;
	}

	public void setSaddle() 
	{
		this.horseChest.setInventorySlotContents(0, new ItemStack(Items.SADDLE, 1, 0));
	}
	
	@Override
	public boolean isEntityInvulnerable(DamageSource source)
	{
		if(this.isGhostHorse())
		{
			if(this.isBeingRidden())
			{
				Entity entity = this.getPassengers().get(0);
				if(entity instanceof EntityGhostRider)
				{
					if(((EntityGhostRider)entity).getInvulTime() > 0)
						return false;
				}
			}
		}
		
		return super.isEntityInvulnerable(source);
	}
	
	@Override
	public boolean getIsInvulnerable()
	{
		if(this.isGhostHorse())
		{
			if(this.isBeingRidden())
			{
				Entity entity = this.getPassengers().get(0);
				if(entity instanceof EntityGhostRider)
				{
					if(((EntityGhostRider)entity).getInvulTime() > 0)
						return false;
				}
			}
		}
		
		return super.getIsInvulnerable();
	}
	
	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand)
	{
		ItemStack itemstack = player.getHeldItem(hand);
		boolean flag = !itemstack.isEmpty();

		if (flag && itemstack.getItem() == Items.SPAWN_EGG)
		{
			return super.processInteract(player, hand);
		}
		else if (!this.isTame())
		{
			return false;
		}
		else if (this.isChild())
		{
			return super.processInteract(player, hand);
		}
		else if (player.isSneaking())
		{
			this.openGUI(player);
			return true;
		}
		else if (this.isBeingRidden())
		{
			return super.processInteract(player, hand);
		}
		else
		{
			if (flag)
			{
				if (itemstack.getItem() == Items.SADDLE && !this.isHorseSaddled())
				{
					this.openGUI(player);
					return true;
				}

				if (itemstack.interactWithEntity(player, this, hand))
				{
					return true;
				}
			}

			if(this.getOwnerUniqueId() == player.getUniqueID())
				this.mountTo(player);

			return true;
		}
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if(this.isBeingRidden())
		{
			Entity entity = this.getPassengers().get(0);
			if(entity instanceof EntityGhostRider)
			{
				if(source.getTrueSource() instanceof EntityGhostRider)
					return false;
				
				EntityGhostRider gr = (EntityGhostRider) entity;

				if(gr.getStage() == 1)
				{
					if((gr.getHealth() - amount) < 300F)
					{
						gr.setHealth(300F);
						gr.setInvulTime(60);
						gr.updateBars();
						return super.attackEntityFrom(source, amount);
					}
					else
						gr.setHealth(gr.getHealth() - amount);
				}

				if(gr.getInvulTime() > 0)
					return false;
			}
		}
		else if(this.isEntityInvulnerable(source))
			return false;
		
		return super.attackEntityFrom(source, amount);
	}
	
	@Override
	public void onDeath(DamageSource cause)
	{
		if(this.isBeingRidden())
		{
			if(this.isGhostHorse())
			{
				EntityGhostRider gr = (EntityGhostRider) this.getPassengers().get(0);
				
				if(gr.getStage() == 1)
				{
					gr.setHealth(300F);
					gr.setInvulTime(60);
					gr.updateBars();
				}
			}
		}
		
		super.onDeath(cause);
	}

	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();

		if(this.isGhostHorse())
		{
			if(this.isBeingRidden())
			{
				Entity entity = this.getPassengers().get(0);
				if(entity instanceof EntityGhostRider)
				{
					if(((EntityGhostRider)entity).getStage() == 1)
						((EntityGhostRider)entity).setBossBar(this.getHealth() / 150F);
				}
			}
			
			if(this.world.getDifficulty() == EnumDifficulty.PEACEFUL)
				this.setDead();
		}
	}

}
