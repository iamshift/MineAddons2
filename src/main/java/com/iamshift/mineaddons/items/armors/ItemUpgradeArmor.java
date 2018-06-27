package com.iamshift.mineaddons.items.armors;

import java.util.ArrayList;
import java.util.List;

import com.iamshift.mineaddons.core.Config;
import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.init.ModItems;

import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ItemUpgradeArmor extends ItemUltimateArmor
{
	public ItemUpgradeArmor(String name, ArmorMaterial material, int renderIndex, EntityEquipmentSlot slot)
	{
		super(name, material, renderIndex, slot);
	}
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
		if(!Config.Tinker)
		{
			if(toRepair.getItem() instanceof ItemUpgradeArmor)
			{
				if(repair.isItemEqual(new ItemStack(ModItems.HarmoniousIngot)))
					return true;

				return false;
			}
		}
		
		return false;
	}
	
	@Override
	public List<IRecipe> getRecipe()
	{
		return null;
	}
}
