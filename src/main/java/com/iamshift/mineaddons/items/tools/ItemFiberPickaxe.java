package com.iamshift.mineaddons.items.tools;

import java.util.ArrayList;
import java.util.List;

import com.iamshift.mineaddons.MineAddons;
import com.iamshift.mineaddons.core.Config;
import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.init.ModItems;
import com.iamshift.mineaddons.interfaces.IHasModel;
import com.iamshift.mineaddons.interfaces.IRecipeProvider;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ItemFiberPickaxe extends ItemTool implements IHasModel, IRecipeProvider
{
	public ItemFiberPickaxe(String name)
	{
		super(4.0F, -2.8F, ToolMaterial.DIAMOND, ItemPickaxe.EFFECTIVE_ON);
		setUnlocalizedName(name);
		setRegistryName(new ResourceLocation(Refs.ID, name));

		setHarvestLevel("pickaxe", 6);
		setMaxDamage(1375);

		setCreativeTab(MineAddons.minetab);

		ModItems.ITEMS.add(this);
	}

	@Override
	public boolean canHarvestBlock(IBlockState blockIn)
	{
		Block block = blockIn.getBlock();

		if (block == Blocks.OBSIDIAN)
		{
			return this.toolMaterial.getHarvestLevel() == 3;
		}
		else if (block != Blocks.DIAMOND_BLOCK && block != Blocks.DIAMOND_ORE)
		{
			if (block != Blocks.EMERALD_ORE && block != Blocks.EMERALD_BLOCK)
			{
				if (block != Blocks.GOLD_BLOCK && block != Blocks.GOLD_ORE)
				{
					if (block != Blocks.IRON_BLOCK && block != Blocks.IRON_ORE)
					{
						if (block != Blocks.LAPIS_BLOCK && block != Blocks.LAPIS_ORE)
						{
							if (block != Blocks.REDSTONE_ORE && block != Blocks.LIT_REDSTONE_ORE)
							{
								Material material = blockIn.getMaterial();

								if (material == Material.ROCK)
								{
									return true;
								}
								else if (material == Material.IRON)
								{
									return true;
								}
								else
								{
									return material == Material.ANVIL;
								}
							}
							else
							{
								return this.toolMaterial.getHarvestLevel() >= 2;
							}
						}
						else
						{
							return this.toolMaterial.getHarvestLevel() >= 1;
						}
					}
					else
					{
						return this.toolMaterial.getHarvestLevel() >= 1;
					}
				}
				else
				{
					return this.toolMaterial.getHarvestLevel() >= 2;
				}
			}
			else
			{
				return this.toolMaterial.getHarvestLevel() >= 2;
			}
		}
		else
		{
			return this.toolMaterial.getHarvestLevel() >= 2;
		}
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
		if(!Config.Tinker)
		{
			if(toRepair.getItem() instanceof ItemFiberPickaxe)
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
									'P', new ItemStack(Items.DIAMOND_PICKAXE, 1),
									'F', new ItemStack(ModItems.Fiberglass, 1, 0),
									'S', new ItemStack(Items.NETHER_STAR, 1)
					}).setRegistryName(new ResourceLocation(Refs.ID, "fiber_pickaxe"))
					);

			return list;
		}

		return null;
	}
}
