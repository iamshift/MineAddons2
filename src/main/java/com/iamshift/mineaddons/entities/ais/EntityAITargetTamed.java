package com.iamshift.mineaddons.entities.ais;

import com.google.common.base.Predicate;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAITargetTamed<T extends EntityLivingBase> extends EntityAINearestAttackableTarget<T>
{
	private final EntityTameable tameable;
	
	public EntityAITargetTamed(EntityTameable entityIn, Class<T> classTarget, boolean checkSight, Predicate <? super T > targetSelector)
    {
        super(entityIn, classTarget, 10, checkSight, false, targetSelector);
        this.tameable = entityIn;
    }
	
	@Override
	public boolean shouldExecute()
	{
		return this.tameable.isTamed() && super.shouldExecute();
	}
}
