package com.iamshift.mineaddons.potions;

import com.iamshift.mineaddons.core.Refs;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class PotionMobChanger extends Potion
{
	public PotionMobChanger(String name)
	{
		super(false, 0000000);
		setPotionName("potion." + name);
		setRegistryName(Refs.ID, name);
		setIconIndex(0, 0);
	}
	
	@Override
	public boolean isReady(int duration, int amplifier)
	{
		return false;
	}
	
	public boolean shouldRender(net.minecraft.potion.PotionEffect effect) 
	{
		return false;
	};
	
	@Override
	public boolean shouldRenderHUD(PotionEffect effect)
	{
		return false;
	}
	
	@Override
	public boolean shouldRenderInvText(PotionEffect effect)
	{
		return false;
	}
	
	@Override
	public boolean hasStatusIcon()
	{
		return false;
	}
}
