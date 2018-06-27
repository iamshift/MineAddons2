package com.iamshift.mineaddons.integration.tinkers;

import com.brandon3055.draconicevolution.DEFeatures;
import com.brandon3055.draconicevolution.magic.EnchantmentReaper;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.modifiers.IModifier;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;
import slimeknights.tconstruct.library.modifiers.ModifierNBT;
import slimeknights.tconstruct.library.modifiers.ModifierTrait;
import slimeknights.tconstruct.library.modifiers.TinkerGuiException;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import slimeknights.tconstruct.library.utils.ToolBuilder;

public class ModifierReaper extends ModifierTrait
{
	protected static final int baseCount = 12;
	protected static final int maxLevel = 5;

	private final ReaperAspect aspect;

	public ModifierReaper()
	{
		super("draconic_reaper", 0xad42f4, maxLevel, 0);

		aspects.clear();
		aspect = new ReaperAspect(this);
		addAspects(aspect, new ModifierAspect.CategoryAnyAspect(Category.WEAPON));
		
		addItem(new ItemStack(DEFeatures.draconiumBlock), 1, 1);
	}

	public int getReaperLevel(ItemStack itemStack) 
	{
		return getReaperLevel(TinkerUtil.getModifierTag(itemStack, getModifierIdentifier()));
	}

	public int getReaperLevel(NBTTagCompound modifierTag) 
	{
		ModifierNBT.IntegerNBT data = ModifierNBT.readInteger(modifierTag);
		return aspect.getLevel(data.current);
	}

	@Override
	public void applyEffect(NBTTagCompound rootCompound, NBTTagCompound modifierTag)
	{
		super.applyEffect(rootCompound, modifierTag);

		int lvl = getReaperLevel(modifierTag);

		applyEnchantments(rootCompound, lvl);
	}

	@Override
	public void afterHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damageDealt, boolean wasCritical, boolean wasHit)
	{
		if(player.world.isRemote || !wasHit) 
			return;

		for(int i = (int) (damageDealt / 2f); i > 0; i--) 
			rewardProgress(tool);
	}

	public void rewardProgress(ItemStack tool) 
	{
		if(random.nextFloat() > 0.03f) 
			return;

		try 
		{
			if(canApply(tool, tool)) 
				apply(tool);
		} 
		catch(TinkerGuiException e) 
		{}
	}

	protected void applyEnchantments(NBTTagCompound rootCompound, int lvl) 
	{
		boolean weapon = false;
		lvl = Math.min(lvl, EnchantmentReaper.instance.getMaxLevel());

		for(Category category : TagUtil.getCategories(rootCompound)) 
		{
			if(category == Category.WEAPON) 
				weapon = true;
		}

		if(weapon) 
		{
			while(lvl > ToolBuilder.getEnchantmentLevel(rootCompound, EnchantmentReaper.instance)) 
			{
				ToolBuilder.addEnchantment(rootCompound, EnchantmentReaper.instance);
			}
		}
	}
	
	@Override
	public String getTooltip(NBTTagCompound modifierTag, boolean detailed)
	{
		int level = getReaperLevel(modifierTag);

	    String tooltip = getLocalizedName();
	    if(level > 0) 
	    {
	      tooltip += " " + TinkerUtil.getRomanNumeral(level);
	    }

	    if(detailed) 
	    {
	      ModifierNBT data = ModifierNBT.readInteger(modifierTag);
	      tooltip += " " + data.extraInfo;
	    }
	    
	    return tooltip;
	}


	public static class ReaperAspect extends ModifierAspect.MultiAspect
	{
		public ReaperAspect(IModifier parent)
		{
			super(parent, 0x2d51e2, maxLevel, baseCount, 1);

			freeModifierAspect = new FreeFirstModifierAspect(parent, 1);
		}

		@Override
		protected int getMaxForLevel(int level)
		{
			return (countPerLevel * level);
		}

		public int getLevel(int current)
		{
			int i = 0;
			while(current >= getMaxForLevel(i + 1))
				i++;

			return i; 
		}
	}
}
