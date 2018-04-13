package com.iamshift.mineaddons.entities;

import com.iamshift.mineaddons.init.ModLoot;

import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

public class EntityBlazelier extends EntityCreature
{
	public EntityBlazelier(World worldIn)
	{
		super(worldIn);
	}
	
	@Override
	public EnumPushReaction getPushReaction()
	{
		return EnumPushReaction.IGNORE;
	}
	
	@Override
	protected void initEntityAI() 
	{
		this.tasks.addTask(8, new EntityAILookIdle(this));
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1D);
	}

	@Override
	protected float getSoundVolume()
	{
		return 0F;
	}

	@Override
	protected ResourceLocation getLootTable()
	{
		return ModLoot.BLAZELLIER;
	}

	@Override
	public boolean isEntityInvulnerable(DamageSource source)
	{
		return super.isEntityInvulnerable(source) || !((source.getTrueSource() instanceof EntityPlayer) && !(source.getTrueSource() instanceof FakePlayer));
	}
	
	@Override
	public boolean attackEntityAsMob(Entity entityIn)
	{
		return false;
	}
	
	@Override
	protected boolean canDespawn() 
	{
		return false;
	}
	
	@Override
	public boolean getCanSpawnHere()
	{
		return false;
	}
	
	@Override
	protected boolean canDropLoot()
	{
		return true;
	}
	
	@Override
	public void onLivingUpdate() {}
	
	@Override
	protected boolean processInteract(EntityPlayer player, EnumHand hand)
	{
		if(player.getHeldItem(EnumHand.MAIN_HAND).isEmpty())
		{
			float r = this.getRotationYawHead();

			if(player.isSneaking())
			{
				r -= 22.5F;
				if(r <= 0.0F)
					r += 360.0F;
			}
			else
			{
				r += 22.5F;
				if(r >= 360.0F)
					r -= 360.0F;
			}

			this.setRotationYawHead(r);

			return true;
		}

		return super.processInteract(player, hand);
	}
	
	@Override
	public void onDeath(DamageSource cause)
	{
		super.onDeath(cause);

		BlockPos pos = new BlockPos(this.getPosition().getX(), this.getPosition().getY() - 1, this.getPosition().getZ());
		this.world.setBlockToAir(pos);
	}
}
