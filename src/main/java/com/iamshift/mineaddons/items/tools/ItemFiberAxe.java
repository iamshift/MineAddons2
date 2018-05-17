package com.iamshift.mineaddons.items.tools;

import java.util.ArrayList;
import java.util.List;

import com.iamshift.mineaddons.MineAddons;
import com.iamshift.mineaddons.core.Config;
import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.init.ModItems;
import com.iamshift.mineaddons.interfaces.IHasModel;
import com.iamshift.mineaddons.interfaces.IRecipeProvider;

import net.minecraft.init.Items;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ItemFiberAxe extends ItemTool implements IHasModel, IRecipeProvider
{
	public ItemFiberAxe(String name)
	{
		super(12F, -3.0F, ToolMaterial.DIAMOND, ItemAxe.EFFECTIVE_ON);

		setUnlocalizedName(name);
		setRegistryName(new ResourceLocation(Refs.ID, name));

		setHarvestLevel("axe", 6);
		setMaxDamage(1375);

		setCreativeTab(MineAddons.minetab);

		ModItems.ITEMS.add(this);
	}
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
		if(!Config.Tinker)
		{
			if(toRepair.getItem() instanceof ItemFiberAxe)
			{
				if(repair.isItemEqual(new ItemStack(ModItems.Fiberglass)))
					return true;

				return false;
			}
		}
		return super.getIsRepairable(toRepair, repair);
	}

	@Override
	public void registerModels()
	{
		MineAddons.proxy.registerItemRenderer(this, 0, "inventory");
	}

	@Override
	public List<IRecipe> getRecipe()
	{
		if(!Config.Tinker && !Config.Foundry)
		{
			List<IRecipe> list = new ArrayList<IRecipe>();
			list.add(
					new ShapedOreRecipe(new ResourceLocation(Refs.ID), 
							new ItemStack(this, 1), 
							new Object[] {
									"FFF", " P ", " S ", 
									'P', new ItemStack(Items.DIAMOND_AXE, 1),
									'F', new ItemStack(ModItems.Fiberglass, 1, 0),
									'S', new ItemStack(Items.NETHER_STAR, 1)
					}).setRegistryName(new ResourceLocation(Refs.ID, "fiber_axe"))
					);

			return list;
		}

		return null;
	}
}
