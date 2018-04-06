package com.iamshift.mineaddons.potions;

import com.iamshift.mineaddons.core.Refs;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionBooster extends Potion
{
	public PotionBooster(String name)
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
	
	public boolean shouldRender(PotionEffect effect) 
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
	@SideOnly(Side.CLIENT)
	public boolean hasStatusIcon()
	{
		return false;
	}
}
