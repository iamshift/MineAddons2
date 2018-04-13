package com.iamshift.mineaddons.entities;

import com.iamshift.mineaddons.interfaces.IUncapturable;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

public class EntityBrainlessShulker extends EntityShulker implements IUncapturable
{
	public EntityBrainlessShulker(World worldIn) 
	{
		super(worldIn);
	}

	@Override
	protected void initEntityAI()
	{
		this.tasks.addTask(1, new EntityBrainlessShulker.AIPeek());
		this.tasks.addTask(2, new EntityAILookIdle(this));
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1D);
	}

	@Override
	protected SoundEvent getAmbientSound()
	{
		return null;
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return null;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return null;
	}

	@Override
	protected float getSoundVolume()
	{
		return 0F;
	}

	@Override
	protected ResourceLocation getLootTable()
	{
		return null;
	}

	@Override
	protected boolean tryTeleportToNewPosition()
	{
		return false;
	}

	@Override
	public boolean isEntityInvulnerable(DamageSource source)
	{
		return super.isEntityInvulnerable(source) || !((source.getTrueSource() instanceof EntityPlayer) && !(source.getTrueSource() instanceof FakePlayer));
	}

	public void setColor(EnumDyeColor c)
	{
		this.dataManager.set(COLOR, Byte.valueOf((byte)c.getMetadata()));
	}

	@Override
	protected boolean processInteract(EntityPlayer player, EnumHand hand) 
	{
		if(!player.world.isRemote)
			return false;

		if(player.getHeldItem(hand).isEmpty())
			return false;

		ItemStack stack = player.getHeldItem(hand);

		if(!(stack.getItem() instanceof ItemDye))
			return false;
		
		EnumDyeColor color = EnumDyeColor.byDyeDamage(stack.getItemDamage());
		setColor(color);
		
		return true;
	}
	
	@Override
	protected boolean canDespawn() 
	{
		return false;
	}
	
	@Override
	public EnumDyeColor getColor() 
	{
		return EnumDyeColor.byMetadata(((Byte)this.dataManager.get(COLOR)).byteValue());
	}
	
	class AIPeek extends EntityAIBase
	{
		private int peekTime;

		private AIPeek()
		{
		}

		public boolean shouldExecute()
		{
			return EntityBrainlessShulker.this.rand.nextInt(40) == 0;
		}

		public boolean shouldContinueExecuting()
		{
			return this.peekTime > 0;
		}

		public void startExecuting()
		{
			this.peekTime = 20 * (1 + EntityBrainlessShulker.this.rand.nextInt(3));
			EntityBrainlessShulker.this.updateArmorModifier(30);
		}

		public void resetTask()
		{
			EntityBrainlessShulker.this.updateArmorModifier(0);
		}

		public void updateTask()
		{
			--this.peekTime;
		}
	}
}
