package com.iamshift.mineaddons.potions;

import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.init.ModPotions;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class PotionDoubleHealth extends Potion
{
	public PotionDoubleHealth(String name)
	{
		super(false, 16737380);
		setPotionName("potion." + name);
		setRegistryName(Refs.ID, name);
		setIconIndex(0, 0);

		registerPotionAttributeModifier(SharedMonsterAttributes.MAX_HEALTH, "5a19ba81-d8d0-4c8f-b348-a5ab75950731", 20.0D, 0);
		setBeneficial();
		
		ModPotions.potions.add(this);
	}

	@Override
	public void removeAttributesModifiersFromEntity(EntityLivingBase entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier) 
	{
		super.removeAttributesModifiersFromEntity(entityLivingBaseIn, attributeMapIn, amplifier);

//		if (entityLivingBaseIn.getHealth() > entityLivingBaseIn.getMaxHealth())
//			entityLivingBaseIn.setHealth(entityLivingBaseIn.getMaxHealth());
	}

	@Override
	public void applyAttributesModifiersToEntity(EntityLivingBase entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier) 
	{
		super.applyAttributesModifiersToEntity(entityLivingBaseIn, attributeMapIn, 0);

//		if (entityLivingBaseIn.getHealth() < entityLivingBaseIn.getMaxHealth())
//			entityLivingBaseIn.setHealth(entityLivingBaseIn.getMaxHealth());
	}

	@Override
	public boolean isReady(int duration, int amplifier) 
	{
		return true;
	}
	
	@Override
	public boolean shouldRender(PotionEffect effect) 
	{
		return false;
	}
	
	@Override
	public boolean shouldRenderInvText(PotionEffect effect) 
	{
		return false;
	}
	
	@Override
	public int getStatusIconIndex() 
	{
		Minecraft.getMinecraft().renderEngine.bindTexture(ModPotions.icon);
		return super.getStatusIconIndex();
	}
}
